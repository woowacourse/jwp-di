package slipp.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    void insert(T user);

    Optional<T> findById(String userId);

    void update(T user);

    List<T> findAll() throws SQLException;
}
