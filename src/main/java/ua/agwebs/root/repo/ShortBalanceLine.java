package ua.agwebs.root.repo;


import ua.agwebs.root.entity.BSCategory;

public class ShortBalanceLine {

    private Long bookId;
    private BSCategory bsCategory;
    private String currencyCode;
    private Long outstanding;

    public ShortBalanceLine(Long bookId, BSCategory bsCategory, String currencyCode, Long outstanding) {
        this.bookId = bookId;
        this.bsCategory = bsCategory;
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

    @Override
    public String toString() {
        return "ShortBalanceLine{" +
                "bookId=" + bookId +
                ", bsCategory=" + bsCategory +
                ", currencyCode='" + currencyCode + '\'' +
                ", outstanding=" + outstanding +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortBalanceLine that = (ShortBalanceLine) o;

        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        if (bsCategory != that.bsCategory) return false;
        if (currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null) return false;
        return outstanding != null ? outstanding.equals(that.outstanding) : that.outstanding == null;

    }

    @Override
    public int hashCode() {
        int result = bookId != null ? bookId.hashCode() : 0;
        result = 31 * result + (bsCategory != null ? bsCategory.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (outstanding != null ? outstanding.hashCode() : 0);
        return result;
    }
}
