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
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Map<Class<?>, BeanDefinition> preInstantiateBeans;
    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Map<Class<?>, BeanDefinition> preInstantiateBeans) {
        this.preInstantiateBeans = preInstantiateBeans;
    }

    public void initialize() {
        for (Class<?> preInstantiateBean : preInstantiateBeans.keySet()) {
            createBean(preInstantiateBean);
        }
    }

    private void createBean(Class<?> preInstantiateBean) {
        if (beans.containsKey(preInstantiateBean)) {
            return;
        }

        BeanDefinition tempBean = preInstantiateBeans.get(preInstantiateBean);

        List<Object> parameters = createParameters(tempBean);
        beans.put(preInstantiateBean, tempBean.createBean(parameters.toArray()));

        Constructor constructor = BeanFactoryUtils.getInjectedConstructor(preInstantiateBean); //생성자 찾기

        try {
            Object bean = constructor == null ? createNonConstructorBean(preInstantiateBean) : createConstructorBean(constructor); //생성자를 통해 생성
            beans.put(preInstantiateBean, bean);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private List<Object> createParameters(BeanDefinition beanDefinition) {
        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if(beans.containsKey(parameterType)) {
               parameters.add(beans.get(parameterType));
               continue;
            }
            parameterType = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans.keySet());

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

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    private Object createConstructorBean(Constructor constructor, Object ... parameters) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return constructor.newInstance(parameters);
    }

    private Object createNonConstructorBean(Class<?> preInstantiateBean) throws InstantiationException, IllegalAccessException {
        return BeanFactoryUtils.findConcreteClass(preInstantiateBean, preInstantiateBeans.keySet()).newInstance();
    }
}
