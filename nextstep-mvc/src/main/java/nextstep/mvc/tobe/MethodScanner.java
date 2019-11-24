package nextstep.mvc.tobe;

import com.google.common.collect.Sets;
import org.reflections.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;

public class MethodScanner {
    @SuppressWarnings("unchecked")
    public static Set<Method> scanAnnotatedMethods(Set<Class<?>> controllers, Class<? extends Annotation> annotation) {
        Set<Method> requestMappingMethods = Sets.newHashSet();
        Predicate<Method> methodPredicate = ReflectionUtils.withAnnotation(annotation)::apply;

        for (Class<?> clazz : controllers) {
            requestMappingMethods.addAll(ReflectionUtils.getAllMethods(clazz, methodPredicate::test));
        }
        return requestMappingMethods;
    }

}
