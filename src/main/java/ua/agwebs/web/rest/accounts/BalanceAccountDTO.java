package ua.agwebs.web.rest.accounts;


public class BalanceAccountDTO {

    private Long accId;
    private Long bookId;
    private String name;
    private String desc;
    private String bsCategory;
    private Boolean enable;

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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
