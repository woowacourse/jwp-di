package nextstep.di.factory.utilsexample;

import nextstep.annotation.Inject;
import nextstep.stereotype.Controller;

@Controller
public class MultipleInjectController {

    @Inject
    public MultipleInjectController() {
    }

    @Inject
    public MultipleInjectController(int test) {
    }
}
