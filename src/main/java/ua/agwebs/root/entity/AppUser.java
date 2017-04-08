package ua.agwebs.root.entity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "APP_USER")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "APP_USER_ID")
    @TableGenerator(name = "AppUserIdGen",
            table = "KEY_GEN",
            pkColumnName = "KEY_NM",
            valueColumnName = "KEY_VAL",
            pkColumnValue = "APP_USER_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AppUserIdGen")
    private Long id;

    @NotBlank
    @Email
    @Column(name = "EML", nullable = false, length = 60, unique = true)
    private String email;

    @Column(name = "APP_USER_NM", length = 25)
    private String name = "";

    @Column(name = "APP_USER_SNM", length = 35)
    private String surname = "";

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY)
    private Set<BalanceBook> books = new HashSet<>();

    public AppUser() {
    }

    public AppUser(String email) {
        this.email = email;
    }

    public AppUser(String email, String name) {
        this(email);
        this.name = name;
    }

    public AppUser(String email, String name, String surname) {
        this(email, name);
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<BalanceBook> getBooks() {
        return books;
    }

    public void addBook(BalanceBook book) {
        this.books.add(book);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppUser appUser = (AppUser) o;

        if (id != null ? !id.equals(appUser.id) : appUser.id != null) return false;
        if (email != null ? !email.equals(appUser.email) : appUser.email != null) return false;
        if (name != null ? !name.equals(appUser.name) : appUser.name != null) return false;
        return surname != null ? surname.equals(appUser.surname) : appUser.surname == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }
}
