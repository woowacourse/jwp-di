package nextstep.di.factory;

import java.util.Map;

public interface MvcApplicationContext extends ApplicationContext {
    Map<Class<?>, Object> getControllers();
}
