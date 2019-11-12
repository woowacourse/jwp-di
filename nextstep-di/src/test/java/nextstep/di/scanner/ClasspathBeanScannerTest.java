package nextstep.di.scanner;

import nextstep.di.example.JdbcQuestionRepository;
import nextstep.di.example.JdbcUserRepository;
import nextstep.di.example.MyQnaServiceImpl;
import nextstep.di.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathBeanScannerTest {

    @Test
    @DisplayName("@Controller, @Service, @Repository 스캔 확인")
    void scan_Test() {
        BeanScanner beanScanner = new ClasspathBeanScanner("nextstep.di.example");
        Set<Class<?>> actual = Set.of(QnaController.class, JdbcUserRepository.class, JdbcQuestionRepository.class, MyQnaServiceImpl.class);
        Set<Class<?>> expected = beanScanner.getClassTypes();
        assertThat(expected).containsAll(actual);
    }
}