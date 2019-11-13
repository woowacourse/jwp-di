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

    private Map<Class<?>, Object> integratedBeans;
    private Map<Class<?>, Object> preInstanticateClazz;

    public BeanFactory() {
        integratedBeans = Maps.newHashMap();
        preInstanticateClazz = Maps.newHashMap();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) integratedBeans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            scanBean(preInstanticateBean);
        }
    }

    private Object scanBean(Class<?> preInstanticateBean) {
        if (integratedBeans.containsKey(preInstanticateBean)) {
            return integratedBeans.get(preInstanticateBean);
        }

        if (preInstanticateClazz.get(preInstanticateBean) instanceof Method) {
            Method method = (Method) preInstanticateClazz.get(preInstanticateBean);

            if (method.getParameterCount() == 0) {
                try {
                    Object instance = method.getDeclaringClass().newInstance();
                    integratedBeans.put(preInstanticateBean, method.invoke(instance));
                    logger.debug("config bean name : {}, instance : {}", preInstanticateBean, instance);
                    return integratedBeans.get(preInstanticateBean);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return putParameterizedConfigureObject(preInstanticateBean, method);
        } else {
            Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);

            if (Objects.isNull(injectedConstructor)) {
                Object instance = BeanUtils.instantiateClass(preInstanticateBean);
                integratedBeans.put(preInstanticateBean, instance);
                logger.debug("bean name : {}, instance : {}", preInstanticateBean, instance);
                return integratedBeans.get(preInstanticateBean);
            }

            return putParameterizedObject(preInstanticateBean, injectedConstructor);
        }
    }

    private Object putParameterizedConfigureObject(Class<?> preInstanticateConfig, Method method) {
        try {
            Object[] params = getMethodParams(method);
            Object instance = method.getDeclaringClass().newInstance();
            integratedBeans.put(preInstanticateConfig, method.invoke(instance, params));
            return integratedBeans.get(preInstanticateConfig);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("메서드 초기화 실패");
        }
    }

    private Object[] getMethodParams(Method method) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            params[i] = scanBean(parameterType);
        }
        return params;
    }

    private Object putParameterizedObject(Class<?> preInstanticateBean, Constructor<?> constructor) {
        Object[] params = getConstructorParams(constructor);
        Object instance = BeanUtils.instantiateClass(constructor, params);
        integratedBeans.put(preInstanticateBean, instance);
        logger.debug("bean name : {}, instance : {}", preInstanticateBean, instance);
        return instance;
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
        for (Class<?> clazz : integratedBeans.keySet()) {
            if (clazz.isAnnotationPresent(annotation)) {
                annotatedClass.put(clazz, integratedBeans.get(clazz));
            }
        }
        return annotatedClass;
    }

    public void addPreInstanticateClazz(Map<Class<?>, Constructor> beans) {
        preInstanticateClazz.putAll(beans);
        initialize();
    }

    public void registerBean(Map<Class<?>, Method> configs) {
        preInstanticateClazz.putAll(configs);
    }
}

