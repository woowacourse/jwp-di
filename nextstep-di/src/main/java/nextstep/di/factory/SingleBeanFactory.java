package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.BeanDefinition;
import nextstep.di.exception.BeanFactoryInitializeException;
import nextstep.di.exception.DuplicatedBeanDefinition;
import nextstep.di.exception.NotFoundBeanDefinition;
import nextstep.di.registry.BeanRegistry;
import nextstep.di.scanner.BeanScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SingleBeanFactory implements BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(SingleBeanFactory.class);

    private BeanRegistry beanRegistry;
    private Set<BeanDefinition> beanDefinitions;

    public SingleBeanFactory(BeanScanner... beanScanners) {
        this.beanRegistry = new BeanRegistry();
        initializeBeanDefinitions(beanScanners);
    }

    private Set<BeanDefinition> initializeBeanDefinitions(BeanScanner... beanScanners) {
        beanDefinitions = Sets.newHashSet();

        for (BeanScanner beanScanner : beanScanners) {
            Set<BeanDefinition> addedBeanDefinition = beanScanner.doScan();
            if (isDuplicated(addedBeanDefinition)) {
                logger.debug("Duplicated BeanDefinition: {}, {}", beanDefinitions, addedBeanDefinition);
                throw new DuplicatedBeanDefinition();
            }
            beanDefinitions.addAll(addedBeanDefinition);
        }
        return beanDefinitions;
    }

    private boolean isDuplicated(Set<BeanDefinition> addedBeanDefinitions) {
        return addedBeanDefinitions.stream()
                .anyMatch(addedBeanDefinition -> beanDefinitions.contains(addedBeanDefinition))
                ;
    }

    @Override
    public void initialize() {
        beanDefinitions.forEach(beanDefinition -> {
            try {
                checkAndCreateBean(beanDefinition);
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
        return beanDefinitions.stream()
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
