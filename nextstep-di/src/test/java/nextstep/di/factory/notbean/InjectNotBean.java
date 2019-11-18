package nextstep.di.factory.notbean;

import nextstep.annotation.Inject;

public class InjectNotBean {
    private NotBean notBean;

    @Inject
    public InjectNotBean(NotBean notBean) {
        this.notBean = notBean;
    }
}
