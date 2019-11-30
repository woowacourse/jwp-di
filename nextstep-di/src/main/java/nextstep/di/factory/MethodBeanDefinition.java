package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class MethodBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(MethodBeanDefinition.class);

    private Object implementation;
    private Class<?> beanClass;
    private Method method;

    public MethodBeanDefinition(Object implementation, Class<?> beanClass, Method method) {
        this.implementation = implementation;
        this.beanClass = beanClass;
        this.method = method;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public boolean sameBeanClass(Class<?> beanClass) {
        return this.beanClass.equals(beanClass);
    }

    @Override
    public Object instantiate(Object... parameterBeans) {
        try {
            return method.invoke(implementation, parameterBeans);
        } catch (Exception e) {
            logger.error("빈을 생성할 수 없습니다. : ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }
}
