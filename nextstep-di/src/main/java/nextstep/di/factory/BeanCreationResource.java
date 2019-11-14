package nextstep.di.factory;

import java.util.List;

public interface BeanCreationResource {
    Object initialize(Object... params);

    Class<?> getType();

    List<Class<?>> getParameterTypes();
}
