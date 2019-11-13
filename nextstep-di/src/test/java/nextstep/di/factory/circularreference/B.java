package nextstep.di.factory.circularreference;

import nextstep.annotation.Inject;
import nextstep.di.factory.circularreference.A;
import nextstep.stereotype.Service;

@Service
public class B {
    @Inject
    public B(A a) {
    }
}
