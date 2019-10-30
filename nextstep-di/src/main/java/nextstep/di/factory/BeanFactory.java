package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            Class concreteClass = BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans);

            Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
            if (injectedConstructor == null) {
                try {
                    beans.put(preInstanticateBean, concreteClass.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            initializeInjectedBeans(preInstanticateBean);
        }
    }

    private void initializeInjectedBeans(Class clazz) {
        Class concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);

        if (beans.containsKey(clazz)) {
            return;
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : injectedConstructor.getParameterTypes()) {
            Class concrete = BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateBeans);

            if (!beans.containsKey(concrete)) {
                initializeInjectedBeans(concrete);
            }

            parameters.add(beans.get(concrete));
        }

        try {
            beans.put(concreteClass, injectedConstructor.newInstance(parameters.toArray()));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }
}
