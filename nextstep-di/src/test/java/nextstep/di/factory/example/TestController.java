package nextstep.di.factory.example;

import nextstep.stereotype.Controller;

@Controller
public class TestController {
    private TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    public TestService getTestService() {
        return testService;
    }
}
