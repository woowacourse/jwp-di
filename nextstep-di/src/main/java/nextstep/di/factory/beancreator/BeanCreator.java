package nextstep.di.factory.beancreator;

import java.util.List;

public interface BeanCreator {
    List<Class<?>> getParams();

    Object instantiate(Object... params);
}
