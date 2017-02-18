package ua.agwebs.root.entity;


import org.springframework.util.Assert;

import java.io.Serializable;

public class BalanceAccountId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;

    private Long accId;

    public BalanceAccountId(Long bookId, Long accId) {
        Assert.notNull(bookId, "Balance book Id can't be null.");
        Assert.notNull(bookId, "Balance account Id can't be null.");

        this.bookId = bookId;
        this.accId = accId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        Assert.notNull(bookId, "Balance book Id can't be null.");
        this.bookId = bookId;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        Assert.notNull(bookId, "Balance account Id can't be null.");
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
