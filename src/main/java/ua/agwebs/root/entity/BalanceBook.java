package ua.agwebs.root.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BAL_BOOK")
public class BalanceBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BAL_BOOK_ID")
    @TableGenerator(name = "BalanceBookIdGen",
                    table = "KEY_GEN",
                    pkColumnName = "KEY_NM",
                    valueColumnName = "KEY_VAL",
                    pkColumnValue = "BAL_BOOK_ID",
                    initialValue = 1,
                    allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BalanceBookIdGen")
    private Long id;

    @Column(name = "BAL_BOOK_NM", nullable = false, length = 25)
    private String name;

    @Column(name = "BAL_BOOK_DESC", length = 60)
    private String desc;


}
