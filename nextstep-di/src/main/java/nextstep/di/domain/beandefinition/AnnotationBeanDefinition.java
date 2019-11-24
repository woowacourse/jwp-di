package nextstep.di.domain.beandefinition;

import nextstep.di.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class AnnotationBeanDefinition implements BeanDefinition {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationBeanDefinition.class);

    private final Constructor<?> constructor;
    private final List<Class<?>> parameters;
    private Class<?> clazz;

    public AnnotationBeanDefinition(Class<?> clazz, Constructor<?> constructor, Class<?>... parameters) {
        this.clazz = clazz;
        this.constructor = constructor;
        this.parameters = Arrays.asList(parameters);
    }

    @Override
    public Object makeInstance(Object... parameters) {
        logger.debug("BeanDefinition {} 에서 생성한다. parameters : {}",clazz, parameters);
        return ReflectionUtils.newInstance(constructor, parameters);
    }

    @Override
    public List<Class<?>> getParameters() {
        return parameters;
    }

    @Override
    public Class<?> getBeanType() {
        return clazz;
    }
}
