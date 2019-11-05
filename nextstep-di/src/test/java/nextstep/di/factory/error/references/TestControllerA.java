package nextstep.di.factory.error.references;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class TestControllerA {
    private TestControllerB testControllerB;

    @Inject
    public TestControllerA(TestControllerB testControllerB) {
        this.testControllerB = testControllerB;
    }

    public TestControllerB getTestControllerB() {
        return testControllerB;
    }
}
