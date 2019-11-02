package nextstep.di.cyclereference.example.complex;

import nextstep.stereotype.Service;

@Service
public class TestCycleService {
    private TestCycleRepository testCycleRepository;

    public TestCycleService(TestCycleRepository testCycleRepository) {
        this.testCycleRepository = testCycleRepository;
    }

    public TestCycleRepository getTestCycleRepository() {
        return testCycleRepository;
    }
}
