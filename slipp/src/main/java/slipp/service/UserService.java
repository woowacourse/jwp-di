package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import slipp.domain.User;
import slipp.repository.Repository;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    private Repository userRepository;

    @Inject
    public UserService(Repository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(User user) {
        userRepository.insert(user);
    }

    public User findByUserId(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("유저를 찾을 수 없습니다."));
    }

    public void update(User user) {
        userRepository.update(user);
    }

    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }
}
