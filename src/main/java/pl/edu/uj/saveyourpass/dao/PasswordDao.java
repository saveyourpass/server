package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.edu.uj.saveyourpass.bo.Password;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class PasswordDao extends AbstractDao {
    public void insert(Password password) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        session.save(password);
        t.commit();
        session.close();
    }

    public Optional<Password> getByName(String name) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Query<Password> query = session.createQuery("from Password where name = :name", Password.class)
                .setParameter("name", name);
        Optional<Password> password = query.uniqueResultOptional();
        t.commit();
        session.close();
        return password;
    }

    public void update(Password password) {
        Session session = getSession();
        Transaction t = session.getTransaction();
        session.update(password);
        t.commit();
        session.close();
    }
}
