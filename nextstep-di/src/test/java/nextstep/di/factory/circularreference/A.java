package nextstep.di.factory.circularreference;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class A {
    @Inject
    public A(B b) {
    }
}
