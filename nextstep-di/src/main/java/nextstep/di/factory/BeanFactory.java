package nextstep.di.factory;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            inject(preInstanticateBean);
        }

    }

    private Object inject(Class<?> preInstanticateBean) throws IllegalAccessException, InstantiationException {
        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(preInstanticateBean);
        if (constructor == null) {
            Object bean = BeanFactoryUtils.findConcreteClass(preInstanticateBean, preInstanticateBeans).newInstance();

            beans.put(preInstanticateBean, bean);
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
            beans.put(preInstanticateBean, bean);
            return bean;
        } catch (
                InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
