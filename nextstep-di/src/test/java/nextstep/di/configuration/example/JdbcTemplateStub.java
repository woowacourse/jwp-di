package nextstep.di.configuration.example;

import javax.sql.DataSource;

public class JdbcTemplateStub {

    private final DataSource dataSource;

    public JdbcTemplateStub(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
