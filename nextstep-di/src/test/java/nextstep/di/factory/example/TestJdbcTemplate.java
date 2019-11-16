package nextstep.di.factory.example;

import javax.sql.DataSource;

public class TestJdbcTemplate {
    private DataSource dataSource;
    private TestService testServcie;


    public TestJdbcTemplate(DataSource dataSource, TestService testService) {
        this.dataSource = dataSource;
        this.testServcie = testServcie;
    }

    public TestService getTestServcie() {
        return testServcie;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
