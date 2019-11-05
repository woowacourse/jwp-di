package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.entrySet().stream()
                .filter(entry -> entry.getKey().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        initializeNotInjectedBeans();
        initializeInjectedBeans();
    }

    private void initializeNotInjectedBeans() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            Class concreteClass = findConcreteClass(preInstantiateBean);
            Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

            if (injectedConstructor == null) {
                beans.put(preInstantiateBean, createInstance(getDefaultConstructor(concreteClass)));
            }
        }
    }

    private void initializeInjectedBeans() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            initializeInjectedBean(preInstantiateBean);
        }
    }

    @SuppressWarnings("unchecked")
    private Constructor getDefaultConstructor(Class concreteClass) {
        try {
            return concreteClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }

    private void initializeInjectedBean(Class clazz) {
        if (beans.containsKey(clazz)) {
            return;
        }

        Class concreteClass = findConcreteClass(clazz);
        Object injectedBean = createInjectedInstance(concreteClass);

        beans.put(concreteClass, injectedBean);
    }

    private Object createInjectedInstance(Class concreteClass) {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        assert injectedConstructor != null;
        List<Object> parameters = prepareParameterBeans(injectedConstructor);

        return createInstance(injectedConstructor, parameters.toArray());
    }

    private List<Object> prepareParameterBeans(Constructor<?> injectedConstructor) {
        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : injectedConstructor.getParameterTypes()) {
            Class parameter = findConcreteClass(parameterType);

            if (!beans.containsKey(parameter)) {
                initializeInjectedBean(parameter);
            }
            parameters.add(beans.get(parameter));
        }

        return parameters;
    }

    private void initializeInjectedBean2(Class clazz) {
        if (beans.containsKey(clazz)) {
            return;
        }

        Class concreteClass = findConcreteClass(clazz);
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : injectedConstructor.getParameterTypes()) {
            Class parameter = findConcreteClass(parameterType);

            if (!beans.containsKey(parameter)) {
                initializeInjectedBean2(parameter);
            }
            parameters.add(beans.get(parameter));
        }

        Object injectedBean = createInstance(injectedConstructor, parameters.toArray());
        beans.put(concreteClass, injectedBean);
    }

    private Class findConcreteClass(Class<?> clazz) {
        return BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans);
    }

    private Object createInstance(Constructor constructor, Object... parameters) {
        try {
            return constructor.newInstance(parameters);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            throw new BeanCreationFailException(e);
        }
    }

}
