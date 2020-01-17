package nextstep.di.scanner;


import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.exception.EmptyBasePackagesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClasspathBeanScannerTest {
    private BeanFactory beanFactory;
    private ClasspathBeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanFactory = new BeanFactory();
        beanScanner = new ClasspathBeanScanner(beanFactory);
    }

    @Test
    void scanBeans() {
        Set<Class<?>> expected = new HashSet<>(Arrays.asList(QnaController.class, MyQnaService.class,
                JdbcQuestionRepository.class, JdbcUserRepository.class));
        beanScanner.scanBeans("nextstep.di.factory.example");
        beanFactory.initialize();

        expected.stream()
                .forEach(clazz -> assertNotNull(beanFactory.getBean(clazz)));
    }

    @Test
    void scanBeansWithEmptyBasePackages() {
        assertThrows(EmptyBasePackagesException.class, beanScanner::scanBeans);
    }
}