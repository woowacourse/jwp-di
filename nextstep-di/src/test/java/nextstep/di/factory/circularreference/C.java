package nextstep.di.factory.circularreference;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class C {

    @Inject
    public C(A a) {
    }
}
