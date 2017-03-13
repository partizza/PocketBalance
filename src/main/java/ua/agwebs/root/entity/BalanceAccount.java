package ua.agwebs.root.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Assert;
import ua.agwebs.root.validator.EnabledBalanceBook;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Chart of accounts
@Entity
@Table(name = "CHART_OF_ACC")
@IdClass(BalanceAccountId.class)
public class BalanceAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "COA_ID")
    private Long accId;

    @Id
    @Column(name = "BAL_BOOK_ID")
    private Long bookId;

    @EnabledBalanceBook
    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "BAL_BOOK_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK__CHART_OF_ACC__BAL_BOOK"))
    private BalanceBook book;

    @NotBlank(message = "Name can't be null.")
    @Size(max = 25, message = "Length of name should be between 1 and 25 characters.")
    @Column(name = "COA_NM")
    private String name;

    @Size(max = 60, message = "Max allowed length is 60 characters.")
    @Column(name = "COA_DESC")
    private String desc;

    @NotNull
    @Column(name = "COA_CATEG", length = 9, nullable = false)
    @Enumerated(EnumType.STRING)
    private BSCategory bsCategory;

    @NotNull
    @Column(name = "CHART_OF_ACC_ENBL", columnDefinition = "tinyint(1) default 1")
    private Boolean enable = true;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<EntryLine> entryLines = new HashSet<>();

    public BalanceAccount() {
    }

    public BalanceAccount(BSCategory bsCategory, Long accId, BalanceBook book, String name) {

        this.accId = accId;
        this.bookId = book.getId();
        this.book = book;
        this.name = name;
        this.bsCategory = bsCategory;
    }

    public BalanceAccount(BSCategory bsCategory, Long accId, BalanceBook book, String name, String desc) {

        this(bsCategory, accId, book, name);
        this.desc = desc;
    }

    public Long getAccId() {
        return accId;
    }

    public void setAccId(Long accId) {
        this.accId = accId;
    }

    public BalanceBook getBook() {
        return book;
    }

    public void setBook(BalanceBook book) {
        this.book = book;
        this.bookId = book.getId();
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

    public Set<EntryLine> getEntryLines() {
        return entryLines;
    }

    public void addEntryLines(EntryLine entryLine) {
        this.entryLines.add(entryLine);
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public BSCategory getBsCategory() {
        return bsCategory;
    }

    public void setBsCategory(BSCategory bsCategory) {
        this.bsCategory = bsCategory;
    }

    @Override
    public String toString() {
        return "BalanceAccount{" +
                "accId=" + accId +
                ", book=" + book +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", enable=" + enable +
                ", bsCategory=" + bsCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceAccount account = (BalanceAccount) o;

        if (accId != null ? !accId.equals(account.accId) : account.accId != null) return false;
        if (bookId != null ? !bookId.equals(account.bookId) : account.bookId != null) return false;
        if (name != null ? !name.equals(account.name) : account.name != null) return false;
        if (desc != null ? !desc.equals(account.desc) : account.desc != null) return false;
        if (bsCategory != account.bsCategory) return false;
        return enable != null ? enable.equals(account.enable) : account.enable == null;

    }

    @Override
    public int hashCode() {
        int result = accId != null ? accId.hashCode() : 0;
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (bsCategory != null ? bsCategory.hashCode() : 0);
        result = 31 * result + (enable != null ? enable.hashCode() : 0);
        return result;
    }
}
