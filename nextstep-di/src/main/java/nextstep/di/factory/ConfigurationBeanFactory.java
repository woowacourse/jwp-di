package nextstep.di.factory;

import nextstep.di.MethodBeanDefinition;
import nextstep.di.exception.BeanFactoryInitializeException;
import nextstep.di.exception.NotFoundBeanDefinition;
import nextstep.di.scanner.ConfigurationScanner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanFactory implements BeanFactory {
    private BeanRegistry beanRegistry;
    private ConfigurationScanner beanScanner;

    public ConfigurationBeanFactory(BeanRegistry beanRegistry, ConfigurationScanner beanScanner) {
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

    private void checkAndCreateBean(MethodBeanDefinition beanDefinition) throws Exception {
        if (!beanRegistry.isEnrolled(beanDefinition.getType())) {
            createBean(beanDefinition);
        }
    }

    private void createBean(MethodBeanDefinition beanDefinition) throws Exception {
        List<Object> params = new ArrayList<>();

        for (Class parameterType : beanDefinition.getParameterTypes()) {
            checkAndCreateBean(searchBeanDefinition(parameterType));
            params.add(beanRegistry.get(beanDefinition.getType()));
        }

        beanRegistry.put(beanDefinition.getType(), beanDefinition.createBean(params.toArray()));
    }

    private MethodBeanDefinition searchBeanDefinition(Class<?> type) {
        return beanScanner.doScan().stream()
                .filter(beanDefinition -> beanDefinition.isType(type))
                .findAny()
                .orElseThrow(NotFoundBeanDefinition::new);
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
}
