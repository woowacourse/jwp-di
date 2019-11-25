package slipp.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    void insert(T object);

    Optional<T> findById(long id);

    void update(T object);

    List<T> findAll() throws SQLException;
}
