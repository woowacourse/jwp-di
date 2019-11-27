package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findByUserId(String userId) {
        log.debug("begin");

        return userDao.findByUserId(userId);
    }

    public Optional<User> findByUserId2(String userId) {
        log.debug("begin");

        return Optional.ofNullable(userDao.findByUserId(userId));
    }

    public void create(UserCreatedDto userCreatedDto) {
        log.debug("begin");

        User user = userCreatedDto.toUser();
        userDao.insert(user);
    }

    public void update(String userId, UserUpdatedDto userUpdatedDto) {
        log.debug("begin");

        User user = userDao.findByUserId(userId);
        user.update(userUpdatedDto);

        userDao.update(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }
}
