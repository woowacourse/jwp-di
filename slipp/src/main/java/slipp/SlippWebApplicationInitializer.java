package slipp;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.BeanScanner;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Map;
import java.util.Set;

public class SlippWebApplicationInitializer  implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        BeanScanner beanScanner = new BeanScanner("slipp.controller");
        Set<Class<?>> preInstantiatedClazz = beanScanner.getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        BeanFactory beanFactory = new BeanFactory(preInstantiatedClazz);
        beanFactory.initialize();

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        Map<Class<?>, Object> beans = beanFactory.getBeans();
        dispatcherServlet.addHandlerMpping(new AnnotationHandlerMapping(beans));

        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }
}