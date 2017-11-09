package ua.agwebs.web.rest.entries;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ua.agwebs.root.entity.EntryLine;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.root.service.specifications.SearchCriteria;
import ua.agwebs.web.rest.entries.datatables.DataRow;
import ua.agwebs.web.rest.entries.datatables.DataTableRequest;
import ua.agwebs.web.rest.entries.datatables.DataTableResponse;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookEntryProvider implements BookEntryService {

    public static final Logger logger = LoggerFactory.getLogger(BookEntryProvider.class);

    private EntryService entryService;
    private ModelMapper modelMapper;

    @Autowired
    public BookEntryProvider(EntryService entryService, ModelMapper modelMapper) {
        this.entryService = entryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public DataTableResponse findEntry(DataTableRequest dataTableRequest) {
        Assert.notNull(dataTableRequest, "dataTableRequest can not be null.");

        PageRequest pageRequest = this.createPageRequest(dataTableRequest);
        Page<EntryLine> entryLines = entryService.findEntryLines(dataTableRequest.getFilters(), pageRequest);
        DataTableResponse dataTableResponse = this.createDataTableResponse(entryLines, dataTableRequest.getDraw());

        return dataTableResponse;
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

    protected DataTableResponse createDataTableResponse(Page<EntryLine> entryLinePage, Integer draw){
        Assert.notNull(draw, "Draw can not be null.");

        DataTableResponse dataTableResponse = new DataTableResponse(draw);
        dataTableResponse.setRecordsTotal(entryLinePage.getTotalElements());
        dataTableResponse.setRecordsFiltered(entryLinePage.getTotalElements());
        List<DataRow> dataRows = entryLinePage.getContent().stream().map(e -> modelMapper.map(e, DataRow.class)).collect(Collectors.toList());
        dataTableResponse.setData(dataRows);

        return dataTableResponse;
    }
}
