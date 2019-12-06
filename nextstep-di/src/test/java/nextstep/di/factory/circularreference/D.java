package nextstep.di.factory.circularreference;

import annotation.Service;
import nextstep.annotation.Inject;

@Service
public class D {
    @Inject
    public D(E e) {
    }
}
