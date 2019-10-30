package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.domain.User;
import slipp.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.insert(user);
    }

    public User findUser(String userId) {
        return userRepository.findById(userId);
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }
}
