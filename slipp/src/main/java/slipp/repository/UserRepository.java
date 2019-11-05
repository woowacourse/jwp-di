package slipp.repository;

import slipp.domain.User;

import java.util.List;

public interface UserRepository {
    void insert(final User user);

    void update(final User user);

    User findByUserId(final String userId);

    List<User> findAll();
}
