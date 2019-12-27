package nextstep.di.beans.specification;

public interface BeanSpecification {

    Object instantiate(Object... parameter);

    Class<?> getType();

    Class<?>[] getParameterTypes();

    boolean canInterface();
}
