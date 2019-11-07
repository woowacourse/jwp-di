package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeanScannerTest {

    @Test
    void getBeans() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.example");
        assertThat(beanScanner.getBeans()).isEqualTo(new HashSet<>(Arrays.asList(JdbcUserRepository.class,
                                                                                JdbcQuestionRepository.class,
                                                                                QnaController.class,
                                                                                MyQnaService.class)));
    }
}