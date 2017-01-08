package pl.edu.uj.saveyourpass.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public abstract class AbstractDao {
    @Inject
    private SessionFactory sessionFactory;

    protected Session getSession() {
        return sessionFactory.openSession();
    }
}
