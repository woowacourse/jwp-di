package nextstep.di.factory;

import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.repository.QuestionRepository;
import nextstep.di.factory.example.repository.UserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example2.TestService;
import nextstep.di.factory.example2.TestServiceObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        Set<Class<?>> preInstantiateClazz = new HashSet<>(Arrays.asList(MyQnaService.class, UserRepository.class, QuestionRepository.class,
                JdbcUserRepository.class, JdbcQuestionRepository.class, QnaController.class));

        beanFactory = new BeanFactory(preInstantiateClazz);
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
    void beanScopeTest() {
        assertThat(beanFactory.getBean(TestService.class)).isNull();
        assertThat(beanFactory.getBean(TestServiceObject.class)).isNull();
    }
}
