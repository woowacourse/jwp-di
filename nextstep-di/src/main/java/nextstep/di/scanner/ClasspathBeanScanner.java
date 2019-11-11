package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.di.initiator.BeanInitiator;
import nextstep.di.initiator.ClasspathBeanInitiator;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;

import java.util.Map;
import java.util.Set;

public class ClasspathBeanScanner extends BeanScannerImpl {
    private Map<Class<?>, ClasspathBeanInitiator> classpathBeans;

    public ClasspathBeanScanner(Object... basePackage) {
        super(basePackage);
        classpathBeans = Maps.newHashMap();
        initialize();
    }

    public void initialize() {
        for (Class<?> clazz : super.scanAnnotatedWith(Controller.class, Service.class, Repository.class)) {
            register(clazz);
        }
    }

    public void register(Class<?> clazz) {
        classpathBeans.put(clazz, new ClasspathBeanInitiator(clazz));
    }

    @Override
    public Set<Class<?>> getInstantiatedTypes() {
        return classpathBeans.keySet();
    }

    @Override
    public boolean isContainsBean(Class<?> clazz) {
        return classpathBeans.containsKey(clazz);
    }

    @Override
    public BeanInitiator getBeanInitiator(Class<?> clazz) {
        return classpathBeans.get(clazz);
    }
}
