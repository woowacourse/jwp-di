package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.exception.DefaultConstructorInitException;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory() {
    }

    public void setPreInstanticateBeans(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    public Map<Class<?>, Object> getBeansWithType(Class<? extends Annotation> type) {
        return this.beans.entrySet().stream()
                .filter(bean -> bean.getKey().isAnnotationPresent(type))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> bean : preInstanticateBeans) {
            // TODO refactor

//            if (bean.isAnnotationPresent(Configuration.class)) {
//                Method[] methods = bean.getMethods();
//                Arrays.stream(methods)
//                        .filter(method -> method.isAnnotationPresent(Bean.class))
//                        .map(beanMethod -> enrollMethodBean(beanMethod));
//                continue;
//            }
            checkIfInterface(bean);
            enrollBean(bean);
        }
    }

//    private Object enrollMethodBean(Method beanMethod) {
//        List<Object> arguments = getArguments(beanMethod);
//
//        beanMethod.invoke(beanMethod, Arrays.asList(arguments));
//        return null;
//    }

    private List<Object> getArguments(Method beanMethod) {
        Parameter[] parameters = beanMethod.getParameters();
        return Arrays.stream(parameters).
                map(this::createOrGetBean)
                .collect(Collectors.toList());
    }

    private Object createOrGetBean(Parameter parameter) {
        if (beans.containsKey(parameter.getType())) {
            return beans.get(parameter.getType());
        }

        // TODO refactor
        return null;
    }

    private void checkIfInterface(Class<?> bean) {
        if (bean.isInterface()) {
            throw new InvalidBeanClassTypeException();
        }
    }

    private Object enrollBean(Class<?> clazz) {
        if (isBeanExists(clazz)) {
            return getBean(clazz);
        }
        Object object = createBeanInstance(clazz);
        beans.put(clazz, object);
        return object;
    }

    private Object createBeanInstance(Class<?> clazz) {
        Constructor<?> constructor = getConstructor(clazz);
        List<Object> paramInstances = initParameters(constructor);
        return BeanUtils.instantiateClass(constructor, paramInstances.toArray());
    }

    private boolean isBeanExists(Class<?> bean) {
        return beans.containsKey(bean);
    }

    private Constructor<?> getConstructor(Class<?> clazz) {
        Constructor injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (injectedConstructor == null) {
            return getDefaultConstructor(clazz);
        }
        return injectedConstructor;
    }

    private Constructor getDefaultConstructor(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error("Error : {0}", e);
            throw new DefaultConstructorInitException(e);
        }
    }

    private List<Object> initParameters(Constructor constructor) {
        Parameter[] parameters = constructor.getParameters();
        List<Object> paramInstances = new ArrayList<>();
        for (Parameter parameter : parameters) {
            Class<?> clazz = BeanFactoryUtils.findConcreteClass(parameter.getType(), preInstanticateBeans);
            paramInstances.add(enrollBean(clazz));
        }
        return paramInstances;
    }
}
