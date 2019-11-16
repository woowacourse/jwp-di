package nextstep.di.factory.beans;

public interface BeanRecipe {
    Object bakeBean(Object... params) throws FailToCreateBeanException;

    Class<?> getBeanType();

    Class<?>[] getBeanParamTypes();
}
