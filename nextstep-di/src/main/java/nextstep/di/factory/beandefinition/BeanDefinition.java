package nextstep.di.factory.beandefinition;

public interface BeanDefinition {
    Class<?> getClassType();

    Class<?>[] getParameterTypes();

    Object instantiate(Object... parameters);
}
