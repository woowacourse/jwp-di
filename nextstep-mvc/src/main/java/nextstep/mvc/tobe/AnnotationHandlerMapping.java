package nextstep.mvc.tobe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.mvc.HandlerMapping;
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

    private BeanFactory beanFactory;
    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beanFactory.initialize();
    }

    public void initialize() {
        Set<Method> methods = getRequestMappingMethods(beanFactory.getControllers());
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            logger.debug("register handlerExecution : url is {}, request method : {}, method is {}",
                    requestMapping.value(), requestMapping.method(), method);
            addHandlerExecutions(method, requestMapping);
        }

        logger.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        handlerKeys.forEach(handlerKey ->
                handlerExecutions.put(
                        handlerKey,
                        new HandlerExecution(beanFactory.getBean(method.getDeclaringClass()), method)));
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
    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> requestMappingMethods = Sets.newHashSet();
        for (Class<?> clazz : controllers) {
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
