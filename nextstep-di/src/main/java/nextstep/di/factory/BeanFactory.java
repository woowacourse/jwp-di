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

    public BeanFactory() {

    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            if (preInstanticateClazz.get(preInstanticateBean) instanceof Method) {
                injectInstantiateBean2(preInstanticateBean);
            }
        }
    }

    private Object injectInstantiateBean(Class<?> methodReturnClazz) {
        if (beans.containsKey(methodReturnClazz)) {
            return beans.get(methodReturnClazz);
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(methodReturnClazz);

        if (Objects.isNull(injectedConstructor)) {
            Object instance = BeanUtils.instantiateClass(methodReturnClazz);
            beans.put(methodReturnClazz, instance);
            logger.debug("bean name : {}, instance : {}", methodReturnClazz, instance);
            return beans.get(methodReturnClazz);
        }

        return putParameterizedObject(methodReturnClazz, injectedConstructor);
    }


    private Object[] getMethodParams(Method method) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            params[i] = injectInstantiateBean(parameterType);
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

    private Object putParameterizedObject(Class<?> preInstanticateBean, Constructor<?> constructor) {
        Object[] params = getConstructorParams(constructor);
        Object instance = BeanUtils.instantiateClass(constructor, params);
        beans.put(preInstanticateBean, instance);
        logger.debug("bean name : {}, instance : {}", preInstanticateBean, instance);
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

    public void addPreInstanticateClazz(Map<Class<?>, Constructor> beans) {
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

    private Object injectInstantiateBean2(Class<?> methodReturnClazz) {
        if (beans.containsKey(methodReturnClazz)) {
            return beans.get(methodReturnClazz);
        }

        Method method = (Method) preInstanticateClazz.get(methodReturnClazz);

        if (method.getParameterCount() == 0) {
            try {
                Object instance = method.getDeclaringClass().newInstance();
                beans.put(methodReturnClazz, method.invoke(instance));
                logger.debug("config bean name : {}, instance : {}", methodReturnClazz, instance);
                return beans.get(methodReturnClazz);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return putParameterizedConfigureObject(methodReturnClazz, method);
    }

    private Object putParameterizedConfigureObject(Class<?> preInstanticateConfig, Method method) {
        try {
            Object[] params = getMethodParams(method);
            Object instance = method.getDeclaringClass().newInstance();
            beans.put(preInstanticateConfig, method.invoke(instance, params));
            return beans.get(preInstanticateConfig);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("메서드 초기화 실패");
        }
    }
}

