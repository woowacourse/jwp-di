package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.CreateBeanException;
import nextstep.di.factory.exception.CycleReferenceException;
import nextstep.di.factory.exception.InaccessibleConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
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

    public void initialize() {
        checkCycleReference(preInstantiateBeans);

        preInstantiateBeans.forEach(this::createBeanWithTryCatch);
        logger.debug("beans created: {}", beans);
        Set<Class<?>> configurationBeans = beans.keySet().stream()
                .filter(key -> key.isAnnotationPresent(Configuration.class))
                .collect(Collectors.toSet());
        logger.debug("configuration beans: {}", configurationBeans);
    }

    private void checkCycleReference(Set<Class<?>> preInstantiateBeans) {
        preInstantiateBeans.forEach(beanClass -> checkCycleReference(beanClass, new ArrayList<>()));
    }

    private void checkCycleReference(Class<?> beanClass, List<String> beanNames) {
        beanClass = BeanFactoryUtils.findConcreteClass(beanClass, preInstantiateBeans);
        Constructor<?> constructor = getConstructor(beanClass);
        if (beanNames.contains(constructor.getName())) {
            throw new CycleReferenceException();
        }
        beanNames.add(constructor.getName());
        Arrays.stream(constructor.getParameterTypes()).forEach(paramterBeanClass -> checkCycleReference(paramterBeanClass, beanNames));
    }

    private Object createBeanWithTryCatch(Class<?> parameter) {
        try {
            return createBean(parameter);
        } catch (Exception e) {
            throw new CreateBeanException(e);
        }
    }

    private Object createBean(Class<?> beanClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        beanClass = BeanFactoryUtils.findConcreteClass(beanClass, preInstantiateBeans);
        Constructor<?> constructor = getConstructor(beanClass);

        if (beans.keySet().contains(beanClass)) {
            return beans.get(beanClass);
        }

        return instantiate(beanClass, constructor);
    }

    private Constructor<?> getConstructor(Class<?> beanClass) {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(beanClass);
        if (constructor != null) {
            return constructor;
        }

        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();

        constructor = BeanFactoryUtils
                .findBeansConstructor(constructors, preInstantiateBeans)
                .or(() -> BeanFactoryUtils.findDefaultConstructor(constructors))
                .orElseThrow(InaccessibleConstructorException::new);

        return constructor;
    }

    private Object instantiate(Class<?> beanClass, Constructor<?> beansConstructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?>[] parameters = beansConstructor.getParameterTypes();
        List<Object> objects = Arrays.
                stream(parameters)
                .map(this::createBeanWithTryCatch).collect(Collectors.toList());

        Object newInstance = beansConstructor.newInstance(objects.toArray());

        beans.put(beanClass, newInstance);
        return newInstance;
    }

    public Map<Class<?>, Object> getBeans() {
        return Collections.unmodifiableMap(beans);
    }
}
