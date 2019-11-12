package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.bean.ConfigurationBeanDefinition;
import nextstep.di.bean.BeanDefinition;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ConfigurationBeanScanner implements Scanner2 {
    private static final Class[] AVAILABLE_ANNOTATIONS = {Configuration.class};

    private Reflections reflections;

    @Override
    @SuppressWarnings("unchecked")
    public Set<BeanDefinition> scan(String basePackage) {
        Set<BeanDefinition> beans = new HashSet<>();
        reflections = new Reflections(basePackage);

        // Configuration 이 붙은 클래스를 가져온다
        Set<Class<?>> targets = getTypesAnnotatedWith(AVAILABLE_ANNOTATIONS);
        for (Class<?> target : targets) {
            //  Bean 이 달려있는 메서드를 가져온다
            Set<Method> methods = ReflectionUtils.getMethods(target, ReflectionUtils.withAnnotation(Bean.class));
            for (Method method : methods) {
                beans.add(new ConfigurationBeanDefinition(target, method));
            }
        }
        return beans;
    }

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}
