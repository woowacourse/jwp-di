package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            put(preInstantiateBean);
        }
    }

    private void put(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return;
        }

        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);

        if (constructor == null) {
            constructor = BeanFactoryUtils.getDefaultConstructor(preInstantiateBean);
            Object bean = BeanFactoryUtils.instantiate(constructor);
            log.info("created bean: {}", bean);
            beans.put(preInstantiateBean, bean);
            return;
        }

        List<Object> arguments = new ArrayList<>();
        for (Class<?> parameter : constructor.getParameterTypes()) {
            Class<?> concreteParameter = BeanFactoryUtils.findConcreteClass(parameter, preInstantiateBeans);
            if (!beans.containsKey(concreteParameter)) {
                put(concreteParameter);
            }

            arguments.add(beans.get(concreteParameter));
        }

        Object bean = BeanFactoryUtils.instantiate(constructor, arguments.toArray());
        log.info("created bean: {}", bean);
        beans.put(preInstantiateBean, bean);
    }
}
