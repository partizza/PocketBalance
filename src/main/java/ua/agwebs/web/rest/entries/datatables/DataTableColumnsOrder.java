package ua.agwebs.web.rest.entries.datatables;


public class DataTableColumnsOrder {

    private Integer column;
    private String dir;

    public DataTableColumnsOrder() {
    }

    public DataTableColumnsOrder(Integer column, String dir) {
        this.column = column;
        this.dir = dir;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "DataTableColumnsOrder{" +
                "column=" + column +
                ", dir='" + dir + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataTableColumnsOrder that = (DataTableColumnsOrder) o;

        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        return dir != null ? dir.equals(that.dir) : that.dir == null;

    }

    @Override
    public int hashCode() {
        int result = column != null ? column.hashCode() : 0;
        result = 31 * result + (dir != null ? dir.hashCode() : 0);
        return result;
    }
}
