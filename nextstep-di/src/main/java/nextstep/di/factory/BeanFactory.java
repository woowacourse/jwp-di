package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Map<Class<?>, Object> preInstanticateClazz = Maps.newHashMap();

    public void registerBean(Map<Class<?>, Constructor> beans) {
        preInstanticateClazz.putAll(beans);
    }

    public void registerConfigBean(Map<Class<?>, Method> configs) {
        preInstanticateClazz.putAll(configs);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            scanBean(preInstanticateBean);
        }
    }

    private Object scanBean(Class<?> preInstanticateBean) {
        if (beans.containsKey(preInstanticateBean)) {
            return beans.get(preInstanticateBean);
        }

        if (preInstanticateClazz.get(preInstanticateBean) instanceof Method) {
            Method method = (Method) preInstanticateClazz.get(preInstanticateBean);

            if (method.getParameterCount() == 0) {
                try {
                    Object instance = method.getDeclaringClass().newInstance();
                    beans.put(preInstanticateBean, method.invoke(instance));
                    logger.debug("config bean name : {}, instance : {}", preInstanticateBean, instance);
                    return beans.get(preInstanticateBean);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    logger.error(e.getMessage());
                }
            }

            return putParameterizedConfigureObject(preInstanticateBean, method);
        } else {
            Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);

            if (Objects.isNull(constructor)) {
                Object instance = BeanUtils.instantiateClass(preInstanticateBean);
                beans.put(preInstanticateBean, instance);
                logger.debug("bean name : {}, instance : {}", preInstanticateBean, instance);
                return beans.get(preInstanticateBean);
            }

            return putParameterizedObject(preInstanticateBean, constructor);
        }
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

    private Object putParameterizedObject(Class<?> preInstanticateBean, Constructor<?> constructor) {
        Object[] params = getConstructorParams(constructor);
        Object instance = BeanUtils.instantiateClass(constructor, params);
        beans.put(preInstanticateBean, instance);
        logger.debug("bean name : {}, instance : {}", preInstanticateBean, instance);
        return instance;
    }

    private Object[] getMethodParams(Method method) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            params[i] = scanBean(parameterType);
        }
        return params;
    }

    private Object[] getConstructorParams(Constructor<?> constructor) {
        Object[] params = new Object[constructor.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = constructor.getParameterTypes()[i];
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateClazz);
            params[i] = scanBean(concreteClass);
        }
        return params;
    }

    public Map<Class<?>, Object> getAnnotatedWith(Class<? extends Annotation> annotation) {
        Map<Class<?>, Object> annotatedClass = Maps.newHashMap();
        for (Class<?> clazz : beans.keySet()) {
            if (clazz.isAnnotationPresent(annotation)) {
                annotatedClass.put(clazz, beans.get(clazz));
            }
        }
        return annotatedClass;
    }
}

