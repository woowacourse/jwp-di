package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
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

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() throws InstantiationException, IllegalAccessException {
        for (Class<?> preInstantiateBean : preInstanticateBeans) {
            inject(preInstantiateBean);
        }
    }

    private Object inject(Class<?> preInstantiateBean) throws IllegalAccessException, InstantiationException {
        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean);
        if (constructor == null) {
            Object bean = BeanFactoryUtils.findConcreteClass(preInstantiateBean, preInstanticateBeans).newInstance();

            beans.put(preInstantiateBean, bean);
            return bean;
        }

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> args = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if (beans.get(parameterType) == null) {
                Object bean = inject(parameterType);
                beans.put(parameterType, bean);
                args.add(bean);
            } else {
                args.add(beans.get(parameterType));
            }
        }
        try {
            Object bean = constructor.newInstance(args.toArray());
            beans.put(preInstantiateBean, bean);
            return bean;
        } catch (
                InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Class<?>, Object> getBeanByAnnotation(Class<? extends Annotation> clazz) {
        return beans.keySet()
                .stream()
                .filter(type -> type.isAnnotationPresent(clazz))
                .collect(Collectors.toMap(type -> type, type -> beans.get(type)));
    }
}
