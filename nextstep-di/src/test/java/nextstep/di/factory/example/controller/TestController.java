package nextstep.di.factory.example.controller;

import nextstep.di.factory.example.repository.TestJdbcTemplate;
import nextstep.di.factory.example.service.TestService;
import nextstep.stereotype.Controller;

@Controller
public class TestController {
    private TestService testService;
    private TestJdbcTemplate testJdbcTemplate;

    public TestController(TestService testService, TestJdbcTemplate testJdbcTemplate) {
        this.testService = testService;
        this.testJdbcTemplate = testJdbcTemplate;
    }

    public TestService getTestService() {
        return testService;
    }
}
