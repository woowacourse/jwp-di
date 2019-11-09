package nextstep.di.factory.circular;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class CircularDependenceA {

    private CircularDependenceB circularDependenceB;

    @Inject
    public CircularDependenceA(CircularDependenceB circularDependenceB) {
        this.circularDependenceB = circularDependenceB;
    }
}
