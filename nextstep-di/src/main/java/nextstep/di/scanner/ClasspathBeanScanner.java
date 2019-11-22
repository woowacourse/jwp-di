package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.di.factory.BeanFactory;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class ClasspathBeanScanner implements Scanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);
    private static final Class[] COMPONENTS = {Controller.class, Service.class, Repository.class};

    private BeanFactory beanFactory;
    private Reflections reflections;
    private Map<Class<?>, BeanDefinition> beans;

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beans = Maps.newHashMap();
    }

    public void registerPackage(Object... basePackage) {
        reflections = new Reflections(basePackage);
        initializeDefinitions();
    }

    public void registerBeanInfo() {
        beanFactory.register(beans);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initializeDefinitions() {
        Set<Class<?>> typesAnnotatedWith = ScannerUtils.getTypesAnnotateWith(reflections, COMPONENTS);

        for (Class<?> clazz : typesAnnotatedWith) {
            beans.put(clazz, new ClasspathBean(clazz));
        }
        log.debug("component bean : {}", beans);
    }
}
