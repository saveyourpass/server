package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.uj.saveyourpass.bo.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDao extends AbstractDao {
    public void add(User user) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        session.save(user);
        t.commit();
        session.close();
    }
}
