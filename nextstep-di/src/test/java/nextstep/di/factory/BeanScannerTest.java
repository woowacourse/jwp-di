package nextstep.di.factory;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {
    @Test
    void getPreInstantiateClass() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(QnaController.class, MyQnaService.class,
                JdbcQuestionRepository.class, JdbcUserRepository.class));
        Set<Class<?>> actual = beanScanner.getPreInstanticateClass();

        assertThat(expected).isEqualTo(actual);
    }
}