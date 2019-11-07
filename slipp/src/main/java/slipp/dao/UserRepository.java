package slipp.dao;

import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    void insert(User user);

    User findByUserId(String userId);

    List<User> findAll() throws SQLException;

    void update(User user);
}