package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.dao.UserDao;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findByUserId(String userId) {
        return Optional.ofNullable(userDao.findByUserId(userId))
                .orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));
    }

    public User findByUserIdPassword(String userId, String password) {
        User user = findByUserId(userId);
        if(!user.matchPassword(password)) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
        return user;
    }

    public void create(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword()
                , userCreatedDto.getName(), userCreatedDto.getEmail());
        userDao.insert(user);
    }

    public void update(String userId, User loginUser, UserUpdatedDto userUpdatedDto) {
        User user = findByUserId(userId);
        authUser(user, loginUser);
        user.update(userUpdatedDto);
        userDao.update(user);
    }

    public User authUser(User findUser, User loginUser) {
        if (loginUser.isSameUser(findUser)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        return findUser;
    }
}
