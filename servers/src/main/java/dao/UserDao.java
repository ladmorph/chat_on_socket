package dao;

import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateSessionFactory;

import java.util.List;

public class UserDao {

    public void save(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.save(user);
    }

    public void update(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.update(user);
    }

    public void delete(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.delete(user);
    }

    public int getMessages(String username) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT messages FROM users WHERE username = '" + username + "'");

        return (int) query.getSingleResult();
    }

    public void saveOrUpdate(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.saveOrUpdate(user);
    }

    public void updateByUsername(User user) {

        int count = user.getMessages();
        String date = user.getDate();
        String username = user.getUsername();

        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createSQLQuery
                ("UPDATE users SET messages = " + count + ", time ='" + date + "' WHERE  username = '" + username + "'");
        query.executeUpdate();

        tx.commit();
    }

    public void updateMessagesByUsername(String username, int messages) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createSQLQuery
                ("UPDATE users SET messages = " + messages + " WHERE username = '" + username + "'");
        query.executeUpdate();
        tx.commit();
    }

    public List<User> getAllUsers() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Query query = session.createSQLQuery("SELECT * FROM users").addEntity(User.class);

        return query.list();
    }
}
