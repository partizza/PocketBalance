package ua.agwebs.web.rest.entries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.agwebs.security.AppUserDetails;
import ua.agwebs.web.rest.entries.datatables.DataTableRequest;
import ua.agwebs.web.rest.entries.datatables.DataTableResponse;

@Controller
@RequestMapping(value = "/data/entry")
public class EntryController {

    private BookEntryService bookEntryService;

    @Autowired
    public EntryController(BookEntryService bookEntryService) {
        this.bookEntryService = bookEntryService;
    }

    @RequestMapping
    public ResponseEntity<DataTableResponse> getEntries(DataTableRequest dataTableRequest,
                                                        @AuthenticationPrincipal AppUserDetails appUserDetails){

        DataTableResponse response = bookEntryService.findEntry(dataTableRequest);
        return new ResponseEntity<DataTableResponse>(response, HttpStatus.OK);
    }
}
