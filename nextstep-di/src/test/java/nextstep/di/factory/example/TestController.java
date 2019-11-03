package nextstep.di.factory.example;

import nextstep.annotation.Inject;

public class TestController {

    private final TestService testService;

    @Inject
    public TestController(TestService testService) {
        this.testService = testService;
    }
}
