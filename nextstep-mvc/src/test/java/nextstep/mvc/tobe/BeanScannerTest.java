package nextstep.mvc.tobe;

import nextstep.di.factory.Scanner;
import nextstep.mvc.exception.AnnotationNotFoundException;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.Test;
import samples.NotAnnotated;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeanScannerTest {
    private static final Class[] AVAILABLE_ANNOTATIONS = {Controller.class, Service.class, Repository.class};

    @Test
    void bean_스캐닝() {
        Scanner scanner = new BeanScanner("samples");
        Set<Class<?>> annotatedClasses = scanner.getAnnotatedClasses();
        for (Class<?> annotatedClass : annotatedClasses) {
            Annotation[] annotations = annotatedClass.getAnnotations();
            assertThatCode(() -> checkBeans(annotations)).doesNotThrowAnyException();
        }
    }

    @Test
    void 스캔_되지_않은_클래스_확인(){
        Scanner scanner = new BeanScanner("samples");
        Set<Class<?>> scannedClasses = scanner.getAnnotatedClasses();
        assertThat(scannedClasses.contains(NotAnnotated.class)).isFalse();
    }

    private void checkBeans(Annotation[] annotations) {
        Arrays.stream(annotations)
                .filter(annotation -> Arrays.asList(AVAILABLE_ANNOTATIONS).contains(annotation.annotationType()))
                .findFirst()
                .orElseThrow(() -> new AnnotationNotFoundException("Error: Annotation not available"));
    }
}