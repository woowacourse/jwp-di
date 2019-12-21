package nextstep.di.factory;

import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.beandefinition.ClasspathBeanDefinition;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.repository.QuestionRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private BeanFactory beanFactory;
    private Set<BeanDefinition> beanDefinitions;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        Set<Class<?>> clazz = new HashSet<>(Arrays.asList(MyQnaService.class,
                JdbcUserRepository.class, JdbcQuestionRepository.class, QnaController.class));

        beanDefinitions = clazz.stream()
                .map(ClasspathBeanDefinition::new)
                .collect(Collectors.toSet());

        beanFactory = new BeanFactory(beanDefinitions);
        beanFactory.initialize();
    }

    @Test
    public void di() {
        final QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        final MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("QuestionRepository가 싱글 인스턴스가 맞는지 테스트")
    void singleInstanceTest() {
        final MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        final QuestionRepository actual = myQnaService.getQuestionRepository();
        final QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Bean으로 등록되지 않은 클래스를 생성하지 않는지 확인")
    void beanScopeTest() {
        assertThat(beanDefinitions).doesNotContain(new ClasspathBeanDefinition(TestService.class));
    }

    @Test
    void getBeanTest() {
        assertNotNull(beanFactory.getBean(MyQnaService.class));
    }
}
