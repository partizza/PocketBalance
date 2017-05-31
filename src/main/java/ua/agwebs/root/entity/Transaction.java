package ua.agwebs.root.entity;

import org.hibernate.validator.constraints.NotBlank;
import ua.agwebs.root.validator.EnabledBalanceBook;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRAN")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TRAN_ID")
    @TableGenerator(name = "TranIdGen",
            table = "KEY_GEN",
            pkColumnName = "KEY_NM",
            valueColumnName = "KEY_VAL",
            pkColumnValue = "TRAN_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TranIdGen")
    private Long id;

    @NotBlank(message = "Name can't be null.")
    @Size(max = 25, message = "Length of name should be between 1 and 25 characters.")
    @Column(name = "TRAN_NM", nullable = false, length = 25)
    private String name;

    @Size(max = 60, message = "Max allowed length is 60 characters.")
    @Column(name = "TRAN_DESC", length = 60)
    private String desc;

    @NotNull
    @Column(name = "TRAN_DEL", columnDefinition = "tinyint(1) default 0", nullable = false)
    private Boolean deleted = false;

    @EnabledBalanceBook
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAL_BOOK_ID", nullable = false, foreignKey = @ForeignKey(name = "FR__TRAN__BAL_BOOK"))
    private BalanceBook book;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
    private Set<TransactionDetail> details = new HashSet<>();

    public Transaction() {
    }

    public Transaction(String name, BalanceBook book) {
        this.name = name;
        this.book = book;
    }

    public Transaction(String name, BalanceBook book, String desc) {
        this(name, book);
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public BalanceBook getBook() {
        return book;
    }

    public void setBook(BalanceBook book) {
        this.book = book;
    }

    public Set<TransactionDetail> getDetails() {
        return details;
    }

    public void addDetails(TransactionDetail detail) {
        this.details.add(detail);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", deleted=" + deleted +
                ", book=" + book +
                ", details=" + details +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (desc != null ? !desc.equals(that.desc) : that.desc != null) return false;
        return deleted != null ? deleted.equals(that.deleted) : that.deleted == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (desc != null ? desc.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        return result;
    }
}
