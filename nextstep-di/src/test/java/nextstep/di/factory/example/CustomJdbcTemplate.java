package nextstep.di.factory.example;

import javax.sql.DataSource;

public class CustomJdbcTemplate {
    private DataSource dataSource;

    public CustomJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
