package slipp.repository;

import nextstep.jdbc.ConnectionManager;
import nextstep.jdbc.JdbcTemplate;
import nextstep.stereotype.Repository;
import slipp.dao.UserDao;
import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoRepository implements UserRepository {

    private UserDao userDao = new UserDao(new JdbcTemplate(ConnectionManager.getDataSource()));

    public void insert(User user) {
        userDao.insert(user);
    }

    public User findById(String userId) {
        return userDao.findByUserId(userId);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }
}
