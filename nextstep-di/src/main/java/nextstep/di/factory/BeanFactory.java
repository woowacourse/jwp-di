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

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<BeanDefinition> beanDefinitions = Sets.newHashSet();

    public BeanFactory(Object... basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        this.preInstanticateBeans = beanScanner.getTypesAnnotated();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        if (!beans.containsKey(requiredType)) {
            registerBean(requiredType);
        }
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            registerBean(preInstanticateBean);
        }
    }

    public void initialize2() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition);
        }
    }

    private void registerBean(Class<?> preInstanticateBean) {
        BeanDefinition beanDefinition = beanDefinitions.stream()
                .filter(bd -> bd.getBeanClass().equals(preInstanticateBean))
                .findAny()
                .orElseThrow(() -> new RuntimeException("존재하지 않는 빈 입니다."));

        registerBean(beanDefinition);
    }

    private void registerBean(BeanDefinition beanDefinition) {
        if (beans.containsKey(beanDefinition.getBeanClass())) {
            return;
        }

        if (beanDefinition instanceof MethodBeanDefinition) {
            createMethodBean((MethodBeanDefinition) beanDefinition);
        }
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

    private void validateClassType(Class<?> preInstanticateBean) {
        Class<?> requiredType = BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans);
        
        if (!preInstanticateBeans.contains(requiredType)) {
            logger.error("해당 패키지에 존재하지 않는 클래스 입니다.");
            throw new IllegalArgumentException("해당 클래스를 찾을 수 없습니다.");
        }
    }

    private Object createBean(Class<?> preInstanticateBean) {
        try {
            if (preInstanticateBean.isInterface()) {
                return createBean(BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans));
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
