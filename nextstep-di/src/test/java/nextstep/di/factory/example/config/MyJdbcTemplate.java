package nextstep.di.factory.example.config;

import javax.sql.DataSource;

public class MyJdbcTemplate {
    private DataSource dataSource;

    public MyJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
