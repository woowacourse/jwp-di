package nextstep.di.scanner;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

public class ClasspathBeanScanner extends ComponentAnnotationScanner {

    private static final Class[] COMPONENT_ANNOTATIONS = {Controller.class, Repository.class, Service.class};

    public ClasspathBeanScanner(Object... basePackages) {
        super(COMPONENT_ANNOTATIONS, basePackages);
    }
}
