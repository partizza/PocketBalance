package ua.agwebs.web.rest.entries.datatables;


import ua.agwebs.root.service.specifications.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class DataTableRequest {

    private Integer draw;
    private Integer start;
    private Integer length;
    private DataTableSearch search;
    private List<DataTableColumnsOrder> orders = new ArrayList<>();
    private List<DataTableColumn> columns = new ArrayList<>();
    private List<SearchCriteria> filters = new ArrayList<>();
    private Long bookId;

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public DataTableSearch getSearch() {
        return search;
    }

    public void setSearch(DataTableSearch search) {
        this.search = search;
    }

    public List<DataTableColumnsOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DataTableColumnsOrder> orders) {
        this.orders = orders;
    }

    public List<DataTableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataTableColumn> columns) {
        this.columns = columns;
    }

    public List<SearchCriteria> getFilters() {
        return filters;
    }

    public void setFilters(List<SearchCriteria> filters) {
        this.filters = filters;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "DataTableRequest{" +
                "draw=" + draw +
                ", start=" + start +
                ", length=" + length +
                ", search=" + search +
                ", orders=" + orders +
                ", columns=" + columns +
                ", filters=" + filters +
                ", bookId=" + bookId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataTableRequest that = (DataTableRequest) o;

        if (draw != null ? !draw.equals(that.draw) : that.draw != null) return false;
        if (start != null ? !start.equals(that.start) : that.start != null) return false;
        if (length != null ? !length.equals(that.length) : that.length != null) return false;
        if (search != null ? !search.equals(that.search) : that.search != null) return false;
        if (orders != null ? !orders.equals(that.orders) : that.orders != null) return false;
        if (columns != null ? !columns.equals(that.columns) : that.columns != null) return false;
        if (filters != null ? !filters.equals(that.filters) : that.filters != null) return false;
        return bookId != null ? bookId.equals(that.bookId) : that.bookId == null;
    }

    @Override
    public int hashCode() {
        int result = draw != null ? draw.hashCode() : 0;
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (length != null ? length.hashCode() : 0);
        result = 31 * result + (search != null ? search.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        result = 31 * result + (filters != null ? filters.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        return result;
    }
}
