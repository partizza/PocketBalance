package ua.agwebs.web.rest.entries;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class SelectedFilters {

    private Long bookId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
