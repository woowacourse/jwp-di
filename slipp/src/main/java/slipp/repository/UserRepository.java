package slipp.repository;

import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    void insert(User user);

    User findById(String userId);

    void update(User user);

    List<User> findAll() throws SQLException;
}
