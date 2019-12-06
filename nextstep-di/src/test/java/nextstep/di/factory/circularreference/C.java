package nextstep.di.factory.circularreference;

import annotation.Service;
import nextstep.annotation.Inject;

@Service
public class C {
    @Inject
    public C(A a) {
    }
}
