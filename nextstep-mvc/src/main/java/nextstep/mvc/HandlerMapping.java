package nextstep.mvc;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

public interface HandlerMapping {
    void initialize() throws IllegalAccessException, InvocationTargetException, InstantiationException;

    Object getHandler(HttpServletRequest request);
}
