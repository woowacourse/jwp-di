package nextstep.mvc.tobe;

import com.google.common.collect.Maps;
import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.ClasspathBeanScanner;
import nextstep.di.factory.ConfigurationBeanScanner;
import nextstep.mvc.HandlerMapping;
import nextstep.stereotype.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Class<?>[] configurations;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Class<?>... configurations) {
        this.configurations = configurations;
    }

    public void initialize() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(configurations);
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(configurationBeanScanner.getBasePackages());

        Set<Class<?>> classTypes = Stream.of(configurationBeanScanner.getClassTypes(), classpathBeanScanner.getClassTypes())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        BeanFactory beanFactory = new BeanFactory(classTypes);

        Set<Method> methods = beanFactory.findMethodsByAnnotation(RequestMapping.class, Controller.class);
        createHandlerExecution(beanFactory, methods);

        logger.info("Initialized AnnotationHandlerMapping!");
    }

    private void createHandlerExecution(BeanFactory beanFactory, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            logger.debug("register handlerExecution : url is {}, request method : {}, method is {}",
                    rm.value(), rm.method(), method);
            addHandlerExecutions(beanFactory, method, rm);
        }
    }

    private void addHandlerExecutions(BeanFactory beanFactory, Method method, RequestMapping rm) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(rm.value(), rm.method());
        handlerKeys.forEach(handlerKey -> {
            handlerExecutions.put(handlerKey, new HandlerExecution(beanFactory.getBean(method.getDeclaringClass()), method));
        });
    }

    private List<HandlerKey> mapHandlerKeys(final String value, final RequestMethod[] originalMethods) {
        RequestMethod[] targetMethods = originalMethods;
        if (targetMethods.length == 0) {
            targetMethods = RequestMethod.values();
        }
        return Arrays.stream(targetMethods)
                .map(method -> new HandlerKey(value, method))
                .collect(Collectors.toList());
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        logger.debug("requestUri : {}, requestMethod : {}", requestUri, rm);
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
