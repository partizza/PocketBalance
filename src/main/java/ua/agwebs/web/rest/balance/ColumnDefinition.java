package ua.agwebs.web.rest.balance;


public class ColumnDefinition {

    private String title;

    private boolean isNumber;

    public ColumnDefinition(String title, boolean isNumber) {
        this.title = title;
        this.isNumber = isNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public void setNumber(boolean number) {
        isNumber = number;
    }

    @Override
    public String toString() {
        return "ColumnDefinition{" +
                "title='" + title + '\'' +
                ", isNumber=" + isNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnDefinition that = (ColumnDefinition) o;

        if (isNumber != that.isNumber) return false;
        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (isNumber ? 1 : 0);
        return result;
    }
}
