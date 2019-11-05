package nextstep.di.factory.example;

import nextstep.annotation.Inject;

public class TestService {

    private final TestRepository testRepository;

    @Inject
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }
}
