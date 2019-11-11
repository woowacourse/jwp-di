package nextstep.di.factory.example.controller;

import nextstep.annotation.Inject;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.stereotype.Controller;

@Controller
public class QnaController {
    private MyQnaService qnaService;

    @Inject
    public QnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }
}
