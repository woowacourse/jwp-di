package nextstep.di.factory.domain;

import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.exception.BeanNotExistException;
import nextstep.di.factory.support.Beans;
import nextstep.di.factory.util.ReflectionUtils;

import java.lang.reflect.Method;

public class ConfigurationScanner {
    private Beans beans;

    public ConfigurationScanner() {
        this.beans = new Beans();
    }

    public void initialize(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Configuration.class)) {
            findBean(clazz);
            return;
        }
        throw new IllegalArgumentException("클래스에 Configuration 어노테이션이 없습니다");
    }

    private Object findBean(Class<?> configuration) {
        Method[] methods = configuration.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                Object instance = getInvoke(configuration, method);
                beans.put(method.getReturnType(), () -> instance);
            }
        }
        return null;
    }

    private Object getInvoke(Class<?> configuration, Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        if (parameters.length == 0) {
            return ReflectionUtils.invoke(method, configuration);
        }

        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = getInstance(parameters[i]);
        }

        return ReflectionUtils.invoke(method, configuration, objects);
    }

    private Object getInstance(Class<?> parameter) {
        try {
            return beans.get(parameter);
        } catch (BeanNotExistException e) {
            return ReflectionUtils.newInstance(parameter);
        }
    }

    public void scanBeanFactory(BeanFactory beanFactory) {
        beans.putAll(beanFactory);
    }
}
