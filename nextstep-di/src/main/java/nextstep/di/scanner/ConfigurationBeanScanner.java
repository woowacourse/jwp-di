package nextstep.di.scanner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nextstep.annotation.Bean;
import nextstep.annotation.Configuration;
import nextstep.di.factory.BeanFactory;
import nextstep.exception.DuplicatedBeansException;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class ConfigurationBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationBeanScanner.class);

    private BeanFactory beanFactory;
    private Set<Class<?>> configClasses = Sets.newHashSet();

    public ConfigurationBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void scanBeans(Object... basePackage) {
        checkEmptyBasePackage(basePackage);

        Reflections reflections = new Reflections(basePackage);
        configClasses = reflections.getTypesAnnotatedWith(Configuration.class);

        List<Method> methodsWithAnnotation = findAllMethodsWithAnnotation(configClasses, Bean.class);
        checkDuplicatedReturnType(methodsWithAnnotation);

        beanFactory.appendMethodsOfPreInstantiatedBeans(methodsWithAnnotation);
        log.info("Scan Beans : {}", this.getClass().getName());
    }

    private List<Method> findAllMethodsWithAnnotation(Set<Class<?>> configClasses, Class<? extends Annotation> annotation) {
        List<Method> methodsWithAnnotation = Lists.newArrayList();
        configClasses.forEach(clazz ->
                methodsWithAnnotation.addAll(ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(annotation))));
        return methodsWithAnnotation;
    }

    private void checkDuplicatedReturnType(List<Method> methods) {
        Set<Class<?>> returnTypeOfMethod = Sets.newHashSet();

        for (Method method : methods) {
            Class<?> clazz = method.getReturnType();
            if (returnTypeOfMethod.contains(clazz)) {
                throw new DuplicatedBeansException(clazz.getName());
            }
            returnTypeOfMethod.add(clazz);
        }
    }
}
