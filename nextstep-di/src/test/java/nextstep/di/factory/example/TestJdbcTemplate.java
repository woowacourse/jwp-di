package nextstep.di.factory.example;

import javax.sql.DataSource;

public class TestJdbcTemplate {
    private DataSource dataSource;
    private JdbcUserRepository jdbcUserRepository;


    public TestJdbcTemplate(DataSource dataSource, JdbcUserRepository jdbcUserRepository) {
        this.dataSource = dataSource;
        this.jdbcUserRepository = jdbcUserRepository;
    }

    public JdbcUserRepository getJdbcUserRepository() {
        return jdbcUserRepository;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
