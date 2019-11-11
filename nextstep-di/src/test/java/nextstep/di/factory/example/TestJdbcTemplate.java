package nextstep.di.factory.example;

import javax.sql.DataSource;

public class TestJdbcTemplate {
    private DataSource dataSource;


    public TestJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
