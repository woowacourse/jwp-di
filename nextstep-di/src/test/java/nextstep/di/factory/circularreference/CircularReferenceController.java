package nextstep.di.factory.circularreference;

import nextstep.annotation.Inject;

public class CircularReferenceController {
    private CircularReferenceController circularReferenceController;

    @Inject
    public CircularReferenceController(CircularReferenceController circularReferenceController) {
        this.circularReferenceController = circularReferenceController;
    }
}
