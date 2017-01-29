package pl.edu.uj.saveyourpass.bo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "keys")
public class Key {
    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "data")
    private String data;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User owner;
    @JsonIgnore
    @OneToMany(mappedBy = "key", fetch = FetchType.EAGER)
    private Set<EncryptedPassword> encryptedPasswords;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JsonProperty
    public User getOwner() {
        return owner;
    }

    @JsonIgnore
    public void setOwner(User owner) {
        this.owner = owner;
    }

    @JsonIgnore
    public Set<EncryptedPassword> getEncryptedPasswords() {
        return encryptedPasswords;
    }

    @JsonIgnore
    public void setEncryptedPasswords(Set<EncryptedPassword> passwords) {
        this.encryptedPasswords = passwords;
    }
}
