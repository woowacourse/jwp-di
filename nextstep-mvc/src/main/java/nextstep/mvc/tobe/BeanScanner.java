package nextstep.mvc.tobe;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BeanScanner {
    private Reflections reflections;
    private List<Class<? extends Annotation>> types = List.of(Controller.class, Service.class, Repository.class);


    BeanScanner(Object[] basePackage) {
        reflections = new Reflections(basePackage);
    }

    Set<Class<?>> scanBeans() {
        return types.stream()
                .map(type -> reflections.getTypesAnnotatedWith(type))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
