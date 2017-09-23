package ua.agwebs.tests.unit.web.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.agwebs.root.entity.*;
import ua.agwebs.root.service.EntryService;
import ua.agwebs.root.service.specifications.SearchCriteria;
import ua.agwebs.web.rest.entries.BookEntryProvider;
import ua.agwebs.web.rest.entries.BookEntryService;
import ua.agwebs.web.rest.entries.datatables.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockBookEntryProviderTest {

    @Mock
    private EntryService entryService;

    private ModelMapper mapper = new ModelMapper();

    private final List<EntryLine> entryLines = new ArrayList<>();

    private BookEntryService bookEntryService;

    @Before
    public void setUpMock() {
        AppUser appUser = new AppUser("bf@u.com", "An", "Xe");

        BalanceBook book = new BalanceBook("t-book", "for testing", appUser);
        book.setId(7L);

        BalanceAccount account = new BalanceAccount(BSCategory.ASSET, 1002L, book, "Cash");
        account.setAccId(555L);

        entryLines.add(new EntryLine(101L, account, 205L, EntrySide.D, new Currency(840, "USD", "US Dollar")));

        bookEntryService = new BookEntryProvider(entryService, mapper);
    }

    // successful
    @Test
    public void test_findEntry() {
        DataTableRequest dataTableRequest = new DataTableRequest();
        dataTableRequest.setDraw(11);
        dataTableRequest.setStart(50);
        dataTableRequest.setLength(25);
        dataTableRequest.setSearch(null);
        dataTableRequest.setOrders(Arrays.asList(new DataTableColumnsOrder(1, "asc")));
        dataTableRequest.setColumns(Arrays.asList(
                new DataTableColumn("headerValueDate", "header.valueDate", true, true, new DataTableSearch("", false)),
                new DataTableColumn("accountBsCategory", "account.bsCategory", true, true, new DataTableSearch("", false)),
                new DataTableColumn("accountAccId", "account.accId", true, true, new DataTableSearch("", false))
        ));

        Long totalRecords = 250L;
        Long filteredRecords = 250L;
        Page pageRsl = new PageImpl(entryLines, new PageRequest(3, 25), totalRecords);
        when(entryService.findEntryLines(anyListOf(SearchCriteria.class), any(PageRequest.class))).thenReturn(pageRsl);

      пш  DataTableResponse dataTableResponse = bookEntryService.findEntry(dataTableRequest);

        Assert.assertEquals("Incorrect draw", dataTableRequest.getDraw(), dataTableResponse.getDraw());
        Assert.assertEquals("Incorrect total records", totalRecords, dataTableResponse.getRecordsTotal());
        Assert.assertEquals("Incorrect number of fitered records", filteredRecords, dataTableResponse.getRecordsFiltered());
        Assert.assertTrue("Null result data", dataTableResponse.getData() != null);
        Assert.assertTrue("No result data", !dataTableResponse.getData().isEmpty());
    }

    // rejected
    @Test(expected = IllegalArgumentException.class)
    public void test_findEntry_NullAgrument() {
        bookEntryService.findEntry(null);
    }


}
