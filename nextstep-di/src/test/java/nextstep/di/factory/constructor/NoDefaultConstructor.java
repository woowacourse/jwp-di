package nextstep.di.factory.constructor;

import nextstep.stereotype.Controller;

@Controller
public class NoDefaultConstructor {
    private final AnyService anyService;

    public NoDefaultConstructor(AnyService anyService) {
        this.anyService = anyService;
    }
}
