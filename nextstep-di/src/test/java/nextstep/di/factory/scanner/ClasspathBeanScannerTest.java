package nextstep.di.factory.scanner;

import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyJdbcTemplate;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ClasspathBeanScannerTest {
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    public void setUp() {
        classpathBeanScanner = new ClasspathBeanScanner("nextstep.di.factory.example");
    }

    @Test
    public void scanTest() {
        Set<BeanDefinition> beanDefinitions = classpathBeanScanner.scan();
        Set<Class<?>> classTypes = getClassTypes(beanDefinitions);

        assertThat(classTypes).contains(QnaController.class);
        assertThat(classTypes).contains(MyQnaService.class);
        assertThat(classTypes).contains(JdbcQuestionRepository.class);
    }

    @Test
    public void getTypesAnnotatedTest() {
        Set<Class<?>> typesAnnotatedWithController = classpathBeanScanner.getAnnotatedTypes();

        assertThat(typesAnnotatedWithController).contains(QnaController.class);
        assertThat(typesAnnotatedWithController).contains(MyQnaService.class);
        assertThat(typesAnnotatedWithController).contains(JdbcQuestionRepository.class);
        assertThat(typesAnnotatedWithController).doesNotContain(MyJdbcTemplate.class);
    }

    private Set<Class<?>> getClassTypes(Set<BeanDefinition> beanDefinitions) {
        return beanDefinitions.stream()
                .map(BeanDefinition::getClassType)
                .collect(Collectors.toSet());
    }
}
