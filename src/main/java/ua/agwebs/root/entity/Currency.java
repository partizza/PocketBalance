package ua.agwebs.root.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CURY")
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CURY_ID")
    private Long id;

    @NotBlank
    @Size(max = 3, message = "Max allowed length is 3 characters.")
    @Column(name = "CURY_CD", nullable = false, unique = true, length = 3)
    private String code;

    @NotBlank
    @Size(max = 60, message = "Max allowed length is 60 characters.")
    @Column(name = "CURY_NM", nullable = false, length = 60)
    private String name;

    @OneToMany(mappedBy = "currency")
    private Set<EntryLine> entryLines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EntryLine> getEntryLines() {
        return entryLines;
    }

    public void addEntryLines(EntryLine entryLines) {
        this.entryLines.add(entryLines);
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        if (id != null ? !id.equals(currency.id) : currency.id != null) return false;
        if (code != null ? !code.equals(currency.code) : currency.code != null) return false;
        return name != null ? name.equals(currency.name) : currency.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
