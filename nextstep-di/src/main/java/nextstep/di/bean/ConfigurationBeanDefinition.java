package nextstep.di.bean;

import nextstep.di.factory.exception.InvalidBeanTargetException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConfigurationBeanDefinition implements BeanDefinition {
    private Object target;
    private Class<?> clazz;
    private Method method;

    public ConfigurationBeanDefinition(Class<?> clazz, Method method) {
        // method invoke 할 때 필요한것은 메서드의 class type 이 아니라 @Configuration 의 class type
        this.target = initTarget(clazz);
        // ConfigurationBean 에서의 @Bean 의 타입은 메서드의 반환 타입
        this.clazz = method.getReturnType();
        this.method = method;
    }

    private Object initTarget(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InvalidBeanTargetException("bean을 생성할 수 없습니다");
        }
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public Object invoke(Object[] objects) {
        try {
            return method.invoke(target, objects);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new InvalidBeanTargetException("bean 을 생성할 수 없습니다");
        }
    }

    @Override
    public Class<?>[] getParametersType() {
        return method.getParameterTypes();
    }
}
