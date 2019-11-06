package nextstep.di.scanner;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    public void setUp() {
        beanScanner = new BeanScanner("nextstep.di.factory.example");
    }

    @Test
    public void scanAnnotationTest() {
        Set<Class<?>> classes = beanScanner.scanAnnotation(Controller.class, Service.class, Repository.class);
        assertThat(classes)
                .contains(QnaController.class)
                .contains(MyQnaService.class)
                .contains(JdbcUserRepository.class)
                .contains(JdbcQuestionRepository.class);
    }
}