package nextstep.di.bean;

public interface BeanDefinition {
    Class<?> getClazz();

    Class<?>[] getParameterTypes();

    Object instantiate(Object... parameters);
}
