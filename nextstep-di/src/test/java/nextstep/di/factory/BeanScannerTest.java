package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
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
