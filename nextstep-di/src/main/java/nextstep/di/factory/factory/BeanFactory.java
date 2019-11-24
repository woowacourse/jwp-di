package nextstep.di.factory.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.CycleReferenceException;
import nextstep.di.factory.strategy.BeanCreationStrategies;
import nextstep.di.factory.strategy.ComponentBeanCreationStrategy;
import nextstep.di.factory.strategy.ConfigurationBeanCreationStrategy;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {
    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preComponentInstantiateBeans;
    private Set<Class<?>> preConfigurationInstantiateBeans;

    private BeanCreationStrategies beanCreationStrategies;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(String baseDirectory) {
        BeanScanner beanScanner = new BeanScanner(baseDirectory);
        this.preComponentInstantiateBeans = beanScanner.getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        this.initialize();
    }

    public BeanFactory(Set<Class<?>> preComponentInstantiateBeans) {
        this.preComponentInstantiateBeans = Collections.unmodifiableSet(preComponentInstantiateBeans);
        log.debug("pre construction beans: {}", preComponentInstantiateBeans);
        this.preConfigurationInstantiateBeans = Collections.unmodifiableSet(makePreComponentInstantiateBeans());
        log.debug("pre method beans: {}", preConfigurationInstantiateBeans);
        this.beanCreationStrategies = makeCreationStrategies();
    }

    private Set<Class<?>> makePreComponentInstantiateBeans() {
        return BeanFactoryUtils.getConfigurationBeanClasses(preComponentInstantiateBeans);
    }

    private BeanCreationStrategies makeCreationStrategies() {
        return new BeanCreationStrategies(
                Arrays.asList(new ComponentBeanCreationStrategy(preComponentInstantiateBeans), new ConfigurationBeanCreationStrategy(BeanFactoryUtils.getMethods(preComponentInstantiateBeans), preConfigurationInstantiateBeans)));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        checkCycleReference(preComponentInstantiateBeans);
        preComponentInstantiateBeans
                .forEach(this::createBean);
        preConfigurationInstantiateBeans
                .forEach(this::createBean);
    }

    private void checkCycleReference(Set<Class<?>> preInstantiateBeans) {
        log.debug("==> checking cycle reference of {}", preInstantiateBeans);
        preInstantiateBeans.forEach(beanClass -> checkCycleReference(beanClass, new ArrayList<>()));
    }

    private void checkCycleReference(Class<?> beanClass, List<String> beanNames) {
        if (beanClass.isInterface()) {
            return;
        }
        log.debug("==>checking cycleReference bean: {}, beanNames: {}", beanClass, beanNames);
        beanClass = BeanFactoryUtils.findConcreteClass(beanClass, preComponentInstantiateBeans);
        Constructor<?> constructor = BeanFactoryUtils.getConstructor(beanClass, preComponentInstantiateBeans);
        if (beanNames.contains(constructor.getName())) {
            throw new CycleReferenceException();
        }
        beanNames.add(constructor.getName());
        Arrays.stream(constructor.getParameterTypes())
                .forEach(parameterBeanClass -> {
                    List<String> accumulatedBeanNames = new ArrayList<>(beanNames);
                    checkCycleReference(parameterBeanClass, accumulatedBeanNames);
                });
    }

    private void createBean(Class<?> preInstantiateBean) {
        beanCreationStrategies.createBean(preInstantiateBean, beans);
    }

    public Map<Class<?>, Object> getBeans() {
        return Collections.unmodifiableMap(beans);
    }
}
