package ua.agwebs.root.entity;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "BAL_BOOK_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK__CHART_OF_ACC__BAL_BOOK"))
    private BalanceBook book;


    @Column(name = "COA_NM", nullable = false, length = 25)
    private String name;

    @Column(name = "COA_DESC", length = 60)
    private String desc;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private Set<EntryLine> entryLines = new HashSet<>();

    public BalanceAccount() {
    }

    public BalanceAccount(Long accId, BalanceBook book) {
        this.accId = accId;
        this.book = book;
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

    @Override
    public String toString() {
        return "BalanceAccount{" +
                "accId=" + accId +
                ", bookId=" + bookId +
                ", book=" + book +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BalanceAccount that = (BalanceAccount) o;

        if (accId != null ? !accId.equals(that.accId) : that.accId != null) return false;
        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        if (book != null ? !book.equals(that.book) : that.book != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return desc != null ? desc.equals(that.desc) : that.desc == null;

    }

    @Override
    public int hashCode() {
        int result = accId != null ? accId.hashCode() : 0;
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        return result;
    }
}
