package nextstep.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.ApplicationContext;
import nextstep.mvc.HandlerMapping;
import nextstep.stereotype.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.reflections.ReflectionUtils;
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

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();
    private ApplicationContext ac;

    public AnnotationHandlerMapping(ApplicationContext ac) {
        this.ac = ac;
    }

    @SuppressWarnings("unchecked")
    public void initialize() {
        Set<Class<?>> controllers = ac.getBeanTypes(clazz -> clazz.isAnnotationPresent(Controller.class));
        Set<Method> methods = getRequestMappingMethods(controllers);

        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            logger.debug("register handlerExecution : url is {}, request method : {}, method is {}",
                    rm.value(), rm.method(), method);
            addHandlerExecutions(method, rm);
        }

        logger.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Method method, RequestMapping rm) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(rm.value(), rm.method());
        handlerKeys.forEach(handlerKey -> {
            handlerExecutions.put(handlerKey, new HandlerExecution(ac.getBean(method.getDeclaringClass()), method));
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

    @SuppressWarnings("unchecked")
    private Set<Method> getRequestMappingMethods(Set<Class<?>> controlleers) {
        Set<Method> requestMappingMethods = Sets.newHashSet();
        for (Class<?> clazz : controlleers) {
            requestMappingMethods
                    .addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return requestMappingMethods;
    }


    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        logger.debug("requestUri : {}, requestMethod : {}", requestUri, rm);
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
