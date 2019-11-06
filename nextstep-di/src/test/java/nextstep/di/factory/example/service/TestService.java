package nextstep.di.factory.example.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class TestService {

    private MyQnaService myQnaService;

    @Inject
    public TestService(MyQnaService myQnaService) {
        this.myQnaService = myQnaService;
    }

    public MyQnaService getMyQnaService() {
        return myQnaService;
    }
}
