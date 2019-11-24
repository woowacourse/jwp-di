package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.BeanDefinition;
import nextstep.di.exception.BeanFactoryInitializeException;
import nextstep.di.exception.DuplicatedBeanDefinition;
import nextstep.di.exception.NotFoundBeanDefinition;
import nextstep.di.registry.BeanRegistry;
import nextstep.di.scanner.BeanScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

public class SingleBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(SingleBeanFactory.class);

    private BeanRegistry beanRegistry;
    private Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newLinkedHashMap();

    public SingleBeanFactory(BeanScanner... beanScanners) {
        this.beanRegistry = new BeanRegistry();
        initializeBeanDefinitions(beanScanners);
    }

    private Map<Class<?>, BeanDefinition> initializeBeanDefinitions(BeanScanner... beanScanners) {
        for (BeanScanner beanScanner : beanScanners) {
            putBeanDefinition(beanScanner);
        }

        return beanDefinitions;
    }

    private void putBeanDefinition(BeanScanner beanScanner) {
        for (BeanDefinition beanDefinition : beanScanner.doScan()) {
            checkDuplicate(beanDefinition);
            beanDefinitions.put(beanDefinition.getType(), beanDefinition);
        }
    }

    private void checkDuplicate(BeanDefinition beanDefinition) {
        if (beanDefinitions.containsKey(beanDefinition.getType())) {
            logger.error("Duplicated BeanDefinition: {}", beanDefinition.getType());
            throw new DuplicatedBeanDefinition();
        }
    }

    @Override
    public void initialize() {
        beanDefinitions.entrySet().forEach(beanDefinitionEntry -> {
            try {
                checkAndCreateBean(beanDefinitionEntry.getValue());
            } catch (Exception e) {
                throw new BeanFactoryInitializeException(e);
            }
        });
    }

    private void checkAndCreateBean(BeanDefinition beanDefinition) throws Exception {
        if (!beanRegistry.isEnrolled(beanDefinition.getType())) {
            logger.debug("needed Bean: {}", beanDefinition.getType());
            createBean(beanDefinition);
        }
    }

    private void createBean(BeanDefinition beanDefinition) throws Exception {
        List<Object> params = new ArrayList<>();

        for (Class parameterType : beanDefinition.getParameterTypes()) {
            BeanDefinition selectedBeanDefinition = searchBeanDefinition(parameterType);

            checkAndCreateBean(selectedBeanDefinition);
            params.add(beanRegistry.get(selectedBeanDefinition.getType()));
        }
        logger.debug("{} create Bean: {}", beanDefinition.getType(), params.toArray());
        beanRegistry.put(beanDefinition.getType(), beanDefinition.createBean(params.toArray()));
    }

    private BeanDefinition searchBeanDefinition(Class<?> type) {
        if (Objects.nonNull(beanDefinitions.get(type))) {
            return beanDefinitions.get(type);
        }

        return beanDefinitions.entrySet().stream()
                .map(beanDefinitionEntry -> beanDefinitionEntry.getValue())
                .filter(beanDefinition -> beanDefinition.isType(type))
                .findAny()
                .orElseThrow(NotFoundBeanDefinition::new)
                ;
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
