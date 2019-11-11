package nextstep.di.factory.notbean;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class InjectNotBean {
    private NotBean notBean;

    @Inject
    public InjectNotBean(NotBean notBean) {
        this.notBean = notBean;
    }
}
