package ua.agwebs.root.entity;

import org.hibernate.validator.constraints.NotBlank;
import ua.agwebs.root.validator.EnabledBalanceBook;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

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
}
