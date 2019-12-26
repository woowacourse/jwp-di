package nextstep.di;

public interface BeanSpecification {

    Object instantiate(Object... parameter);

    Class<?> getType();

    Class<?>[] getParameterTypes();

    boolean canInterface();
}
