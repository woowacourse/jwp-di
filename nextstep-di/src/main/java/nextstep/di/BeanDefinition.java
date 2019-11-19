package nextstep.di;

public interface BeanDefinition {
    Class<?> getType();

    Class<?>[] getParameterTypes();

    boolean isType(Class<?> type);

    Object createBean(Object... params);
}
