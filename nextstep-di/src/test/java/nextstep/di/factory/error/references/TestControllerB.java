package nextstep.di.factory.error.references;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class TestControllerB {
    private TestControllerA testControllerA;

    @Inject
    public TestControllerB(final TestControllerA testControllerA) {
        this.testControllerA = testControllerA;
    }

    public TestControllerA getTestControllerA() {
        return testControllerA;
    }
}
