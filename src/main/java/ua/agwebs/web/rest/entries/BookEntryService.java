package ua.agwebs.web.rest.entries;

import ua.agwebs.web.rest.entries.datatables.DataTableRequest;
import ua.agwebs.web.rest.entries.datatables.DataTableResponse;

public interface BookEntryService {

    public DataTableResponse findEntry(DataTableRequest dataTableRequest);

}
