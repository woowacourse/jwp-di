package slipp.dao;

import nextstep.annotation.Inject;
import nextstep.jdbc.JdbcTemplate;
import nextstep.jdbc.RowMapper;
import nextstep.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private JdbcTemplate jdbcTemplate;

    @Inject
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(User user) {
        log.debug("begin");

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    public User findByUserId(String userId) {
        log.debug("begin");

        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        RowMapper<User> rm = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };

        return jdbcTemplate.queryForObject(sql, rm, userId);
    }

    public List<User> findAll() {
        log.debug("begin");

        String sql = "SELECT userId, password, name, email FROM USERS";

        RowMapper<User> rm = new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
                        rs.getString("email"));
            }
        };

        return jdbcTemplate.query(sql, rm);
    }

    public void update(User user) {
        log.debug("begin");

        String sql = "UPDATE USERS set password = ?, name = ?, email = ? WHERE userId = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
    }
}
