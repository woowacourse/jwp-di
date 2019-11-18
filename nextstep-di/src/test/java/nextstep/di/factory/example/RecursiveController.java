package nextstep.di.factory.example;

import nextstep.annotation.Inject;

public class RecursiveController {
    private RecursiveController recursiveController;

    @Inject
    public RecursiveController(RecursiveController recursiveController) {
        this.recursiveController = recursiveController;
    }
}
