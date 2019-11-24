package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaServiceImpl;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathBeanScannerTest {

    @Test
    @DisplayName("@Controller, @Service, @Repository 스캔 확인")
    void scan_Test() {
        ClasspathBeanScanner scanner = new ClasspathBeanScanner("nextstep.di.factory.example");
        Set<Class<?>> actual = scanner.getBeanDefinitions().stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        Set<Class<?>> expected = Set.of(QnaController.class, JdbcUserRepository.class, JdbcQuestionRepository.class, MyQnaServiceImpl.class);

        assertThat(actual).containsAll(expected);
    }
}