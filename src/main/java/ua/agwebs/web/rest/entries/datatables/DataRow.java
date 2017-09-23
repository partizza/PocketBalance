package ua.agwebs.web.rest.entries.datatables;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class DataRow {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate headerValueDate;

    private String accountBsCategory;
    private Long accountAccId;
    private String accountName;
    private String headerDesc;
    private Long currencyId;
    private String currencyCode;
    private Long trnAmount;

    public LocalDate getHeaderValueDate() {
        return headerValueDate;
    }

    public void setHeaderValueDate(LocalDate headerValueDate) {
        this.headerValueDate = headerValueDate;
    }

    public String getAccountBsCategory() {
        return accountBsCategory;
    }

    public void setAccountBsCategory(String accountBsCategory) {
        this.accountBsCategory = accountBsCategory;
    }

    public Long getAccountAccId() {
        return accountAccId;
    }

    public void setAccountAccId(Long accountAccId) {
        this.accountAccId = accountAccId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getHeaderDesc() {
        return headerDesc;
    }

    public void setHeaderDesc(String headerDesc) {
        this.headerDesc = headerDesc;
    }

    public Long getTrnAmount() {
        return trnAmount;
    }

    public void setTrnAmount(Long trnAmount) {
        this.trnAmount = trnAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataRow dataRow = (DataRow) o;

        if (headerValueDate != null ? !headerValueDate.equals(dataRow.headerValueDate) : dataRow.headerValueDate != null)
            return false;
        if (accountBsCategory != null ? !accountBsCategory.equals(dataRow.accountBsCategory) : dataRow.accountBsCategory != null)
            return false;
        if (accountAccId != null ? !accountAccId.equals(dataRow.accountAccId) : dataRow.accountAccId != null)
            return false;
        if (accountName != null ? !accountName.equals(dataRow.accountName) : dataRow.accountName != null) return false;
        if (headerDesc != null ? !headerDesc.equals(dataRow.headerDesc) : dataRow.headerDesc != null) return false;
        if (currencyId != null ? !currencyId.equals(dataRow.currencyId) : dataRow.currencyId != null) return false;
        if (currencyCode != null ? !currencyCode.equals(dataRow.currencyCode) : dataRow.currencyCode != null)
            return false;
        return trnAmount != null ? trnAmount.equals(dataRow.trnAmount) : dataRow.trnAmount == null;

    }

    @Override
    public int hashCode() {
        int result = headerValueDate != null ? headerValueDate.hashCode() : 0;
        result = 31 * result + (accountBsCategory != null ? accountBsCategory.hashCode() : 0);
        result = 31 * result + (accountAccId != null ? accountAccId.hashCode() : 0);
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (headerDesc != null ? headerDesc.hashCode() : 0);
        result = 31 * result + (currencyId != null ? currencyId.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (trnAmount != null ? trnAmount.hashCode() : 0);
        return result;
    }
}
