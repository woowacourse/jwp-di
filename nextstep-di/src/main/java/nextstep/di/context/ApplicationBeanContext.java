package nextstep.di.context;

import nextstep.annotation.ComponentScan;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.SingleBeanFactory;
import nextstep.di.registry.BeanRegistry;
import nextstep.di.scanner.AnnotatedBeanScanner;
import nextstep.di.scanner.ConfigurationScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationBeanContext implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationBeanContext.class);

    private BeanRegistry beanRegistry = new BeanRegistry();
    private Object[] root;

    public ApplicationBeanContext(Object... root) {
        this.root = initializeRoot(root);
        initialize();
    }

    private Object[] initializeRoot(Object... root) {
        for (Object o : root) {
            if (o instanceof Class) {
                Object[] clazz = getComponentScan((Class) o);
                if (clazz != null) return clazz;
            }
        }

        return root;
    }

    private Object[] getComponentScan(Class o) {
        Class<?> clazz = o;
        if (clazz.isAnnotationPresent(ComponentScan.class)) {
            return clazz.getAnnotation(ComponentScan.class).value();
        }
        return null;
    }

    @Override
    public void initialize() {
        BeanFactory beanFactory = new SingleBeanFactory(
                beanRegistry,
                new ConfigurationScanner(this),
                new AnnotatedBeanScanner(this)
        );

        beanFactory.initialize();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return beanRegistry.get(requiredType);
    }

    @Override
    public Set<Class<?>> getTypes(Class<? extends Annotation> annotation) {
        return Collections.unmodifiableSet(beanRegistry.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet()));
    }

    public Object[] getRoot() {
        return root;
    }
}
