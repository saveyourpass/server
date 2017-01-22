package pl.edu.uj.saveyourpass.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.edu.uj.saveyourpass.bo.AuthToken;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class AuthTokenDao extends AbstractDao {
    public void add(AuthToken authToken) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        session.save(authToken);
        t.commit();
        session.close();
    }

    public boolean isValid(String token) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Query<AuthToken> query = session.createQuery("from AuthToken where token = :token", AuthToken.class)
                .setParameter("token", token);
        Optional<AuthToken> authToken = query.uniqueResultOptional();
        t.commit();
        session.close();
        if (authToken.isPresent() && !hasExpired(authToken.get())) {
            return true;
        }
        return false;
    }

    public boolean hasExpired(AuthToken authToken) {
        return authToken.getExpires().isBefore(LocalDateTime.now());
    }
}
