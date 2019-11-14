package slipp;

import nextstep.di.factory.AnnotationConfigApplicationContext;
import nextstep.di.factory.ApplicationContext;
import nextstep.di.factory.MvcApplicationContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SlippWebApplicationInitializer  implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        MvcApplicationContext context = new AnnotationConfigApplicationContext("slipp");

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
//        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("slipp.controller"));
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(context.getControllers()));

        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }
}