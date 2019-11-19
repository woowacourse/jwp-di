package nextstep.di.factory;

import nextstep.di.BeanDefinition;
import nextstep.di.exception.BeanFactoryInitializeException;
import nextstep.di.exception.NotFoundBeanDefinition;
import nextstep.di.scanner.BeanScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
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
        beanScanner.doScan().forEach(beanDefinition -> {
            try {
                checkAndCreateBean(beanDefinition);
            } catch (Exception e) {
                throw new BeanFactoryInitializeException(e);
            }
        });
    }

    private void checkAndCreateBean(BeanDefinition beanDefinition) throws Exception {
        if (!beanRegistry.isEnrolled(beanDefinition.getType())) {
            createBean(beanDefinition);
        }
    }

    private void createBean(BeanDefinition beanDefinition) throws Exception {
        List<Object> params = new ArrayList<>();

        for (Class parameterType : beanDefinition.getParameterTypes()) {
            checkAndCreateBean(searchBeanDefinition(parameterType));
            params.add(beanRegistry.get(searchBeanDefinition(parameterType).getType()));
        }
        logger.debug("{} create Bean: {}", beanDefinition.getType(), params.toArray());
        beanRegistry.put(beanDefinition.getType(), beanDefinition.createBean(params.toArray()));
    }

    private BeanDefinition searchBeanDefinition(Class<?> type) {
        return beanScanner.doScan().stream()
                .filter(beanDefinition -> beanDefinition.isType(type))
                .findAny()
                .orElseThrow(NotFoundBeanDefinition::new);
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
}
