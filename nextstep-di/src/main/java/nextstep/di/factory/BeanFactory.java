package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<MethodBeanDefinition> beanDefinitions = Sets.newHashSet();

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

    private void registerBean(Class<?> preInstanticateBean) {
        validateClassType(preInstanticateBean);

        if (!beans.containsKey(preInstanticateBean)) {
            beans.put(preInstanticateBean, createBean(preInstanticateBean));
        }
    }

    private void validateClassType(Class<?> preInstanticateBean) {
        Class<?> requiredType = preInstanticateBean;
        if (requiredType.isInterface()) {
            requiredType = BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans);
        }

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

    public void register(Set<MethodBeanDefinition> beanDefinitions) {
        this.beanDefinitions.addAll(beanDefinitions);
    }
}
