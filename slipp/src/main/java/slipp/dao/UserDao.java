package slipp.dao;

import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.stereotype.Repository;
import slipp.domain.User;

import java.util.List;

@Repository
public class UserDao implements UserRepository {
    private static final UserDao userDao = new UserDao();
    private JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();

    private UserDao() {
    }

    public static UserDao getInstance() {
        return userDao;
    }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    @Override
    public User findById(String userId) {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper<User> rm = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));

        return jdbcTemplate.queryForObject(sql, rm, userId);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT userId, password, name, email FROM USERS";

        RowMapper<User> rm = rs -> new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                rs.getString("email"));

        return jdbcTemplate.query(sql, rm);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE USERS set password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }
}
