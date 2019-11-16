package nextstep.di.factory;

public interface BeanRecipe {
    Object bakeBean(Object... params);

    Class<?> getBeanType();

    Class<?>[] getBeanParamTypes();
}
