package nextstep.di.factory.example.controller;

import nextstep.di.factory.example.repository.TestJdbcTemplate;
import nextstep.stereotype.Controller;

@Controller
public class TestController2 {
    private TestJdbcTemplate testJdbcTemplate;

    public TestController2(TestJdbcTemplate testJdbcTemplate) {
        this.testJdbcTemplate = testJdbcTemplate;
    }

    public TestJdbcTemplate getTestJdbcTemplate() {
        return testJdbcTemplate;
    }
}
