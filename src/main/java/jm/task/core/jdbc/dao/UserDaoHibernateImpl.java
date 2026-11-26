package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(50), " +
                    "lastName VARCHAR(50), " +
                    "age TINYINT)";
    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String TRUNCATE_TABLE_SQL = "TRUNCATE TABLE users";

    private void executeInTransaction(Consumer<Session> action) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                action.accept(session);
                transaction.commit();
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public void createUsersTable() {
        executeInTransaction(session -> session.createNativeQuery(
                CREATE_TABLE_SQL).executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        executeInTransaction(session -> session.createNativeQuery(
                DROP_TABLE_SQL).executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeInTransaction(session -> session.persist(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        executeInTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public void cleanUsersTable() {
        executeInTransaction(session -> session.createNativeQuery(
                TRUNCATE_TABLE_SQL).executeUpdate());
    }
}

