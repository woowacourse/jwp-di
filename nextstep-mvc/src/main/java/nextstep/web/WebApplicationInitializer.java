package nextstep.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.lang.reflect.InvocationTargetException;

public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException, IllegalAccessException, InstantiationException, InvocationTargetException;
}
