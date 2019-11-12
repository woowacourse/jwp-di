package nextstep.di.factory;

import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanScanner = new BeanScanner("nextstep.di.factory.example.");
    }

    @Test
    @DisplayName("주입 받은 basePackage에서 해당되는 bean을 스캔하는지 테스트")
    void beanScanTest() {
        Set<Class<?>> actual = beanScanner.getPreInstantiateClazz();
        Set<Class<?>> expected = new HashSet<>(Arrays.asList(MyQnaService.class, TestService.class, JdbcUserRepository.class, JdbcQuestionRepository.class, QnaController.class));

        assertEquals(actual, expected);
    }
}