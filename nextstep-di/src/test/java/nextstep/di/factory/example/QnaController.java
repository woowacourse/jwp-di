package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class QnaController {
    private MyQnaService qnaService;

    @Inject
    public QnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    @TestMethodAnnotation
    public void test() {
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }
}
