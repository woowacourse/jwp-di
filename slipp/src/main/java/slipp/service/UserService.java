package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.dao.UserDao;
import slipp.domain.User;

@Service
public class UserService {
    private UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void signUp(User user) {
        userDao.insert(user);
    }
}
