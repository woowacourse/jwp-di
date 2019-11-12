package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathBeanScannerTest {
    @Test
    public void getTypesAnnotatedTest() {
        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner("nextstep.di.factory.example");
        Set<Class<?>> typesAnnotatedWithController = classpathBeanScanner.getAnnotatedTypes();

        assertThat(typesAnnotatedWithController).contains(QnaController.class);
        assertThat(typesAnnotatedWithController).contains(MyQnaService.class);
        assertThat(typesAnnotatedWithController).contains(JdbcQuestionRepository.class);
        assertThat(typesAnnotatedWithController).doesNotContain(MyJdbcTemplate.class);
    }
}