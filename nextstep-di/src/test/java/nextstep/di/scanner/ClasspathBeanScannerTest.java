package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.example.JdbcQuestionRepository;
import nextstep.di.example.JdbcUserRepository;
import nextstep.di.example.MyQnaServiceImpl;
import nextstep.di.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathBeanScannerTest {
    private BeanScanner scanner = new ClasspathBeanScanner("nextstep.di.example");

    @Test
    @DisplayName("BeanDefinitions 생성확인")
    void createBeanDefinitionTest() {
        // given
        Set<Class<?>> expected = Set.of(QnaController.class, JdbcUserRepository.class, JdbcQuestionRepository.class, MyQnaServiceImpl.class);

        // when
        List<BeanDefinition> beanDefinitions = scanner.getBeanDefinitions();
        Set<Class<?>> actual = beanDefinitions.stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        // then
        assertThat(expected).isEqualTo(actual);
    }
}