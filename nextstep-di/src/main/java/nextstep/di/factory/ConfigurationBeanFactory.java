package nextstep.di.factory;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class ConfigurationBeanFactory {
    private Map<Class<?>, Object> beans = Maps.newHashMap();
    private Class<?>[] classes;

    public ConfigurationBeanFactory(Class<?>... classes) {
        this.classes = classes;
    }

    public void initialize() {
        for (Class<?> clazz : classes) {
            instantiateBeans(clazz);
        }
    }

    private void instantiateBeans(Class<?> clazz) {
        Method[] methods = BeanFactoryUtils.getHavingBeanAnnotation(clazz);
        Object instance = ReflectionUtils.newInstance(clazz);
        for (Method method : methods) {
            getBean(instance, method);
        }
    }

    private void getBean(Object instance, Method method) {
        Class<?> returnType = method.getReturnType();
        if (isVoidType(returnType)) {
            // TODO: 2019-11-13 Exception!!
            throw new RuntimeException("void 못함!");
        }
        Object[] parameters = getParameterBeans(method);
        beans.put(returnType, ReflectionUtils.invoke(method, instance, parameters));
    }

    private boolean isVoidType(Class<?> returnType) {
        return returnType == void.class;
    }

    private Object[] getParameterBeans(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(type -> beans.get(type))
                .toArray(Object[]::new);
    }

    public Object getBean(Class<?> clazz) {
        return beans.get(clazz);
    }
}
