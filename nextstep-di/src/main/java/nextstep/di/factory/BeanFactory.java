package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.di.factory.definition.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeanFactory {
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

        for (Object parameter : parameters) {
            beans.put(parameter.getClass(), parameter);
        }
        beans.put(preInstantiateBean, tempBean.createBean(parameters.toArray()));
    }

    private List<Object> createParameters(BeanDefinition beanDefinition) {
        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
        List<Object> parameters = new ArrayList<>();

        for (Class<?> parameterType : parameterTypes) {
            if (beans.containsKey(parameterType)) {
                parameters.add(beans.get(parameterType));
                continue;
            }
            parameterType = BeanFactoryUtils.findConcreteClass(parameterType, preInstantiateBeans.keySet()).orElse(parameterType);

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
}
