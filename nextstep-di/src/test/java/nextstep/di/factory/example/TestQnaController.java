package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class TestQnaController {
    private MyQnaService qnaService;

    @Inject
    public TestQnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }
}
