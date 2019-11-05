package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.dao.UserDao;
import slipp.domain.User;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findByUserId(String userId) {
        return userDao.findByUserId(userId);
    }

    public void insert(User user) {
        userDao.insert(user);
    }

    public void update(User user) {
        userDao.update(user);
    }
}
