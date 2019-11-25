package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.domain.User;
import slipp.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    private UserRepository userDao;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userDao = userRepository;
    }

    public void create(User user) {
        userDao.insert(user);
    }

    public User findByUserId(String userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("유저를 찾을 수 없습니다."));
    }

    public void update(User user) {
        userDao.update(user);
    }

    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }
}
