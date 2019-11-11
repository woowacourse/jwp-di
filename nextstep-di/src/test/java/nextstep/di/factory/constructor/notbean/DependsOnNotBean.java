package nextstep.di.factory.constructor.notbean;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class DependsOnNotBean {
    private AnyClass anyClass;

    @Inject
    public DependsOnNotBean(final AnyClass anyClass) {
        this.anyClass = anyClass;
    }
}
