package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.edu.uj.saveyourpass.bo.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserDao extends AbstractDao {
    public void add(User user) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        session.save(user);
        t.commit();
        session.close();
    }

    public Optional<User> get(String username) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Query<User> query = session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username);
        Optional<User> opt = query.uniqueResultOptional();
        t.commit();
        session.close();
        return opt;
    }

    public User getByName(String username) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Query<User> query = session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username);
        User user = query.getSingleResult();
        t.commit();
        session.close();
        return user;
    }
}
