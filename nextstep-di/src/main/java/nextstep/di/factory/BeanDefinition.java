package nextstep.di.factory;

public interface BeanDefinition {
    Class<?>[] getParameterTypes();

    Object createBean(Object... objects);
}
