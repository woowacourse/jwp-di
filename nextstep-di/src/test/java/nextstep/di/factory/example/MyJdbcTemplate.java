package nextstep.di.factory.example;

import nextstep.annotation.ComponentScan;
import nextstep.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"next","core"})
public class MyJdbcTemplate {
    private DataSource dataSource;

    public MyJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
