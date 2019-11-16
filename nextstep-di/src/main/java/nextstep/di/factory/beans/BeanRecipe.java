package nextstep.di.factory.beans;

public interface BeanRecipe {
    Object bakeBean(Object... params);

    Class<?> getBeanType();

    Class<?>[] getBeanParamTypes();
}
