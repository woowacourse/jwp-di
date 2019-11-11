package nextstep.di.factory.circular;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;

@Service
public class CircularDependenceB {
    private CircularDependenceA circularDependenceA;

    @Inject
    public CircularDependenceB(CircularDependenceA circularDependenceA) {
        this.circularDependenceA = circularDependenceA;
    }
}
