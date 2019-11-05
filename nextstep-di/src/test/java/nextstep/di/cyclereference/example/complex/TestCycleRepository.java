package nextstep.di.cyclereference.example.complex;

import nextstep.stereotype.Repository;

@Repository
public class TestCycleRepository {
    private TestCycleController testCycleController;

    public TestCycleRepository(TestCycleController testCycleController) {
        this.testCycleController = testCycleController;
    }

    public TestCycleController getTestCycleController() {
        return testCycleController;
    }
}
