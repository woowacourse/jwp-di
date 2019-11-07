package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.dao.UserRepository;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(UserCreatedDto userCreatedDto) {
        User user = new User(userCreatedDto.getUserId(),
                userCreatedDto.getPassword(),
                userCreatedDto.getName(),
                userCreatedDto.getEmail());

        userRepository.insert(user);
        return user;
    }

    public User update(String userId, UserUpdatedDto userUpdatedDto) {
        User user = userRepository.findByUserId(userId);
        user.update(userUpdatedDto);

        return user;
    }

    public User findById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }
}
