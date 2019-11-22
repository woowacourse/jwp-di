package nextstep.di.scanner;

public interface BeanDefinition {
    Object getInstance(Object[] params);

    Class[] getParameterTypes();
}
