package nextstep.di.factory.example.component;

import nextstep.annotation.Inject;

public class RecursiveController {
    private RecursiveController myself;

    @Inject
    public RecursiveController(RecursiveController myself) {
        this.myself = myself;
    }
}
