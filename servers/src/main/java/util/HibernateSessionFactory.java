package util;

import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;

    private HibernateSessionFactory() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration cfg = new Configuration().configure();
            cfg.addAnnotatedClass(User.class);
            sessionFactory = cfg.buildSessionFactory();
        }

        return sessionFactory;
    }
}
