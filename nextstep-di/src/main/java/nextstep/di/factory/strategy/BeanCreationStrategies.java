package nextstep.di.factory.strategy;

import nextstep.di.factory.exception.CreateBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanCreationStrategies {
    private static final Logger log = LoggerFactory.getLogger(BeanCreationStrategies.class);
    private List<BeanCreationStrategy> strategies;

    public BeanCreationStrategies(List<BeanCreationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Object createBean(Class<?> clazz, Map<Class<?>, Object> beans) {
        log.debug("creating bean... {}", clazz);

        BeanCreationStrategy beanCreationStrategy = findBeanCreationStrategy(clazz);
        List<Class<?>> parameters = beanCreationStrategy.getDependencyTypes(clazz);
        List<Object> parameterInstances = collectParameterInstances(parameters, beans);

        return instantiateBean(clazz, beans, beanCreationStrategy, parameterInstances);
    }

    private BeanCreationStrategy findBeanCreationStrategy(Class<?> clazz) {
        return strategies.stream()
                .filter(strategy -> strategy.canHandle(clazz))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Object> collectParameterInstances(List<Class<?>> parameters, Map<Class<?>, Object> beans) {
        return parameters.stream()
                .map(parameter -> createBean(parameter, beans))
                .collect(Collectors.toList());
    }

    private Object instantiateBean(Class<?> clazz, Map<Class<?>, Object> beans, BeanCreationStrategy beanCreationStrategy, List<Object> parameterInstances) {
        try {
            Object instance = beanCreationStrategy.createBean(clazz, parameterInstances, beans);
            return instance;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new CreateBeanException(e);
        }
    }
}
