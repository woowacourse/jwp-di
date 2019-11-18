package nextstep.di.bean;

import nextstep.di.bean.example.JdbcQuestionRepository;
import nextstep.di.bean.example.JdbcUserRepository;
import nextstep.di.bean.example.MyQnaService;
import nextstep.di.bean.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClasspathBeanScannerTest {
    @Test
    void getClasspathBeansToInstantiateTest() {
        final ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner("nextstep.di.bean.example");
        assertEquals(
                new HashSet<Class<?>>() {{
                    add(JdbcQuestionRepository.class);
                    add(QnaController.class);
                    add(JdbcUserRepository.class);
                    add(MyQnaService.class);
                }},
                classpathBeanScanner.getClasspathBeansToInstantiate()
        );
    }
}