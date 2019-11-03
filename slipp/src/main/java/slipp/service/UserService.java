package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User create(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(),
                userCreatedDto.getPassword(),
                userCreatedDto.getName(),
                userCreatedDto.getEmail());

        userDao.insert(user);
        return user;
    }

    public User update(String userId, UserUpdatedDto userUpdatedDto) {
        User user = userDao.findByUserId(userId);
        user.update(userUpdatedDto);

        return user;
    }

    public User findById(String userId) {
        return userDao.findByUserId(userId);
    }

    public List<User> findAll() throws SQLException {
        return userDao.findAll();
    }
}
