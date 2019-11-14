package slipp;

import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.GenericBeanFactory;
import nextstep.di.factory.domain.scanner.ClassPathScanner;
import nextstep.di.factory.domain.scanner.ConfigurationScanner;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.config.JdbcConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SlippWebApplicationInitializer  implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        BeanFactory beanFactory = new GenericBeanFactory();
        ClassPathScanner classPathScanner = new ClassPathScanner(beanFactory);
        classPathScanner.scan("slipp");
        ConfigurationScanner configurationScanner = new ConfigurationScanner(beanFactory);
        configurationScanner.register(JdbcConfiguration.class);

        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(beanFactory));
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }
}