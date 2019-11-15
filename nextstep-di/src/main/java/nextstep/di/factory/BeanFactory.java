package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, Object> preInstanticateClazz = Maps.newHashMap();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            if (preInstanticateClazz.get(preInstanticateBean) instanceof Method) {
                injectConfigurationBean(preInstanticateBean);
            }
        }
    }

    public void putComponentBean(Map<Class<?>, Constructor> beans) {
        preInstanticateClazz.putAll(beans);

        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            if (preInstanticateClazz.get(preInstanticateBean) instanceof Constructor) {
                injectInstantiateBean(preInstanticateBean);
            }
        }
    }

    public void registerBean(Map<Class<?>, Method> configs) {
        preInstanticateClazz.putAll(configs);
    }

    private Object injectInstantiateBean(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);

        if (Objects.isNull(injectedConstructor)) {
            Object instance = BeanUtils.instantiateClass(clazz);
            beans.put(clazz, instance);
            logger.debug("bean name : {}, instance : {}", clazz, instance);
            return beans.get(clazz);
        }

        return putConstructParameterObject(clazz, injectedConstructor);
    }

    private Object putConstructParameterObject(Class<?> clazz, Constructor<?> constructor) {
        Object[] params = getConstructorParams(constructor);
        Object instance = BeanUtils.instantiateClass(constructor, params);
        beans.put(clazz, instance);
        logger.debug("bean name : {}, instance : {}", clazz, instance);
        return instance;
    }

    private Object[] getConstructorParams(Constructor<?> constructor) {
        Object[] params = new Object[constructor.getParameterCount()];

        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = constructor.getParameterTypes()[i];
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateClazz);
            params[i] = injectInstantiateBean(concreteClass);
        }
        return params;
    }

    private Object injectConfigurationBean(Class<?> returnClazz) {
        if (beans.containsKey(returnClazz)) {
            return beans.get(returnClazz);
        }

        Method method = (Method) preInstanticateClazz.get(returnClazz);

        if (method.getParameterCount() == 0) {
            try {
                Object instance = BeanUtils.instantiateClass(method.getDeclaringClass());
                beans.put(returnClazz, method.invoke(instance));
                logger.debug("config bean name : {}, instance : {}", returnClazz, instance);
                return beans.get(returnClazz);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return putMethodParameterObject(returnClazz, method);
    }

    private Object putMethodParameterObject(Class<?> returnClazz, Method method) {
        try {
            Object[] params = getMethodParams(method);
            Object instance = method.getDeclaringClass().newInstance();
            beans.put(returnClazz, method.invoke(instance, params));
            return beans.get(returnClazz);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("메서드 초기화 실패");
        }
    }

    private Object[] getMethodParams(Method method) {
        Object[] params = new Object[method.getParameterCount()];

        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            params[i] = injectConfigurationBean(parameterType);
        }
        return params;
    }

    public Map<Class<?>, Object> getController() {
        return getAnnotatedWith(Controller.class);
    }

    private Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        return beans.keySet().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .collect(Collectors.toMap(clazz -> clazz, beans::get));
    }
}

