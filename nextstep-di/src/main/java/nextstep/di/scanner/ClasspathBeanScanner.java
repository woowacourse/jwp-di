package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.initiator.ClasspathBeanInitiator;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

public class ClasspathBeanScanner extends BeanScanner {

    public ClasspathBeanScanner(BeanFactory beanFactory, Object... basePackage) {
        super(basePackage);
        initialize(beanFactory);
    }

    public void initialize(BeanFactory beanFactory) {
        for (Class<?> clazz : super.scanAnnotatedWith(Controller.class, Service.class, Repository.class)) {
            beanFactory.addBeanInitiator(clazz, new ClasspathBeanInitiator(clazz));
        }
    }
}
