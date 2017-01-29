package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import pl.edu.uj.saveyourpass.bo.Key;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class KeyDao extends AbstractDao {
    public void insert(Key key) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        session.save(key);
        t.commit();
    }

    public Optional<Key> getByName(String name) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Query<Key> query = session.createQuery("from Key where name = :name", Key.class)
                .setParameter("name", name);
        Optional<Key> key = query.uniqueResultOptional();
        t.commit();
        session.close();
        return key;
    }
//    public Set<Key> getForUser(Object user) {
//        Session session = getSession();
//        Transaction t = session.beginTransaction();
//        Query<Key> query = session.createQuery("from Key where ")
//    }
}
