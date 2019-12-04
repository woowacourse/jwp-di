package nextstep.di.scanner;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nextstep.di.factory.BeanFactory;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClasspathBeanScannerTest {
    @Test
    void getPreInstantiateClass() {
        BeanFactory beanFactory = new BeanFactory();
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner(beanFactory);

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(QnaController.class, MyQnaService.class,
                JdbcQuestionRepository.class, JdbcUserRepository.class));
        classpathBeanScanner.scanBeans("nextstep.di.factory.example");
        beanFactory.initialize();

        expected.stream()
                .forEach(clazz -> assertNotNull(beanFactory.getBean(clazz)));
    }
}