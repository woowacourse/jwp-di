package nextstep.di.factory.utilsexample;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class OneInjectController {

    @Inject
    public OneInjectController() {
    }

    public OneInjectController(int test) {
    }

    public OneInjectController(String test) {
    }
}
