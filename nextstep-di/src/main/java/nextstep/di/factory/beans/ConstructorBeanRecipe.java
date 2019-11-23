package nextstep.di.factory.beans;

import nextstep.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.reflections.ReflectionUtils.withAnnotation;

public class ConstructorBeanRecipe implements BeanRecipe {
    private static final Logger log = LoggerFactory.getLogger(ConstructorBeanRecipe.class);
    private Constructor<?> constructor;
    private Class<?> beanType;
    private Class<?>[] params;

    public ConstructorBeanRecipe(Class<?> beanType) {
        this.beanType = beanType;
        this.constructor = resolveConstructor(beanType);
        this.params = constructor.getParameterTypes();
    }

    private Constructor<?> resolveConstructor(Class<?> clazz) {
        List<Constructor<?>> cons = Arrays.asList(clazz.getConstructors());
        if (cons.size() == 1) {
            return cons.get(0);
        }

        return cons.stream()
                .filter(withAnnotation(Inject.class))
                .findAny()
                .orElseThrow(() -> new IllegalBeanException("@Inject 애너테이션이 둘 이상 존재합니다."));
    }

    @Override
    public Object bakeBean(Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new FailToCreateBeanException(e);
        }
    }

    @Override
    public Class<?> getBeanType() {
        return beanType;
    }

    @Override
    public Class<?>[] getBeanParamTypes() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstructorBeanRecipe that = (ConstructorBeanRecipe) o;
        return Objects.equals(beanType, that.beanType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanType);
    }
}
