package nextstep.di.cyclereference.example.simple;

import nextstep.stereotype.Controller;

@Controller
public class TestCycleController1 {
    private TestCycleController2 testCycleController2;

    public TestCycleController1(TestCycleController2 testCycleController2) {
        this.testCycleController2 = testCycleController2;
    }

    public TestCycleController2 getTestCycleController2() {
        return testCycleController2;
    }
}