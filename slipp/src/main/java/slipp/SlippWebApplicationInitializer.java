package slipp;

import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.BeanFactory2;
import nextstep.di.factory.domain.BeanScanner;
import nextstep.mvc.DispatcherServlet;
import nextstep.mvc.asis.ControllerHandlerAdapter;
import nextstep.mvc.tobe.AnnotationHandlerMapping;
import nextstep.mvc.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.WebApplicationInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SlippWebApplicationInitializer  implements WebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SlippWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        BeanFactory beanFactory = new BeanFactory2();
        BeanScanner beanScanner = new BeanScanner("slipp");
        beanScanner.initialize();
        beanScanner.scanBeanFactory(beanFactory);
//        BeanFactory1 beanFactory1 = new BeanFactory1(beanScanner);
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping(beanFactory));
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        log.info("Start MyWebApplication Initializer");
    }
}