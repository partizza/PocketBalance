package ua.agwebs.web.rest.balance;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BalanceSheetDTO {

    private Long bookId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    private List<String[]> data = new ArrayList<>();

    private List<ColumnDefinition> columns = new ArrayList<>();

    public BalanceSheetDTO(Long bookId, LocalDate reportDate) {
        this.bookId = bookId;
        this.reportDate = reportDate;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDefinition> columns) {
        this.columns = columns;
    }

    public void addData(String[] array){
        data.add(array);
    }
}
