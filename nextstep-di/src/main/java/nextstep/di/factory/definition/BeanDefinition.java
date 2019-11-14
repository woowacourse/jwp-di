package nextstep.di.factory.definition;

public interface BeanDefinition {

    Class<?> getName();

    Object createBean();
}
