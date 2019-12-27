package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Optional<Object>> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType).orElseThrow(NotFoundBeanException::new);
    }

    public void initialize() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Class<?> preInstantiateBean : preInstanticateBeans) {
            createBean(preInstantiateBean);
        }
    }

    private Optional<Object> createBean(Class<?> preInstantiateBean) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        if (beans.containsKey(preInstantiateBean)) {
            return Optional.of(beans.get(preInstantiateBean));
        }

        if (preInstantiateBean.isAnnotationPresent(Configuration.class)) {
            createConfiguratinBeans(preInstantiateBean);
        }

        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);
        Object bean;
        try {
            bean = (constructor == null) ? createNonConstructorBean(preInstantiateBean) : createConstructorBean(constructor);
            beans.put(preInstantiateBean, Optional.of(bean));
            return Optional.of(bean);
        } catch (
                InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    Object createConfiguratinBeans(Class<?> preInstantiateBean) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Method[] methods = preInstantiateBean.getMethods();
        List<Method> beanMethods = findBeanAnnotationMethod(methods);

        for (Method method : beanMethods) {
            createConfigurationBean(beanMethods, method);
        }

        return Optional.empty();
    }

    Object createConfigurationBean(List<Method> methods, Method method) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object obj = method.getDeclaringClass().newInstance();

        Class<?> clazz = method.getReturnType();
        if (beans.get(clazz) != null) {
            return beans.get(clazz);
        }
        if (method.getParameterCount() == 0) {
            Object bean = method.invoke(obj);
            beans.put(clazz, Optional.of(bean));
            return Optional.of(bean);
        }
        List<Optional<Object>> methodParameters = createMethodParameters(methods, method);
        Object[] args = methodParameters.stream()
                .map(methodParameter -> methodParameter.orElseThrow(NotFoundBeanException::new)).toArray();
        Object bean = method.invoke(obj, args);
        beans.put(clazz, Optional.of(bean));

        return Optional.empty();
    }

    private List<Method> findBeanAnnotationMethod(Method[] methods) {
        List<Method> beanMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                beanMethods.add(method);
            }
        }
        return beanMethods;
    }

    private List<Optional<Object>> createMethodParameters(List<Method> methods, Method method) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Optional<Object>> parameters = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if (beans.get(parameterType) == null) {
                Method paramMethod = findBeanMethodReturn(methods, parameterType);
                Optional<Object> bean = Optional.of(createConfigurationBean(methods, paramMethod));
                beans.put(parameterType, bean);
                parameters.add(bean);
            } else {
                parameters.add(beans.get(parameterType));
            }
        }
        return parameters;
    }

    private Object createConstructorBean(Constructor constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Optional<Object>> parameters = createConstructorParameters(constructor);
        Object[] params = parameters.stream()
                .map(parameter -> parameter.orElseThrow(NotFoundBeanException::new))
                .toArray();
        return constructor.newInstance(params);
    }

    private Object createNonConstructorBean(Class<?> preInstantiateBean) throws InstantiationException, IllegalAccessException {
        return BeanFactoryUtils.findConcreteClass(preInstantiateBean, preInstanticateBeans).newInstance();
    }

    private List<Optional<Object>> createConstructorParameters(Constructor constructor) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Optional<Object>> parameters = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if (beans.get(parameterType) == null) {
                Optional<Object> bean = createBean(parameterType);
                beans.put(parameterType, bean);
                parameters.add(bean);
            } else {
                parameters.add(beans.get(parameterType));
            }
        }
        return parameters;
    }

    private Method findBeanMethodReturn(List<Method> methods, Class<?> returnType) {
        return methods.stream()
                .filter(method -> method.getReturnType().equals(returnType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Map<Class<?>, Object> getBeanByAnnotation(Class<? extends Annotation> clazz) {
        return beans.keySet()
                .stream()
                .filter(type -> type.isAnnotationPresent(clazz))
                .collect(Collectors.toMap(type -> type, type -> beans.get(type).orElseThrow(NotFoundBeanException::new)));
    }
}
