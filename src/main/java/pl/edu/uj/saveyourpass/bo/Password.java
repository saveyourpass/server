package pl.edu.uj.saveyourpass.bo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "passwords")
public class Password {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User owner;
    @OneToMany(mappedBy = "password", fetch = FetchType.EAGER)
    private Set<EncryptedPassword> encryptedPasswords = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<EncryptedPassword> getEncryptedPasswords() {
        return encryptedPasswords;
    }

    public void setEncryptedPasswords(Set<EncryptedPassword> encryptedPasswords) {
        this.encryptedPasswords = encryptedPasswords;
    }
}
