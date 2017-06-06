package ua.agwebs.web.rest.transactions;


public class AccountDTO {

    private Long accId;
    private Long bookId;
    private String name;
    private String desc;
    private String bsCategory;

    public AccountDTO() {
    }

    public AccountDTO(Long accId, Long bookId, String name, String desc, String bsCategory) {
        this.accId = accId;
        this.bookId = bookId;
        this.name = name;
        this.desc = desc;
        this.bsCategory = bsCategory;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

    public String getBsCategory() {
        return bsCategory;
    }

    public void setBsCategory(String bsCategory) {
        this.bsCategory = bsCategory;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accId=" + accId +
                ", bookId=" + bookId +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", bsCategory='" + bsCategory + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDTO that = (AccountDTO) o;

        if (accId != null ? !accId.equals(that.accId) : that.accId != null) return false;
        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        return bsCategory != null ? bsCategory.equals(that.bsCategory) : that.bsCategory == null;

    }

    @Override
    public int hashCode() {
        int result = accId != null ? accId.hashCode() : 0;
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (bsCategory != null ? bsCategory.hashCode() : 0);
        return result;
    }
}
