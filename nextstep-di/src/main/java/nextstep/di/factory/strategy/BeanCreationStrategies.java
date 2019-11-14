package nextstep.di.factory.strategy;

import nextstep.di.factory.exception.CreateBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanCreationStrategies {
    private static final Logger log = LoggerFactory.getLogger(BeanCreationStrategies.class);
    private List<BeanCreationStrategy> strategies;

    public BeanCreationStrategies(List<BeanCreationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Object createBean(Class<?> clazz, Map<Class<?>, Object> beans){

        log.debug("creating bean... {}", clazz);

        BeanCreationStrategy beanCreationStrategy = strategies.stream()
                .filter(strategy -> strategy.canHandle(clazz))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        List<Class<?>> parameters = beanCreationStrategy.getDependencyTypes(clazz);
        List<Object> parameterInstances = new ArrayList<>();

        for (Class<?> parameter : parameters) {
            parameterInstances.add(createBean(parameter, beans));
        }
        try {
            Object instance =  beanCreationStrategy.createBean(clazz, parameterInstances, beans);
            return instance;
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            throw new CreateBeanException(e);
        }
    }
}
