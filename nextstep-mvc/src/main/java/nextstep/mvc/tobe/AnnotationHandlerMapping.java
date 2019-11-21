package nextstep.mvc.tobe;

import com.google.common.collect.Maps;
import nextstep.di.factory.ApplicationContext;
import nextstep.mvc.HandlerMapping;
import nextstep.stereotype.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private ApplicationContext applicationContext;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        Set<Method> methods = applicationContext.findMethodsByAnnotation(RequestMapping.class, Controller.class);
        createHandlerExecution(applicationContext, methods);

        logger.info("Initialized AnnotationHandlerMapping!");
    }

    private void createHandlerExecution(ApplicationContext applicationContext, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            logger.debug("register handlerExecution : url is {}, request method : {}, method is {}",
                    rm.value(), rm.method(), method);
            addHandlerExecutions(applicationContext, method, rm);
        }
    }

    private void addHandlerExecutions(ApplicationContext applicationContext, Method method, RequestMapping rm) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(rm.value(), rm.method());
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey,
                new HandlerExecution(applicationContext.getBean(method.getDeclaringClass()), method)));
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
