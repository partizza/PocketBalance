package ua.agwebs.web.rest.entries.datatables;


public class DataTableSearch {

    private String value;
    private Boolean regex;

    public DataTableSearch() {
    }

    public DataTableSearch(String value, Boolean regex) {
        this.value = value;
        this.regex = regex;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getRegex() {
        return regex;
    }

    public void setRegex(Boolean regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        return "DataTableSearch{" +
                "value='" + value + '\'' +
                ", regex=" + regex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataTableSearch that = (DataTableSearch) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return regex != null ? regex.equals(that.regex) : that.regex == null;

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (regex != null ? regex.hashCode() : 0);
        return result;
    }
}
