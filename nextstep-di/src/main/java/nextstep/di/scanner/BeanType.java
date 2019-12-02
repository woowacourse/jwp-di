package nextstep.di.scanner;

import nextstep.stereotype.Component;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.lang.annotation.Annotation;

public enum BeanType {
    CONTROLLER(Controller.class),
    SERVICE(Service.class),
    REPOSITORY(Repository.class),
    COMPONENT(Component.class);

    private Class<? extends Annotation> type;

    BeanType(final Class<? extends Annotation> type) {
        this.type = type;
    }

    public Class<? extends Annotation> getType() {
        return type;
    }
}
