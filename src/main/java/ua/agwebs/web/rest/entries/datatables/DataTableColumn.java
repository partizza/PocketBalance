package ua.agwebs.web.rest.entries.datatables;


public class DataTableColumn {

    private String data;
    private String name;
    private Boolean searchable = false;
    private Boolean orderable = false;
    private DataTableSearch search;

    public DataTableColumn() {
    }

    public DataTableColumn(String data, String name, Boolean searchable, Boolean orderable, DataTableSearch search) {
        this.data = data;
        this.name = name;
        this.searchable = searchable;
        this.orderable = orderable;
        this.search = search;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    public Boolean getOrderable() {
        return orderable;
    }

    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    public DataTableSearch getSearch() {
        return search;
    }

    public void setSearch(DataTableSearch search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return "DataTableColumn{" +
                "data='" + data + '\'' +
                ", name='" + name + '\'' +
                ", searchable=" + searchable +
                ", orderable=" + orderable +
                ", search=" + search +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataTableColumn that = (DataTableColumn) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (searchable != null ? !searchable.equals(that.searchable) : that.searchable != null) return false;
        if (orderable != null ? !orderable.equals(that.orderable) : that.orderable != null) return false;
        return search != null ? search.equals(that.search) : that.search == null;

    }

    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (searchable != null ? searchable.hashCode() : 0);
        result = 31 * result + (orderable != null ? orderable.hashCode() : 0);
        result = 31 * result + (search != null ? search.hashCode() : 0);
        return result;
    }
}
