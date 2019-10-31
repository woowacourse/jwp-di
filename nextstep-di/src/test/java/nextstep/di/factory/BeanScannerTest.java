package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QnaController2;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(QnaController.class);
        expected.add(QnaController2.class);
        assertThat(beans).isEqualTo(expected);

        expected.clear();
        expected.add(JdbcQuestionRepository.class);
        expected.add(JdbcUserRepository.class);
        beans = beanScanner.getBeans(Repository.class);
        assertThat(beans).isEqualTo(expected);
    }
}