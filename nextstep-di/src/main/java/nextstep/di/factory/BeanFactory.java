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

    private Map<Class<?>, Object> beans;
    private Map<Class<?>, Constructor> preInstanticateClazz;

    private Map<Class<?>, Object> configBeans;
    private Map<Class<?>, Method> preInstanticateConfigs;

    public BeanFactory() {
        beans = Maps.newHashMap();
        preInstanticateClazz = Maps.newHashMap();
        configBeans = Maps.newHashMap();
        preInstanticateConfigs = Maps.newHashMap();
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateClazz.keySet()) {
            scanBean(preInstanticateBean);
        }
        for (Class<?> preInstanticateConfig : preInstanticateConfigs.keySet()) {
            scanConfig(preInstanticateConfig);
        }
        beans.putAll(configBeans);
    }

    private Object scanBean(Class<?> preInstanticateBean) {
        if (beans.containsKey(preInstanticateBean)) {
            return beans.get(preInstanticateBean);
        }

        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);

        if (Objects.isNull(injectedConstructor)) {
            Object instance = BeanUtils.instantiateClass(preInstanticateBean);
            beans.put(preInstanticateBean, instance);
            logger.debug("bean name : {}, instance : {}", preInstanticateBean, instance);
            return instance;
        }

        return putParameterizedObject(preInstanticateBean, injectedConstructor);
    }

    private Object scanConfig(Class<?> preInstanticateConfig) {
        if (configBeans.containsKey(preInstanticateConfig)) {
            return configBeans.get(preInstanticateConfig);
        }

        Method method = preInstanticateConfigs.get(preInstanticateConfig);

        if (method.getParameterCount() == 0) {
            try {
                Object instance = method.getDeclaringClass().newInstance();
                configBeans.put(preInstanticateConfig, method.invoke(instance));
                logger.debug("config bean name : {}, instance : {}", preInstanticateConfig, instance);
                return configBeans.get(preInstanticateConfig);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return putParameterizedConfigureObject(preInstanticateConfig, method);
    }

    private Object putParameterizedConfigureObject(Class<?> preInstanticateConfig, Method method) {
        try {
            Object[] params = getMethodParams(method);
            Object instance = method.getDeclaringClass().newInstance();
            configBeans.put(preInstanticateConfig, method.invoke(instance, params));
            return configBeans.get(preInstanticateConfig);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("메서드 초기화 실패");
        }
    }

    private Object[] getMethodParams(Method method) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            Class<?> invokeMethod = findConfig(parameterType);
            params[i] = scanConfig(invokeMethod);
        }
        return params;
    }

    public Class<?> findConfig(Class<?> returnType) {
        if (preInstanticateConfigs.containsKey(returnType)) {
            return returnType;
        }

        throw new IllegalStateException(returnType + "을 찾을 수 없습니다.");
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
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateClazz.keySet());
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

    public void addPreInstanticateClazz(Map<Class<?>, Constructor> beans) {
        preInstanticateClazz.putAll(beans);
        initialize();
    }

    public void registerBean(Map<Class<?>, Method> configs) {
        preInstanticateConfigs.putAll(configs);
    }
}

