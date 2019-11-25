package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<BeanDefinition> beanDefinitions = Sets.newHashSet();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        if (!beans.containsKey(requiredType)) {
            registerBean(requiredType);
        }
        return (T) beans.get(findConcreteClass(requiredType));
    }

    public void initialize() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition);
        }
    }

    private void registerBean(Class<?> preInstanticateBean) {
        BeanDefinition beanDefinition = findBeanDefinition(preInstanticateBean);

        registerBean(beanDefinition);
    }

    private BeanDefinition findBeanDefinition(Class<?> concreteClass) {
        if (concreteClass.isInterface() && notExistBeanDefinition(concreteClass)) {
            return findBeanDefinition(findConcreteClass(concreteClass));
        }

        return beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.sameBeanClass(concreteClass))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private boolean notExistBeanDefinition(Class<?> concreteClass) {
        return beanDefinitions.stream()
                .noneMatch(beanDefinition -> beanDefinition.sameBeanClass(concreteClass));
    }

    private Class<?> findConcreteClass(Class<?> preInstanticateBean) {
        if (beans.containsKey(preInstanticateBean)) {
            return preInstanticateBean;
        }
        Set<Class<?>> beanClazz = this.beanDefinitions.stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        return BeanFactoryUtils.findConcreteClass(preInstanticateBean, beanClazz);
    }

    private void registerBean(BeanDefinition beanDefinition) {
        if (beans.containsKey(beanDefinition.getBeanClass())) {
            return;
        }

        if (beanDefinition instanceof MethodBeanDefinition) {
            createMethodBean((MethodBeanDefinition) beanDefinition);
            return;
        }

        createDefaultBean((DefaultBeanDefinition) beanDefinition);
    }

    private void createDefaultBean(DefaultBeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();

        beans.put(beanClass, createBean(beanClass));
    }

    private void createMethodBean(MethodBeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Object implementation = beanDefinition.getImplementation();
        Method method = beanDefinition.getMethod();

        Object[] parameterBeans = Arrays.stream(method.getParameterTypes())
                .map(this::getBean)
                .toArray();
        try {
            beans.put(beanClass, method.invoke(implementation, parameterBeans));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object createBean(Class<?> preInstanticateBean) {
        try {
            if (preInstanticateBean.isInterface()) {
                return createBean(findConcreteClass(preInstanticateBean));
            }
            return getInstance(preInstanticateBean);
        } catch (Exception e) {
            logger.error("### Bean create fail : ", e);
            throw new IllegalArgumentException("Bean create fail!");
        }
    }

    private Object getInstance(Class<?> preInstanticateBean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);
        if (injectedConstructor == null) {
            return createInstance(preInstanticateBean.getDeclaredConstructor());
        }
        return createInstance(injectedConstructor);
    }

    private Object createInstance(Constructor<?> injectedConstructor) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            parameters.add(getBean(parameterType));
        }
        return injectedConstructor.newInstance(parameters.toArray());
    }

    public Map<Class<?>, Object> getControllers() {
        return beans.values().stream()
                .filter(bean -> bean.getClass().isAnnotationPresent(Controller.class))
                .collect(Collectors.toMap(Object::getClass, bean -> bean));
    }

    public void register(Set<BeanDefinition> beanDefinitions) {
        this.beanDefinitions.addAll(beanDefinitions);
    }
}
