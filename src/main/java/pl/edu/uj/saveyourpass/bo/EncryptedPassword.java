package pl.edu.uj.saveyourpass.bo;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "encrypted_passwords")
public class EncryptedPassword {
    @JsonIgnore
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Key key;
    @Column(name = "data")
    private String data;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Password password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }
}
