package ua.agwebs.web.rest.entries;

import org.aspectj.weaver.ArrayAnnotationValue;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.service.CoaService;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.root.service.specifications.SearchCriteria;
import ua.agwebs.web.exceptions.PocketBalanceIllegalAccessException;
import ua.agwebs.web.rest.PermissionService;
import ua.agwebs.web.rest.entries.datatables.DataRow;
import ua.agwebs.web.rest.entries.datatables.DataTableRequest;
import ua.agwebs.web.rest.entries.datatables.DataTableResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookEntryProvider implements BookEntryService {

    public static final Logger logger = LoggerFactory.getLogger(BookEntryProvider.class);

    private PermissionService permissionService;
    private EntryService entryService;
    private ModelMapper modelMapper;
    private CoaService coaService;

    @Autowired
    public BookEntryProvider(EntryService entryService, ModelMapper modelMapper, PermissionService permissionService, CoaService coaService) {
        this.entryService = entryService;
        this.modelMapper = modelMapper;
        this.permissionService = permissionService;
        this.coaService = coaService;
    }

    @Override
    public DataTableResponse findEntry(DataTableRequest dataTableRequest, long userId) {
        Assert.notNull(dataTableRequest, "dataTableRequest can not be null.");
        if (permissionService.checkPermission(dataTableRequest.getBookId(), userId)) {
            PageRequest pageRequest = this.createPageRequest(dataTableRequest);

            List<SearchCriteria> criterias = new ArrayList<>();
            criterias.addAll(dataTableRequest.getFilters());
            criterias.add(new SearchCriteria("header.book.id", SearchCriteria.CriteriaType.EQUALS, dataTableRequest.getBookId()));

            Page<EntryLine> entryLines = entryService.findEntryLines(criterias, pageRequest);
            DataTableResponse dataTableResponse = this.createDataTableResponse(entryLines, dataTableRequest.getDraw());

            return dataTableResponse;
        } else {
            throw new PocketBalanceIllegalAccessException();
        }
    }

    protected PageRequest createPageRequest(DataTableRequest dataTableRequest) {
        int page = dataTableRequest.getStart() / dataTableRequest.getLength();
        int size = dataTableRequest.getLength();
        Sort sort = this.createSort(dataTableRequest);
        return new PageRequest(page, size, sort);
    }

    protected Sort createSort(DataTableRequest dataTableRequest) {
        List<Sort.Order> sortOrders = dataTableRequest.getOrders()
                .stream()
                .map(e -> {
                    int clmIdx = e.getColumn();
                    String prop = dataTableRequest.getColumns().get(clmIdx).getName();
                    Sort.Direction dir = Sort.Direction.fromString(e.getDir());
                    return new Sort.Order(dir, prop);
                })
                .collect(Collectors.toList());

        Sort sort = sortOrders.size() == 0 ? null : new Sort(sortOrders);
        return sort;
    }

    protected DataTableResponse createDataTableResponse(Page<EntryLine> entryLinePage, Integer draw) {
        Assert.notNull(draw, "Draw can not be null.");

        DataTableResponse dataTableResponse = new DataTableResponse(draw);
        dataTableResponse.setRecordsTotal(entryLinePage.getTotalElements());
        dataTableResponse.setRecordsFiltered(entryLinePage.getTotalElements());
        List<DataRow> dataRows = entryLinePage.getContent().stream().map(e -> modelMapper.map(e, DataRow.class)).collect(Collectors.toList());
        dataTableResponse.setData(dataRows);

        return dataTableResponse;
    }

    @Override
    public FilterOptionsDto getFilterOptions(long bookId, long userId) {
        if (permissionService.checkPermission(bookId, userId)) {
            final FilterOptionsDto optionsDto = new FilterOptionsDto();

            coaService.findBalanceBookById(bookId)
                    .getAccounts()
                    .stream()
                    .forEach(e -> {
                        optionsDto.getCategories().add(e.getBsCategory());
                        optionsDto.getAccounts().put(e.getAccId(), e.getName());
                    });

            return optionsDto;
        } else {
            throw new PocketBalanceIllegalAccessException();
        }
    }
}
