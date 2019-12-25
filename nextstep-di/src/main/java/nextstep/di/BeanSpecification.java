package nextstep.di;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;

public interface BeanSpecification {

    Object instantiate(Object... parameter) throws InvocationTargetException, IllegalAccessException;

    Class<?> getType();

    Class<?>[] getParameterTypes();
}
