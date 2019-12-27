package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstanticateBeans) {
            createBean(preInstantiateBean);
        }
    }

    private Optional<Object> createBean(Class<?> preInstantiateBean) {
        if(beans.containsKey(preInstantiateBean)) {
            return Optional.of(beans.get(preInstantiateBean));
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

    private Object createConstructorBean(Constructor constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Optional<Object>> parameters = createParameters(constructor);
        Object[] params = parameters.stream()
                .map(parameter -> parameter.orElseThrow(NotFoundBeanException::new))
                .toArray();
        return constructor.newInstance(params);
    }

    private Object createNonConstructorBean(Class<?> preInstantiateBean) throws InstantiationException, IllegalAccessException {
        return BeanFactoryUtils.findConcreteClass(preInstantiateBean, preInstanticateBeans).newInstance();
    }

    private List<Optional<Object>> createParameters(Constructor constructor) {
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

    public Map<Class<?>, Object> getBeanByAnnotation(Class<? extends Annotation> clazz) {
        return beans.keySet()
                .stream()
                .filter(type -> type.isAnnotationPresent(clazz))
                .collect(Collectors.toMap(type -> type, type -> beans.get(type).orElseThrow(NotFoundBeanException::new)));
    }
}
