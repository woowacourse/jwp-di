package nextstep.di.factory;

import nextstep.annotation.Inject;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import nextstep.di.factory.exception.InvalidBeanTargetException;
import nextstep.di.factory.bean.ClassPathBeanDefinition;
import nextstep.di.factory.scanner.Scanner;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    @Test
    void 클래스_어노테이션_빈_등록_성공() {
        Scanner manualScanner = () ->
                Stream.of(AnnotatedClass.class, ParameterClass.class)
                        .map(ClassPathBeanDefinition::new)
                        .collect(Collectors.toSet());
        BeanFactory beanFactory = new BeanFactory(manualScanner);
        beanFactory.initialize();
        AnnotatedClass annotatedClass = beanFactory.getBean(AnnotatedClass.class);
        assertNotNull(annotatedClass);
    }

    @Test
    void 파라미터_빈_대상_아닐시_등록_실패() {
        Scanner manualScanner = () ->
                Stream.of(AnnotatedClass.class)
                        .map(ClassPathBeanDefinition::new)
                        .collect(Collectors.toSet());
        BeanFactory beanFactory = new BeanFactory(manualScanner);
        assertThrows(NullPointerException.class, () -> {
            beanFactory.initialize();
        });
    }

    @Test
    void 없는_빈_Null() {
        Scanner manualScanner = () ->
                Stream.of(AnnotatedClass.class, ParameterClass.class)
                        .map(ClassPathBeanDefinition::new)
                        .collect(Collectors.toSet());
        BeanFactory beanFactory = new BeanFactory(manualScanner);
        beanFactory.initialize();
        assertNull(beanFactory.getBean(NotAnnotatedClass.class));
    }

    @Test
    void 빈_싱글턴_보장_여부() {
        Scanner manualScanner = () ->
                Stream.of(AnnotatedClass.class, ParameterClass.class)
                        .map(ClassPathBeanDefinition::new)
                        .collect(Collectors.toSet());
        BeanFactory beanFactory = new BeanFactory(manualScanner);
        beanFactory.initialize();
        AnnotatedClass annotatedClass = beanFactory.getBean(AnnotatedClass.class);
        assertThat(annotatedClass.getParameterClass()).isEqualTo(beanFactory.getBean(ParameterClass.class));
    }

    private interface AnnotatedInterface {
    }

    private static class AnnotatedClass {
        private ParameterClass parameterClass;

        @Inject
        public AnnotatedClass(ParameterClass parameterClass) {
            this.parameterClass = parameterClass;
        }

        ParameterClass getParameterClass() {
            return parameterClass;
        }

        private ParameterClass annotatedMethod() {
            return null;
        }
    }

    private static class ParameterClass {
    }

    private static class NotAnnotatedClass {
    }
}
