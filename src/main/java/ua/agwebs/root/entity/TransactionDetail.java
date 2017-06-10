package ua.agwebs.root.entity;

import ua.agwebs.root.validator.EnableBalanceAccount;
import ua.agwebs.root.validator.EnabledTransaction;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "TRAN_DET")
@IdClass(TransactionDetailId.class)
public class TransactionDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TRAN_ID")
    private Long tranId;

//    @Valid
    @EnabledTransaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAN_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK__TRAN_DET__TRAN"))
    private Transaction transaction;

    @Id
    @Column(name = "COA_ID")
    private Long coaId;

    @Id
    @Column(name = "BAL_BOOK_ID")
    private Long bookId;

//    @Valid
    @EnableBalanceAccount
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {@JoinColumn(name = "COA_ID", insertable = false, updatable = false),
            @JoinColumn(name = "BAL_BOOK_ID", insertable = false, updatable = false)},
            foreignKey = @ForeignKey(name = "FK__TRAN_DET__CHART_OF_ACC"))
    private BalanceAccount account;

    @NotNull
    @Column(name = "TRAN_DET_SIDE", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private EntrySide entrySide;

    @NotNull
    @Column(name = "TRAN_DET_ENBL", nullable = false, columnDefinition = "tinyint(1) default 1")
    private Boolean enable = true;

    public TransactionDetail() {
    }

    public TransactionDetail(Transaction transaction, BalanceAccount account, EntrySide side) {
        this.transaction = transaction;
        this.tranId = transaction.getId();

        this.account = account;
        this.coaId = account.getAccId();
        this.bookId = account.getBook().getId();

        this.entrySide = side;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        this.tranId = transaction.getId();
    }

    public BalanceAccount getAccount() {
        return account;
    }

    public void setAccount(BalanceAccount account) {
        this.account = account;
        this.coaId = account.getAccId();
        this.bookId = account.getBook().getId();
    }

    public EntrySide getEntrySide() {
        return entrySide;
    }

    public void setEntrySide(EntrySide entrySide) {
        this.entrySide = entrySide;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Long getTranId() {
        return tranId;
    }

    public Long getCoaId() {
        return coaId;
    }

    public Long getBookId() {
        return bookId;
    }

    @Override
    public String toString() {
        return "TransactionDetail{" +
                "tranId=" + tranId +
                ", coaId=" + coaId +
                ", bookId=" + bookId +
                ", entrySide=" + entrySide +
                ", enable=" + enable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionDetail that = (TransactionDetail) o;

        if (tranId != null ? !tranId.equals(that.tranId) : that.tranId != null) return false;
        if (coaId != null ? !coaId.equals(that.coaId) : that.coaId != null) return false;
        if (bookId != null ? !bookId.equals(that.bookId) : that.bookId != null) return false;
        if (entrySide != that.entrySide) return false;
        return enable != null ? enable.equals(that.enable) : that.enable == null;

    }

    @Override
    public int hashCode() {
        int result = tranId != null ? tranId.hashCode() : 0;
        result = 31 * result + (coaId != null ? coaId.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (entrySide != null ? entrySide.hashCode() : 0);
        result = 31 * result + (enable != null ? enable.hashCode() : 0);
        return result;
    }
}
