package pl.edu.uj.saveyourpass.bo;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "auth_token")
public class AuthToken {
    private static final SecureRandom random = new SecureRandom();

    @Id
    @GeneratedValue
    @Column(name = "id")
    @JsonIgnore
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "expires")
    private LocalDateTime expires;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    public AuthToken() {}

    public AuthToken(User user) {
        this.user = user;
        token = generateToken();
        expires = LocalDateTime.now().plus(30, ChronoUnit.MINUTES);
    }

    private static String generateToken() {
        return new BigInteger(130, random).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
