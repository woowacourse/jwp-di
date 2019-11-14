package nextstep.di.factory.definition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class FactoryMethodBeanDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(FactoryMethodBeanDefinition.class);

    private Object instance;
    private Method factoryMethod;

    public FactoryMethodBeanDefinition(Object instance, Method factoryMethod) {
        this.instance = instance;
        this.factoryMethod = factoryMethod;
    }

    @Override
    public Class<?> getName() {
        return factoryMethod.getReturnType();
    }

    @Override
    public Object createBean() {
        try {
            return factoryMethod.invoke(instance);
        } catch (Exception e) {
            log.debug("Bean Creation Exception : ", e);
            throw new RuntimeException(e);
        }
    }
}
