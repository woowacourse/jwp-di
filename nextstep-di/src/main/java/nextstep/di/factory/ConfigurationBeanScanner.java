package nextstep.di.factory;

import nextstep.annotation.Bean;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner implements BeanScanner{
    private Reflections reflections;
    private List<Class> annotations;

    public ConfigurationBeanScanner(List<Class> annotations, Object... basePackage) {
        this.annotations = annotations;
        this.reflections = new Reflections(basePackage);
    }

    @Override
    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> maps = new HashMap<>();
        List<Class> classInfo = findClassByAnnotation();

        List<Method> methods = new ArrayList<>();
        for (Class aClass : classInfo) {
            methods.addAll(Arrays.stream(aClass.getMethods())
                    .filter(m -> m.isAnnotationPresent(Bean.class))
                    .collect(Collectors.toList()));
        }
        for (Method method : methods) {
            maps.put(method.getReturnType(), new MethodDefinition(method));
        }
        return maps;
    }

    private List<Class> findClassByAnnotation() {
        List<Class> classInfo = new ArrayList<>();
        for (Class annotation : annotations) {
            classInfo.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return classInfo;
    }


}
