package nextstep.di.factory;

import nextstep.annotation.Bean;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationBeanScanner {
    private Reflections reflections;
    private List<Class> annotations;

    public ConfigurationBeanScanner(List<Class> annotations, Object... basePackage) {
        this.annotations = annotations;
        this.reflections = new Reflections(basePackage);
    }

    public List<Method> scanBeans() {
        List<Class> classInfo = findClassByAnnotation();

        List<Method> methods = new ArrayList<>();
        for (Class aClass : classInfo) {
            methods.addAll(Arrays.stream(aClass.getMethods())
                    .filter(m -> m.isAnnotationPresent(Bean.class)).collect(Collectors.toList()));
        }
        return methods;
    }

    private List<Class> findClassByAnnotation() {
        List<Class> classInfo = new ArrayList<>();
        for (Class annotation : annotations) {
            classInfo.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return classInfo;
    }

}
