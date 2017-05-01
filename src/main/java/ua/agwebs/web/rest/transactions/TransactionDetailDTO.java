package ua.agwebs.web.rest.transactions;


public class TransactionDetailDTO {

    private Long transactionId;
    private String entrySide;
    private Boolean enable;
    private Long accountAccId;
    private Long accountBookId;
    private String accountName;
    private String accountDesc;
    private String accountBsCategory;
    private Boolean accountEnable;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getEntrySide() {
        return entrySide;
    }

    public void setEntrySide(String entrySide) {
        this.entrySide = entrySide;
    }

    public Long getAccountAccId() {
        return accountAccId;
    }

    public void setAccountAccId(Long accountAccId) {
        this.accountAccId = accountAccId;
    }

    public Long getAccountBookId() {
        return accountBookId;
    }

    public void setAccountBookId(Long accountBookId) {
        this.accountBookId = accountBookId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public String getAccountBsCategory() {
        return accountBsCategory;
    }

    public void setAccountBsCategory(String accountBsCategory) {
        this.accountBsCategory = accountBsCategory;
    }

    public Boolean getAccountEnable() {
        return accountEnable;
    }

    public void setAccountEnable(Boolean accountEnable) {
        this.accountEnable = accountEnable;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "TransactionDetailDTO{" +
                "transactionId=" + transactionId +
                ", entrySide='" + entrySide + '\'' +
                ", enable=" + enable +
                ", accountAccId=" + accountAccId +
                ", accountBookId=" + accountBookId +
                ", accountName='" + accountName + '\'' +
                ", accountDesc='" + accountDesc + '\'' +
                ", accountBsCategory='" + accountBsCategory + '\'' +
                ", accountEnable=" + accountEnable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDetailDTO that = (TransactionDetailDTO) o;

        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (entrySide != null ? !entrySide.equals(that.entrySide) : that.entrySide != null) return false;
        if (enable != null ? !enable.equals(that.enable) : that.enable != null) return false;
        if (accountAccId != null ? !accountAccId.equals(that.accountAccId) : that.accountAccId != null) return false;
        if (accountBookId != null ? !accountBookId.equals(that.accountBookId) : that.accountBookId != null)
            return false;
        if (accountName != null ? !accountName.equals(that.accountName) : that.accountName != null) return false;
        if (accountDesc != null ? !accountDesc.equals(that.accountDesc) : that.accountDesc != null) return false;
        if (accountBsCategory != null ? !accountBsCategory.equals(that.accountBsCategory) : that.accountBsCategory != null)
            return false;
        return accountEnable != null ? accountEnable.equals(that.accountEnable) : that.accountEnable == null;

    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + (entrySide != null ? entrySide.hashCode() : 0);
        result = 31 * result + (enable != null ? enable.hashCode() : 0);
        result = 31 * result + (accountAccId != null ? accountAccId.hashCode() : 0);
        result = 31 * result + (accountBookId != null ? accountBookId.hashCode() : 0);
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (accountDesc != null ? accountDesc.hashCode() : 0);
        result = 31 * result + (accountBsCategory != null ? accountBsCategory.hashCode() : 0);
        result = 31 * result + (accountEnable != null ? accountEnable.hashCode() : 0);
        return result;
    }
}
