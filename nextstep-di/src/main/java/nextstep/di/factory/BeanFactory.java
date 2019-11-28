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

    private Set<Class<?>> preInstantiateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans) {
            createBean(preInstantiateBean);
        }
    }

    private void createBean(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return;
        }

        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean); //생성자 찾기

        try {
            Object bean = constructor == null ? createNonConstructorBean(preInstantiateBean) : createConstructorBean(constructor); //생성자를 통해 생성
            beans.put(preInstantiateBean, bean);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Object createConstructorBean(Constructor constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Object> parameters = createParameters(constructor);
        return constructor.newInstance(parameters.toArray());
    }

    private Object createNonConstructorBean(Class<?> preInstantiateBean) throws InstantiationException, IllegalAccessException {
        return BeanFactoryUtils.findConcreteClass(preInstantiateBean, preInstantiateBeans).newInstance();
    }

    private List<Object> createParameters(Constructor constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            parameterType = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans);

            if (!beans.containsKey(parameterType)) {
                createBean(parameterType);
            }
            parameters.add(beans.get(parameterType));
        }
        return parameters;
    }

    public Map<Class<?>, Object> getBeanByAnnotation(Class<? extends Annotation> clazz) {
        return beans.keySet()
                .stream()
                .filter(type -> type.isAnnotationPresent(clazz))
                .collect(Collectors.toMap(type -> type, type -> beans.get(type)));
    }
}
