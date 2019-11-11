package nextstep.stereotype;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum BeanAnnotations {
    CONTROLLER_ANNOTATION(Controller.class),
    SERVICE_ANNOTATION(Service.class),
    REPOSITORY_ANNOTATION(Repository.class);

    private Class<? extends Annotation> clazz;

    BeanAnnotations(Class<? extends Annotation> clazz) {
        this.clazz = clazz;
    }

    public static Set<Class<? extends Annotation>> getClazz() {
        return Arrays.stream(values())
                .map(annotation -> annotation.clazz)
                .collect(Collectors.toSet());
    }
}
