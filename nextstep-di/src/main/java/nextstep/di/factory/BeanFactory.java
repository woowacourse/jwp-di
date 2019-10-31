package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;

public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            registerBean(preInstantiateBean);
        }
    }

    private void registerBean(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return;
        }

        Constructor<?> constructor = getConstructorOf(preInstantiateBean);
        Object bean = instantiateBeanOf(constructor);
        log.info("created bean: {}", bean);

        beans.put(preInstantiateBean, bean);
    }

    private Constructor<?> getConstructorOf(final Class<?> preInstantiateBean) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);

        if (Objects.isNull(constructor)) {
            return BeanFactoryUtils.getDefaultConstructor(preInstantiateBean);
        }
        return constructor;
    }

    private Object instantiateBeanOf(final Constructor<?> constructor) {
        List<Object> arguments = extractArgumentsOf(constructor);
        return BeanFactoryUtils.instantiate(constructor, arguments.toArray());
    }

    private List<Object> extractArgumentsOf(final Constructor<?> constructor) {
        List<Object> arguments = new ArrayList<>();
        for (Class<?> parameter : constructor.getParameterTypes()) {
            Class<?> concreteParameter = BeanFactoryUtils.findConcreteClass(parameter, preInstantiateBeans);

            if (isUnregisteredBean(concreteParameter)) {
                registerBean(concreteParameter);
            }
            arguments.add(beans.get(concreteParameter));
        }
        return arguments;
    }

    private boolean isUnregisteredBean(final Class<?> concreteParameter) {
        return !beans.containsKey(concreteParameter);
    }
}
