package nextstep.di.factory.example;

import nextstep.stereotype.Controller;

@Controller
public class TestController {
    private MyQnaService qnaService;

    public TestController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }

}
