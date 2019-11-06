package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.stereotype.Repository;
import slipp.domain.User;
import slipp.repository.UserRepository;

import java.util.List;

@Repository
public class UserDao implements UserRepository {
    private static final RowMapper<User> USER_MAPPER = resultSet -> new User(
            resultSet.getString("userId"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("email"));

    private final JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    public void insert(final User user) {
        final String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public void update(final User user) {
        final String sql = "UPDATE USERS set password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }

    public User findByUserId(final String userId) {
        final String sql = "SELECT userId, password, name, email FROM USERS WHERE userId = ?";
        return jdbcTemplate.queryForObject(sql, USER_MAPPER, userId);
    }

    public List<User> findAll() {
        final String sql = "SELECT userId, password, name, email FROM USERS";
        return jdbcTemplate.query(sql, USER_MAPPER);
    }
}
