package ua.agwebs.root.repo;


import ua.agwebs.root.entity.BSCategory;

public class BalanceLine {

    private Long bookId;
    private BSCategory bsCategory;
    private Long accId;
    private String account;
    private String currencyCode;
    private Long outstanding;

    public BalanceLine(Long bookId, BSCategory bsCategory, String account, Long accId, String currencyCode, Long outstanding) {
        this.bookId = bookId;
        this.bsCategory = bsCategory;
        this.account = account;
        this.accId = accId;
        this.currencyCode = currencyCode;
        this.outstanding = outstanding;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public BSCategory getBsCategory() {
        return bsCategory;
    }

    public void setBsCategory(BSCategory bsCategory) {
        this.bsCategory = bsCategory;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(Long outstanding) {
        this.outstanding = outstanding;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    @Override
    public String toString() {
        return "BalanceLine{" +
                "bookId=" + bookId +
                ", bsCategory=" + bsCategory +
                ", accId=" + accId +
                ", account='" + account + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", outstanding=" + outstanding +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceLine that = (BalanceLine) o;

        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        if (bsCategory != that.bsCategory) return false;
        if (accId != null ? !accId.equals(that.accId) : that.accId != null) return false;
        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null) return false;
        return outstanding != null ? outstanding.equals(that.outstanding) : that.outstanding == null;

    }

    @Override
    public int hashCode() {
        int result = bookId != null ? bookId.hashCode() : 0;
        result = 31 * result + (bsCategory != null ? bsCategory.hashCode() : 0);
        result = 31 * result + (accId != null ? accId.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (outstanding != null ? outstanding.hashCode() : 0);
        return result;
    }
}
