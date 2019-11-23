package nextstep.di.factory.exampleforinvalid.notexistbean;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class HelloController {
    private final HelloService helloService;

    @Inject
    public HelloController(HelloService helloService) {
        if (helloService == null) {
            throw new NullPointerException();
        }
        this.helloService = helloService;
    }
}
