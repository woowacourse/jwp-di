package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class DefaultBeanDefinition extends BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanDefinition.class);

    public DefaultBeanDefinition(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public Object instantiate(Object... parameterBeans) {
        try {
            Class<?> beanClass = super.getBeanClass();
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
            Constructor<?> constructor = getConstructor(getBeanClass());
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
