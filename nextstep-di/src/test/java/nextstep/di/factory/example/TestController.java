package nextstep.di.factory.example;

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
