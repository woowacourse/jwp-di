package nextstep.di;

import java.util.List;

public interface BeanInitiator {
    Object instantiate(Object... beans);

    List<Class<?>> getParameterTypes();
}
