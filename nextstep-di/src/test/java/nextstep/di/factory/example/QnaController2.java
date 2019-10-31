package nextstep.di.factory.example;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class QnaController2 {
    private MyQnaService qnaService;

    @Inject
    public QnaController2(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }
}
