package slipp;

import nextstep.di.context.ApplicationBeanContext;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.configuration.MyConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SlippWebApplicationInitializer implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ApplicationBeanContext applicationBeanContext = new ApplicationBeanContext(MyConfiguration.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMpping(new AnnotationHandlerMapping(applicationBeanContext));

        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }
}