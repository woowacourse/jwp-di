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
import java.util.Set;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;
    private Map<Class<?>, Method> methods = Maps.newHashMap();
    private Map<Class<?>, Object> beans = Maps.newHashMap();


    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    public BeanFactory() {

    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            injectInstantiateBean(preInstanticateBean);
        }

        for (Class<?> methodReturnClazz : methods.keySet()) {
            injectConfigurationBean(methodReturnClazz);
        }


    }

    private Object injectConfigurationBean(Class<?> methodReturnClazz) {
        if (beans.containsKey(methodReturnClazz)) {
            return beans.get(methodReturnClazz);
        }

        Method method = methods.get(methodReturnClazz);

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

    public Class<?> findInvokeMethodClazz(Class<?> returnType) {
        if (beans.containsKey(returnType)) {
            return returnType;
        }

        throw new IllegalStateException(returnType + "을 찾을 수 없습니다.");
    }

    private Object[] getMethodParams(Method method) {
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < params.length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            Class<?> invokeMethod = findInvokeMethodClazz(parameterType);
            params[i] = injectConfigurationBean(invokeMethod);
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

    private Object injectInstantiateBean(Class<?> preInstanticateBean) {
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
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateBeans);
            params[i] = injectInstantiateBean(concreteClass);
        }
        return params;
    }

    public void addPreInstanticateClazz(Set<Class<?>> preInstanticateClazz) {
        this.preInstanticateBeans = preInstanticateClazz;
    }

    public void register(Map<Class<?>, Method> configMethods) {
        methods.putAll(configMethods);
    }
}

