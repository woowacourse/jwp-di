package nextstep.di.scanner;

import com.google.common.collect.Maps;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.BeanFactoryUtils;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

public class ClasspathBeanScanner extends AbstractBeanScanner {
    private static final Class[] COMPONENTS = {Controller.class, Service.class, Repository.class};

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public void doScan(Object... basePackage) {
        reflections = new Reflections(basePackage);
        beanFactory.registerBean(getBeans());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<Class<?>, Constructor> getBeans() {
        Set<Class<?>> typesAnnotatedWith = getTypesAnnotatedWith(COMPONENTS);
        Map<Class<?>, Constructor> beans = Maps.newHashMap();
        for (Class<?> clazz : typesAnnotatedWith) {
            beans.put(clazz, BeanFactoryUtils.getInjectedConstructor(clazz));
        }
        return beans;
    }
}
