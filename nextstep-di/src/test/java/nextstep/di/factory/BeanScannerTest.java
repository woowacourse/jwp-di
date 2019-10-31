package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {

    @Test
    void scan_Test() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        Set<Class<?>> actual = Set.of(QnaController.class, JdbcUserRepository.class, JdbcQuestionRepository.class, MyQnaService.class);
        Set<Class<?>> expected = beanScanner.getBeans();

        assertThat(expected).containsAll(actual);
    }
}