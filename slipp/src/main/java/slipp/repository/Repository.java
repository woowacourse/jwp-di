package slipp.repository;

import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository {

    void insert(User user);

    Optional<User> findById(String userId);

    void update(User user);

    List<User> findAll() throws SQLException;
}
