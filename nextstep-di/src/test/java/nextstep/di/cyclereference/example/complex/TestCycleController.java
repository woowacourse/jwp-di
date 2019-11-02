package nextstep.di.cyclereference.example.complex;

import nextstep.di.cyclereference.example.simple.TestCycleController2;
import nextstep.stereotype.Controller;

@Controller
public class TestCycleController {
    private TestCycleService testCycleService;

    public TestCycleController(TestCycleService testCycleService) {
        this.testCycleService = testCycleService;
    }

    public TestCycleService getTestCycleService() {
        return testCycleService;
    }
}
