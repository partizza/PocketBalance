package ua.agwebs.root.entity;

import org.hibernate.validator.constraints.NotEmpty;
import ua.agwebs.root.validator.EnabledBalanceBook;
import ua.agwebs.root.validator.EntryAmountBalancing;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JRN_ENT_HDR")
public class EntryHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "JRN_ENT_HDR_ID")
    @TableGenerator(name = "JournalEntryHeaderIdGen",
            table = "KEY_GEN",
            pkColumnName = "KEY_NM",
            valueColumnName = "KEY_VAL",
            pkColumnValue = "JRN_ENT_HDR_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "JournalEntryHeaderIdGen")
    private Long id;

    @Size(max = 60, message = "Description of entry could be not more than 60 characters.")
    @Column(name = "JRN_ENT_HDR_DESC", length = 60)
    private String desc;

    @NotNull
    @Column(name = "JRN_ENT_HDR_POST_DTTM", nullable = false)
    private LocalDateTime postedTime = LocalDateTime.now();

    @NotNull
    @Column(name = "JRN_ENT_HDR_VAL_DT", nullable = false)
    private LocalDate valueDate = LocalDate.now();

    @NotNull
    @Column(name = "JRN_ENT_HDR_STORNO", columnDefinition = "tinyint(1) default 0", nullable = false)
    private Boolean storno = false;

    @NotNull
    @EnabledBalanceBook
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAL_BOOK_ID", foreignKey = @ForeignKey(name = "FK__JRN_ENT_HDR__BAL_BOOK"), nullable = false)
    private BalanceBook book;

    @NotEmpty
    @EntryAmountBalancing
    @Valid
    @OneToMany(mappedBy = "header", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EntryLine> lines = new HashSet<>();

    public EntryHeader() {
    }

    public EntryHeader(BalanceBook book) {
        this.book = book;
    }

    public EntryHeader(BalanceBook book, String desc) {
        this(book);
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LocalDateTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(LocalDateTime postedTime) {
        this.postedTime = postedTime;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public BalanceBook getBook() {
        return book;
    }

    public void setBook(BalanceBook book) {
        this.book = book;
    }

    public Boolean isStorno() {
        return storno;
    }

    public void setStorno(Boolean storno) {
        this.storno = storno;
    }

    public Set<EntryLine> getLines() {
        return lines;
    }

    public void addLines(EntryLine line) {
        this.lines.add(line);
    }

    @Override
    public String toString() {
        return "EntryHeader{" +
                "id=" + id +
                ", desc='" + desc + '\'' +
                ", postedTime=" + postedTime +
                ", valueDate=" + valueDate +
                ", storno=" + storno +
                ", book=" + book +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryHeader that = (EntryHeader) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        if (postedTime != null ? !postedTime.equals(that.postedTime) : that.postedTime != null) return false;
        if (valueDate != null ? !valueDate.equals(that.valueDate) : that.valueDate != null) return false;
        if (storno != null ? !storno.equals(that.storno) : that.storno != null) return false;
        return book != null ? book.equals(that.book) : that.book == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (postedTime != null ? postedTime.hashCode() : 0);
        result = 31 * result + (valueDate != null ? valueDate.hashCode() : 0);
        result = 31 * result + (storno != null ? storno.hashCode() : 0);
        result = 31 * result + (book != null ? book.hashCode() : 0);
        return result;
    }
}
