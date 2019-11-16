package nextstep.di.factory.example.repository;

import nextstep.di.factory.example.service.TestService;

import javax.sql.DataSource;

public class TestJdbcTemplate {
    private DataSource dataSource;
    private TestService testService;


    public TestJdbcTemplate(DataSource dataSource, TestService testService) {
        this.dataSource = dataSource;
        this.testService = testService;
    }

    public TestService getTestService() {
        return testService;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
