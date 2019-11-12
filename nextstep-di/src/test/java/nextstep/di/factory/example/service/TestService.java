package nextstep.di.factory.example.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class TestService {

    @Inject
    public TestService(TestServiceObject testObject) {
    }
}

