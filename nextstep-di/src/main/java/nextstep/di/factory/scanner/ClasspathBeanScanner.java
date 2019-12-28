package nextstep.di.factory.scanner;

import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.definition.ConstructorDefinition;
import nextstep.di.factory.exception.DuplicateBeanException;
import org.reflections.Reflections;

import java.util.*;

public class ClasspathBeanScanner implements BeanScanner {
    private Reflections reflections;
    private List<Class> annotations;

    public ClasspathBeanScanner(List<Class> annotations, Object... basePackage) {
        this.annotations = annotations;
        this.reflections = new Reflections(basePackage);
    }

    @Override
    public Map<Class<?>, BeanDefinition> scanBeans() {
        Map<Class<?>, BeanDefinition> maps = new HashMap<>();
        for (Class<?> preInitiatedBean : scanClassesByAnnotation()) {
            maps.put(preInitiatedBean, new ConstructorDefinition(preInitiatedBean));
        }
        return maps;
    }

    private Set<Class<?>> scanClassesByAnnotation() {
        Set<Class<?>> preInitiatedBeans = new HashSet<>();
        for (Class clazz : annotations) {
            Set scannedClasses = reflections.getTypesAnnotatedWith(clazz);
            checkDuplicateClass(preInitiatedBeans, scannedClasses);

            preInitiatedBeans.addAll(scannedClasses);
        }
        return preInitiatedBeans;
    }

    private void checkDuplicateClass(Set<Class<?>> preInitiatedBeans, Set scannedClasses) {
        for (Object scannedClass : scannedClasses) {
            checkAlreadyExistClass(preInitiatedBeans, scannedClass);
        }
    }

    private void checkAlreadyExistClass(Set<Class<?>> preInitiatedBeans, Object scannedClass) {
        if (preInitiatedBeans.contains(scannedClass)) {
            throw new DuplicateBeanException();
        }
    }

}
