package nextstep.di.factory.exampleforinvalid.cycle;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class ServiceA {
    private final ServiceB serviceB;

    @Inject
    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
