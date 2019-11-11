package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dao.UserRepository;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUserId(String userId) {
        return Optional.ofNullable(userRepository.findById(userId))
                .orElseThrow(() -> new NullPointerException("사용자를 찾을 수 없습니다."));
    }

    public User findByUserIdPassword(String userId, String password) {
        User user = findByUserId(userId);
        if (!user.matchPassword(password)) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
        return user;
    }

    public void create(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(), userCreatedDto.getPassword()
                , userCreatedDto.getName(), userCreatedDto.getEmail());
        userRepository.insert(user);
    }

    public void update(String userId, User loginUser, UserUpdatedDto userUpdatedDto) {
        User user = findByUserId(userId);
        authUser(user, loginUser);
        user.update(userUpdatedDto);
        userRepository.update(user);
    }

    public User authUser(User findUser, User loginUser) {
        if (!loginUser.isSameUser(findUser)) {
            throw new IllegalStateException("서로 다른 사용자입니다.");
        }
        return findUser;
    }
}
