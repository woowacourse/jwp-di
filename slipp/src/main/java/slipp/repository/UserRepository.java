package slipp.repository;

import slipp.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User> {
    Optional<User> findByUserId(String id);

    @Override
    void insert(User object);

    @Override
    Optional<User> findById(long id);

    @Override
    void update(User object);

    @Override
    List<User> findAll() throws SQLException;
}
