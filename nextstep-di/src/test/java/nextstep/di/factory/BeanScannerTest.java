package nextstep.di.factory;

import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanScannerTest {

    private BeanScanner beanScanner;

    @Test
    void name() {
        beanScanner = new BeanScanner();

        Set<Class<?>> preInstantiateClazz = beanScanner.scan("nextstep.di.factory.example");

        assertThat(preInstantiateClazz).contains(QnaController.class, MyQnaService.class, JdbcQuestionRepository.class, JdbcUserRepository.class);
        assertThat(preInstantiateClazz.size()).isEqualTo(4);
    }
}
