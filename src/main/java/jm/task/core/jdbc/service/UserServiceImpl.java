package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
//import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
//    private final UserDao userDao = new UserDaoJDBCImpl();
    private final UserDao userDao = new UserDaoHibernateImpl();

    public void createUsersTable() {
        userDao.createUsersTable();
    }

    public void dropUsersTable() {
        userDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Фамилия не может быть пустой");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Имя не может быть длиннее 50 символов");
        }
        if (lastName.length() > 50) {
            throw new IllegalArgumentException("Фамилия не может быть длиннее 50 символов");
        }
        if (age < 1 || age > 120) {
            throw new IllegalArgumentException("Возраст должен быть от 1 до 120");
        }
        userDao.saveUser(name, lastName, age);
    }

    public void removeUserById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным");
        }
        userDao.removeUserById(id);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void cleanUsersTable() {
        userDao.cleanUsersTable();
    }
}
