package ua.agwebs.web.rest.balance;


public class ColumnDefinition {

    private String title;

    private boolean isNumber;

    private boolean isVisible = true;

    public ColumnDefinition(String title, boolean isNumber) {
        this.title = title;
        this.isNumber = isNumber;
    }

    public ColumnDefinition(String title, boolean isNumber, boolean isHidden) {
        this(title, isNumber);
        this.isVisible = isHidden;
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return "ColumnDefinition{" +
                "title='" + title + '\'' +
                ", isNumber=" + isNumber +
                ", isHidden=" + isVisible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnDefinition that = (ColumnDefinition) o;

        if (isNumber != that.isNumber) return false;
        if (isVisible != that.isVisible) return false;
        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (isNumber ? 1 : 0);
        result = 31 * result + (isVisible ? 1 : 0);
        return result;
    }
}
