package nextstep.di.factory.definition;

public interface BeanDefinition {
    Object generateBean(Object... params);

    Class<?> getType();

    Class<?>[] getParams();
}
