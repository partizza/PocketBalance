package ua.agwebs.root.entity;


import java.io.Serializable;

public class TransactionDetailId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tranId;

    private Long coaId;

    private Long bookId;

    public TransactionDetailId() {
    }

    public TransactionDetailId(Long tranId, Long coaId, Long bookId) {
        this.tranId = tranId;
        this.coaId = coaId;
        this.bookId = bookId;
    }

    public Long getTranId() {
        return tranId;
    }

    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    public Long getCoaId() {
        return coaId;
    }

    public void setCoaId(Long coaId) {
        this.coaId = coaId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public String toString() {
        return "TransactionDetailId{" +
                "tranId=" + tranId +
                ", coaId=" + coaId +
                ", bookId=" + bookId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDetailId that = (TransactionDetailId) o;

        if (tranId != null ? !tranId.equals(that.tranId) : that.tranId != null) return false;
        if (coaId != null ? !coaId.equals(that.coaId) : that.coaId != null) return false;
        return bookId != null ? bookId.equals(that.bookId) : that.bookId == null;

    }

    @Override
    public int hashCode() {
        int result = tranId != null ? tranId.hashCode() : 0;
        result = 31 * result + (coaId != null ? coaId.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        return result;
    }
}
