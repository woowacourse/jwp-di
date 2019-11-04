package nextstep.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private static final Logger logger = LoggerFactory.getLogger( JdbcTemplate.class );

    private static JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcTemplate() {
    }

    public static JdbcTemplate getInstance() {
        return jdbcTemplate;
    }

    public void update(String sql, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementSetter.setParameters(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void update(String sql, Object... parameters) {
        update(sql, createPreparedStatementSetter(parameters));
    }

    public void update(PreparedStatementCreator psc, KeyHolder holder) {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = psc.createPreparedStatement(conn)) {
            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()) {
                long generatedKey = resultSet.getLong(1);
                logger.debug("Generated Key : {}", generatedKey);
                holder.setId(generatedKey);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) {
        List<T> list = query(sql, rowMapper, preparedStatementSetter);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return queryForObject(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, PreparedStatementSetter preparedStatementSetter) throws DataAccessException {
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatementSetter.setParameters(preparedStatement);
            return mapResultSetToObject(rowMapper, preparedStatement);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private <T> List<T> mapResultSetToObject(RowMapper<T> rowMapper, PreparedStatement preparedStatement) {
        try(ResultSet resultSet = preparedStatement.executeQuery()) {
            List<T> list = new ArrayList<T>();
            while (resultSet.next()) {
                list.add(rowMapper.mapRow(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters) {
        return query(sql, rowMapper, createPreparedStatementSetter(parameters));
    }

    private PreparedStatementSetter createPreparedStatementSetter(Object... parameters) {
        return preparedStatement -> {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
        };
    }
}
