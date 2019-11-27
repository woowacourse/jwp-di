package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClasspathBeanScannerTest {
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    void setUp() {
        classpathBeanScanner = new ClasspathBeanScanner("nextstep.di.factory.example");
    }

    @Test
    void 정상적으로_스캔한다() {
        List<BeanDefinition> beanDefinitions = classpathBeanScanner.doScan();

        assertTrue(beanDefinitions.stream()
                .allMatch(beanDefinition -> beanDefinition.sameBeanClass(MyQnaService.class) ||
                        beanDefinition.sameBeanClass(QnaController.class) ||
                        beanDefinition.sameBeanClass(JdbcUserRepository.class) ||
                        beanDefinition.sameBeanClass(JdbcQuestionRepository.class)
                ));
    }
}