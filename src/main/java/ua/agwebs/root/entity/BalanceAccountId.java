package ua.agwebs.root.entity;


import org.springframework.util.Assert;

import java.io.Serializable;

public class BalanceAccountId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;

    private Long accId;

    public BalanceAccountId(){}

    public BalanceAccountId(Long bookId, Long accId) {
        this.bookId = bookId;
        this.accId = accId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    @Override
    public String toString() {
        return "BalanceAccountId{" +
                "bookId=" + bookId +
                ", accId=" + accId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceAccountId that = (BalanceAccountId) o;

        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        return accId != null ? accId.equals(that.accId) : that.accId == null;

    }

    @Override
    public int hashCode() {
        int result = bookId != null ? bookId.hashCode() : 0;
        result = 31 * result + (accId != null ? accId.hashCode() : 0);
        return result;
    }
}
