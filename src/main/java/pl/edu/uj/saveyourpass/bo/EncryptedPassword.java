package pl.edu.uj.saveyourpass.bo;

import javax.persistence.*;

@Entity
@Table(name = "encrypted_passwords")
public class EncryptedPassword {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    @Column(name = "fingerprint")
    private String fingerprint;
    @Column(name = "data")
    private String data;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Password password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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
