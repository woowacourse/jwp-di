package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class DefaultBeanDefinition extends BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(DefaultBeanDefinition.class);

    public DefaultBeanDefinition(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    public Object instantiate(Object... parameterBeans) {
        try {
            Constructor<?> constructor = getConstructor(getBeanClass());
            return constructor.newInstance(parameterBeans);
        } catch (Exception e) {
            log.error("Bean create Fail : ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        try {
            return getConstructor(getBeanClass()).getParameterTypes();
        } catch (Exception e) {
            log.error("생성자를 찾을 수 없습니다. : ", e);
            throw new RuntimeException(e);
        }
    }

    private Constructor<?> getConstructor(Class<?> beanClass) throws NoSuchMethodException {
        Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(beanClass);
        if (injectedConstructor == null) {
            return beanClass.getDeclaredConstructor();
        }
        return injectedConstructor;
    }
}
