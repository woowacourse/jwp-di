package nextstep.di.factory.circularreference;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class B {

    @Inject
    public B(C c) {
    }
}
