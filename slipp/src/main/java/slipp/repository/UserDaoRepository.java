package slipp.repository;

import slipp.dao.UserDao;
import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@nextstep.stereotype.Repository
public class UserDaoRepository implements Repository {

    private UserDao userDao = UserDao.getInstance();

    public void insert(User user) {
        userDao.insert(user);
    }

    public Optional<User> findById(String userId) {
        return Optional.of(userDao.findByUserId(userId));
    }

    public void update(User user) {
        userDao.update(user);
    }

    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }
}
