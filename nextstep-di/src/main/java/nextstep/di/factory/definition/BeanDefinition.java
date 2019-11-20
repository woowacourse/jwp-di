package nextstep.di.factory.definition;

public interface BeanDefinition {

    Class<?> getName();

    Class<?>[] getParams();

    Object createBean(Object... initArgs) throws Exception;

    boolean matchClass(Class<?> clazz);
}
