package nextstep.di.factory.scanner;

import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class BeanScannerTest {
    private static final Class[] AVAILABLE_ANNOTATIONS = {Controller.class, Service.class, Repository.class};

    @Test
    void bean_스캐닝() {
        Scanner scanner = new ClassPathBeanScanner("samples");
        Set<BeanDefinition> beanDefinitions = scanner.getBeanDefinitions();
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Annotation[] annotations = beanDefinition.getClass().getAnnotations();
            assertThatCode(() -> checkBeans(annotations)).doesNotThrowAnyException();
        }
    }

    @Test
    void 스캔_되지_않은_클래스_확인() {
        Scanner scanner = new ClassPathBeanScanner("samples");
        Set<BeanDefinition> beanDefinitions = scanner.getBeanDefinitions();
        assertThat(beanDefinitions.contains((BeanDefinition) () -> NotAnnotated.class)).isFalse();
    }

    private void checkBeans(Annotation[] annotations) {
        Arrays.stream(annotations)
                .filter(annotation -> Arrays.asList(AVAILABLE_ANNOTATIONS).contains(annotation.annotationType()))
                .findFirst()
                .orElseThrow(() -> new AnnotationNotFoundException("Error: Annotation not available"));
    }
}