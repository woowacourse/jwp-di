package nextstep.di.factory;

import com.google.common.collect.Maps;
import nextstep.exception.BeanMethodNotFoundException;
import nextstep.exception.UnregisteredBeanException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurationBeanFactory {
    private final BeanDefinition beanDefinition;
    private Class<?> configureClass;
    private Map<Class<?>, Object> configurationBeans = Maps.newHashMap();

    public ConfigurationBeanFactory(final BeanDefinition beanDefinition, final Class<?> configureClass) {
        this.beanDefinition = beanDefinition;
        this.configureClass = configureClass;
    }

    public Map<Class<?>, Object> initialize(final Map<Class<?>, Object> components) {
        Set<Class<?>> preInstantiateBeans = beanDefinition.getPreInstantiateConfigurationBeans();

        preInstantiateBeans
            .forEach(preInstantiateBean -> instantiateBean(preInstantiateBean, components));

        return configurationBeans;
    }

    private Object instantiateBean(final Class<?> preInstantiateBean, final Map<Class<?>, Object> components) {
        Optional<Method> maybeBeanMethod = findBeanMethod(preInstantiateBean);
        return maybeBeanMethod.map(method -> instantiateBean(method, components))
            .orElseThrow(BeanMethodNotFoundException::new);
    }

    private Object instantiateBean(final Method method, final Map<Class<?>, Object> components) {
        List<Object> parameters = instantiateParameters(method, components);
        Object bean = new Object();
        try {
            bean = method.invoke(BeanUtils.instantiateClass(configureClass), parameters.toArray());
            configurationBeans.put(method.getReturnType(), bean);

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    private List<Object> instantiateParameters(final Method method, final Map<Class<?>, Object> components) {
        Parameter[] parameters = method.getParameters();

        return Arrays.stream(parameters)
            .map(parameter -> instantiateParameter(parameter, components))
            .collect(Collectors.toList());
    }

    private Object instantiateParameter(final Parameter parameter, final Map<Class<?>, Object> components) {
        Class<?> parameterType = parameter.getType();

        if (components.containsKey(parameterType)) {
            return components.get(parameterType);
        }

        return instantiateParameter(parameterType, components);
    }

    private Object instantiateParameter(final Class<?> concreteParameterType, final Map<Class<?>, Object> components) {
        if (configurationBeans.containsKey(concreteParameterType)) {
            return configurationBeans.get(concreteParameterType);
        }

        Set<Class<?>> preInstantiateBeans = beanDefinition.getPreInstantiateConfigurationBeans();
        if (preInstantiateBeans.contains(concreteParameterType)) {
            return instantiateBean(concreteParameterType, components);
        }

        throw new UnregisteredBeanException();
    }

    private Optional<Method> findBeanMethod(final Class<?> preInstantiateBean) {
        Method[] methods = configureClass.getMethods();

        return Arrays.stream(methods)
            .filter(method -> isExistMethod(preInstantiateBean, method))
            .findAny();
    }

    private boolean isExistMethod(final Class<?> preInstantiateBean, final Method method) {
        Class<?> returnType = method.getReturnType();
        return returnType.equals(preInstantiateBean);
    }
}
