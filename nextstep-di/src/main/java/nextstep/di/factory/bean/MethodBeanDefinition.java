package nextstep.di.factory.bean;

import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private Method method;
    private Class<?> beanClass;

    MethodBeanDefinition(Method method) {
        this.method = method;
        beanClass = method.getDeclaringClass();
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }
}
