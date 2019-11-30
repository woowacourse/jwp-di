package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class DefaultBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanDefinition.class);

    private Class<?> beanClass;

    public DefaultBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
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
            Constructor<?> constructor = getConstructor(beanClass);
            return constructor.newInstance(parameterBeans);
        } catch (Exception e) {
            logger.error("빈을 생성할 수 없습니다. : ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        try {
            Constructor<?> constructor = getConstructor(beanClass);
            return constructor.getParameterTypes();
        } catch (NoSuchMethodException e) {
            logger.error("파라미터를 가져올 수 없습니다. : ", e);
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> getConstructor(Class<?> beanClass) throws NoSuchMethodException {
        Constructor<?> constructor = BeanFactoryUtils.getInjectedConstructor(beanClass);

        if (constructor == null) {
            return beanClass.getDeclaredConstructor();
        }
        return constructor;
    }
}
