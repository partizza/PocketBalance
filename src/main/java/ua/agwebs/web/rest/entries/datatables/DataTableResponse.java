package ua.agwebs.web.rest.entries.datatables;


import java.util.List;

public class DataTableResponse {

    private Integer draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<DataRow> data;
    private String error;

    public DataTableResponse(int draw){
        this.draw = draw;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<DataRow> getData() {
        return data;
    }

    public void setData(List<DataRow> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
