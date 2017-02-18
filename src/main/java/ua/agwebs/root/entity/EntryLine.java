package ua.agwebs.root.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "JRN_ENT_LN")
@IdClass(EntryLineId.class)
public class EntryLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "JRN_ENT_LN_ID")
    private Long lineId;

    @Id
    @Column(name = "JRN_ENT_HDR_ID")
    private Long headerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Id
    @JoinColumn(name = "JRN_ENT_HDR_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK__JRN_ENT_LN__JRN_ENT_HDR"))
    private EntryHeader header;

}
