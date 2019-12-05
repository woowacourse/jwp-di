package nextstep.di.factory.failexample.circularreference;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class ServiceB {
    private final ServiceA serviceA;

    @Inject
    public ServiceB(ServiceA serviceA) {
        this.serviceA = serviceA;
    }
}
