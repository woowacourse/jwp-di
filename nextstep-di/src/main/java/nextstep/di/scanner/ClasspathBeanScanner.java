package nextstep.di.scanner;

import nextstep.di.factory.BeanFactoryImpl;
import nextstep.di.initiator.ClasspathBeanInitiator;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

public class ClasspathBeanScanner extends BeanScanner {

    public ClasspathBeanScanner(BeanFactoryImpl beanFactoryImpl, Object... basePackage) {
        super(basePackage);
        initialize(beanFactoryImpl);
    }

    public void initialize(BeanFactoryImpl beanFactoryImpl) {
        for (Class<?> clazz : super.scanAnnotatedWith(Controller.class, Service.class, Repository.class)) {
            beanFactoryImpl.addBeanInitiator(clazz, new ClasspathBeanInitiator(clazz));
        }
    }
}
