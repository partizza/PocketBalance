package ua.agwebs.root.service;


import ua.agwebs.root.entity.EntryHeader;

import javax.validation.Valid;

public class EntryManger implements EntryService {

    @Override
    public EntryHeader createEntry(@Valid EntryHeader entryHeader) {
        return null;
    }

    @Override
    public void setStorno(long entryHeaderId, boolean value) {

    }
}
