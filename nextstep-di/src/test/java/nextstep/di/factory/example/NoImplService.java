package nextstep.di.factory.example;

import nextstep.annotation.Inject;

public class NoImplService {
    private final NoImplRepository noImplRepository;

    @Inject
    public NoImplService(NoImplRepository noImplRepository) {
        this.noImplRepository = noImplRepository;
    }
}
