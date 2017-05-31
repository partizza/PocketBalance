package ua.agwebs.web.rest.transactions;


import java.util.ArrayList;
import java.util.List;

public class TransactionDTO {

    private Long id;

    private String name;

    private String desc;

    private Long bookId;

    private List<TransactionDetailDTO> details = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public List<TransactionDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<TransactionDetailDTO> details) {
        this.details = details;
    }

    public void addDetails(TransactionDetailDTO detail) {
        this.details.add(detail);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", bookId=" + bookId +
                ", details=" + details +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDTO that = (TransactionDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        return details != null ? details.equals(that.details) : that.details == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }
}
