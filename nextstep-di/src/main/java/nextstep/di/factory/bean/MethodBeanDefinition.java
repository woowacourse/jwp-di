package nextstep.di.factory.bean;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private Method method;
    private Class<?> beanClass;
    private Object methodDeclaringObject;

    public MethodBeanDefinition(Method method) {
        this.method = method;
        beanClass = method.getReturnType();
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public Class<?>[] getBeanParameterClasses() {
        return method.getParameterTypes();
    }

    @Override
    public Constructor<?> getInjectedConstructor() {
        return null;
    }

    public Object getMethodDeclaringObject() {
        return BeanUtils.instantiateClass(method.getDeclaringClass()) ;
    }

    public Method getMethod() {
        return this.method;
    }
}
