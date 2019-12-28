package nextstep.di.factory.definition;

public interface BeanDefinition {
    Class<?>[] getParameterTypes();

    Object createBean(Object... objects);
}
