package nextstep.di.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ConstructorBeanRecipe implements BeanRecipe {
    private static final Logger log = LoggerFactory.getLogger(ConstructorBeanRecipe.class);
    private Constructor<?> constructor;
    private Class<?> beanType;
    private Class<?>[] params;

    public ConstructorBeanRecipe(Class<?> beanType) {
        this.beanType = beanType;
        this.constructor = BeanFactoryUtils.getInjectedConstructor(beanType).orElseThrow(BeanCreateException::new);
        this.params = constructor.getParameterTypes();
    }

    @Override
    public Object bakeBean(Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new BeanCreateException(e);
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
