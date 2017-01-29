package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.edu.uj.saveyourpass.bo.EncryptedPassword;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptedPasswordDao extends AbstractDao {
    public void insert(EncryptedPassword encryptedPassword) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        session.save(encryptedPassword);
        t.commit();
        session.close();
    }
}
