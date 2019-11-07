package slipp.dao;

import slipp.domain.User;

import java.util.List;

public interface UserDao {
    void insert(User user);

    User findByUserId(String userId);

    void update(User user);

    List<User> findAll();
}
