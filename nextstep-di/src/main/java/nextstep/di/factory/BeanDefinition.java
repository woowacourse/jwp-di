package nextstep.di.factory;

public interface BeanDefinition {
    Object instantiate(Object... parameter);

    Class<?>[] getParameterTypes();
}
