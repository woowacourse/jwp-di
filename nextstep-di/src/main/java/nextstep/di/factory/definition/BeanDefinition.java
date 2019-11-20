package nextstep.di.factory.definition;

public interface BeanDefinition {
    Class<?>[] getParameters();

    Object createBean(Object... objects);
}
