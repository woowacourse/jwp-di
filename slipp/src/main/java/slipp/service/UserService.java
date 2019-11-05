package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;
import slipp.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    @Inject
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User insert(final UserCreatedDto dto) {
        final User user = new User(dto.getUserId(), dto.getPassword(), dto.getName(), dto.getEmail());
        userRepository.insert(user);
        return user;
    }

    public User update(final String userId, final UserUpdatedDto dto) {
        final User user = userRepository.findByUserId(userId);
        user.update(dto);
        userRepository.update(user);
        return user;
    }

    public User findByUserId(final String userId) {
        return userRepository.findByUserId(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
