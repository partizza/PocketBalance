package ua.agwebs.root.entity;


import org.springframework.util.Assert;

import java.io.Serializable;

public class EntryLineId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long lineId;

    private Long headerId;

    public EntryLineId() {
    }

    public EntryLineId(Long lineId, Long headerId) {
        Assert.notNull(lineId, "Journal entry line Id can't be null.");
        Assert.notNull(headerId, "Journal entry header Id can't be null.");

        this.lineId = lineId;
        this.headerId = headerId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    @Override
    public String toString() {
        return "EntryLineId{" +
                "lineId=" + lineId +
                ", headerId=" + headerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntryLineId that = (EntryLineId) o;

        if (lineId != null ? !lineId.equals(that.lineId) : that.lineId != null) return false;
        return headerId != null ? headerId.equals(that.headerId) : that.headerId == null;

    }

    @Override
    public int hashCode() {
        int result = lineId != null ? lineId.hashCode() : 0;
        result = 31 * result + (headerId != null ? headerId.hashCode() : 0);
        return result;
    }
}
