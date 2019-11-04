package nextstep.di.cyclereference.example.simple;

import nextstep.stereotype.Controller;

@Controller
public class TestCycleController2 {
    private TestCycleController1 testCycleController1;

    public TestCycleController2(TestCycleController1 testCycleController2) {
        this.testCycleController1 = testCycleController2;
    }

    public TestCycleController1 getTestCycleController1() {
        return testCycleController1;
    }
}