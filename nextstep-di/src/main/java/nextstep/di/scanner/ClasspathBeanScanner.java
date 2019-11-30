package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import nextstep.di.initiator.ClasspathBeanInitiator;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

public class ClasspathBeanScanner extends BeanScanner {

    @Override
    public void doScan(BeanFactory beanFactory) {
        for (Class<?> clazz : super.scanAnnotatedWith(Controller.class, Service.class, Repository.class)) {
            beanFactory.addBeanInitiator(clazz, new ClasspathBeanInitiator(clazz));
        }
    }
}
