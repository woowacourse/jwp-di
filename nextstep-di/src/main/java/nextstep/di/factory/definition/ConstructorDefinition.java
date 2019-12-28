package nextstep.di.factory.definition;

import nextstep.di.factory.BeanFactoryUtils;
import nextstep.di.factory.exception.InvalidBeanException;
import nextstep.di.factory.exception.NotFoundConstructorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConstructorDefinition implements BeanDefinition {
    private static final Logger log = LoggerFactory.getLogger(ConstructorDefinition.class);

    private Constructor constructor;

    public ConstructorDefinition(Class<?> clazz) {
        this.constructor = BeanFactoryUtils.getInjectedConstructor(clazz);
        if (this.constructor == null) {
            try {
                this.constructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                log.error("생성자를 찾을 수 없습니다.");
                throw new NotFoundConstructorException();
            }
        }
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public Object createBean(Object... objects) {
        try {
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("생성할 수 없는 빈입니다.");
            throw new InvalidBeanException();
        }
    }
}
