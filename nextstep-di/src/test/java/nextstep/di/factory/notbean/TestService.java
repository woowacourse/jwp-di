package nextstep.di.factory.notbean;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class TestService {
    @Inject
    public TestService(NotBean notBean) {
    }
}
