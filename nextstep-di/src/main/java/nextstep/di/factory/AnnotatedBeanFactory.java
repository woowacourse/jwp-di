package nextstep.di.factory;

import nextstep.di.exception.BeanFactoryInitializeException;
import nextstep.di.scanner.BeanScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotatedBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(AnnotatedBeanFactory.class);

    private BeanRegistry beanRegistry;
    private BeanScanner beanScanner;

    public AnnotatedBeanFactory(BeanRegistry beanRegistry, BeanScanner beanScanner) {
        this.beanRegistry = beanRegistry;
        this.beanScanner = beanScanner;
    }

    @Override
    public void initialize() {
        beanScanner.doScan().forEach(bean -> {
            try {
                registerBean(bean, bean);
            } catch (Exception e) {
                throw new BeanFactoryInitializeException(e);
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return beanRegistry.get(requiredType);
    }

    @Override
    public Set<Class<?>> getTypes(Class<? extends Annotation> annotation) {
        return Collections.unmodifiableSet(beanRegistry.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet()));
    }

    private void registerBean(Class<?> bean, Class concreteClass) throws Exception {
        if (!beanRegistry.isEnrolled(bean)) {
            createBean(concreteClass);
        }
    }

    private void createBean(Class<?> clazz) throws Exception {
        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        List<Object> params = createParams(clazz, constructor);

        beanRegistry.put(clazz, constructor.newInstance(params.toArray()));
    }

    private List<Object> createParams(Class<?> bean, Constructor constructor) throws Exception {
        List<Object> params = new ArrayList<>();

        for (Class parameterType : constructor.getParameterTypes()) {
            Class concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, beanScanner.doScan());
            registerBean(bean, concreteClass);
            params.add(beanRegistry.get(concreteClass));
        }

        return params;
    }
}
