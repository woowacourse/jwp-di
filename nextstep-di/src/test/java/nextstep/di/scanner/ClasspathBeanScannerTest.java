package nextstep.di.scanner;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathBeanScannerTest {
    private ClasspathBeanScanner beanScanner = new ClasspathBeanScanner("nextstep.di.factory.example");
    private final List<Class<?>> executeResult = Arrays.asList(JdbcUserRepository.class,
            MyQnaService.class,
            QnaController.class,
            JdbcQuestionRepository.class);

    @Test
    void getInstantiatedTypes() {
        for (Class<?> instantiatedType : beanScanner.getInstantiatedTypes()) {
            assertThat(executeResult).contains(instantiatedType);
        }
    }

    @Test
    void isContainsBean() {
        for (Class<?> clazz : executeResult) {
            assertThat(beanScanner.isContainsBean(clazz)).isTrue();
        }
    }
}