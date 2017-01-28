package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.edu.uj.saveyourpass.bo.AuthToken;
import pl.edu.uj.saveyourpass.bo.EncryptedPassword;
import pl.edu.uj.saveyourpass.bo.Password;
import pl.edu.uj.saveyourpass.bo.User;

public abstract class AbstractDao {
    private SessionFactory sessionFactory;

    public AbstractDao() {
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AuthToken.class)
                .addAnnotatedClass(Password.class)
                .addAnnotatedClass(EncryptedPassword.class)
                .buildSessionFactory();
    }

    protected Session getSession() {
        return sessionFactory.openSession();
    }
}
