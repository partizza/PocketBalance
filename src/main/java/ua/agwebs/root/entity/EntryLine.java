package ua.agwebs.root.entity;

import org.hibernate.annotations.Check;
import ua.agwebs.root.validator.EnableBalanceAccount;
import ua.agwebs.root.validator.EntryLineAmountSide;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "JRN_ENT_LN")
@IdClass(EntryLineId.class)
@EntryLineAmountSide
public class EntryLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Min(1)
    @Column(name = "JRN_ENT_LN_ID")
    private Long lineId;

    @Id
    @Column(name = "JRN_ENT_HDR_ID")
    private Long headerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "JRN_ENT_HDR_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK__JRN_ENT_LN__JRN_ENT_HDR"))
    private EntryHeader header;

    @NotNull
    @EnableBalanceAccount
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "COA_ID"),
            @JoinColumn(name = "BAL_BOOK_ID")
    }, foreignKey = @ForeignKey(name = "FK__JRN_ENT_LN__CHART_OF_ACC"))
    private BalanceAccount account;

    @NotNull
    @Column(name = "JRN_ENT_LN_TRN_AMT", nullable = false)
    private Long trnAmount;

    @NotNull
    @Column(name = "JRN_ENT_LN_SIDE", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private EntrySide side;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURY_ID", foreignKey = @ForeignKey(name = "FK__JRN_ENT_LN__CURY"))
    private Currency currency;

    public EntryLine() {
    }

    public EntryLine(long lineId, BalanceAccount account, long trnAmount, EntrySide side, Currency currency) {
        this.lineId = lineId;
        this.account = account;
        this.trnAmount = trnAmount;
        this.side = side;
        this.currency = currency;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public EntryHeader getHeader() {
        return header;
    }

    public void setHeader(EntryHeader header) {
        this.header = header;
        this.headerId = header.getId();
    }

    public BalanceAccount getAccount() {
        return account;
    }

    public void setAccount(BalanceAccount account) {
        this.account = account;
    }

    public Long getTrnAmount() {
        return trnAmount;
    }

    public void setTrnAmount(long trnAmount) {
        this.trnAmount = trnAmount;
    }

    public EntrySide getType() {
        return side;
    }

    public void setType(EntrySide type) {
        this.side = side;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "EntryLine{" +
                "lineId=" + lineId +
                ", header=" + header +
                ", account=" + account +
                ", trnAmount=" + trnAmount +
                ", type='" + side + '\'' +
                ", currency=" + currency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryLine entryLine = (EntryLine) o;

        if (lineId != null ? !lineId.equals(entryLine.lineId) : entryLine.lineId != null) return false;
        if (headerId != null ? !headerId.equals(entryLine.headerId) : entryLine.headerId != null) return false;
        if (header != null ? !header.equals(entryLine.header) : entryLine.header != null) return false;
        if (account != null ? !account.equals(entryLine.account) : entryLine.account != null) return false;
        if (trnAmount != null ? !trnAmount.equals(entryLine.trnAmount) : entryLine.trnAmount != null) return false;
        if (side != entryLine.side) return false;
        return currency != null ? currency.equals(entryLine.currency) : entryLine.currency == null;

    }

    @Override
    public int hashCode() {
        int result = lineId != null ? lineId.hashCode() : 0;
        result = 31 * result + (headerId != null ? headerId.hashCode() : 0);
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (trnAmount != null ? trnAmount.hashCode() : 0);
        result = 31 * result + (side != null ? side.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
