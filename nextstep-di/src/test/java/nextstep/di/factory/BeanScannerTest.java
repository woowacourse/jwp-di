package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QnaController2;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanScanner = new BeanScanner("nextstep.di.factory.example");
    }

    @Test
    @SuppressWarnings("unchecked")
    void getBeans() {
        Set<Class<?>> beans = beanScanner.getBeans(Controller.class);
        assertThat(beans).isEqualTo(Set.of(QnaController.class, QnaController2.class));

        beans = beanScanner.getBeans(Repository.class);
        assertThat(beans).isEqualTo(Set.of(JdbcQuestionRepository.class, JdbcUserRepository.class));
    }
}