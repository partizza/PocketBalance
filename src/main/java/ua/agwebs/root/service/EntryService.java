package ua.agwebs.root.service;


import org.springframework.validation.annotation.Validated;
import ua.agwebs.root.entity.EntryHeader;

import javax.validation.Valid;

@Validated
public interface EntryService {

    public EntryHeader createEntry(@Valid EntryHeader entryHeader);

    public void setStorno(long entryHeaderId, boolean value);

}
