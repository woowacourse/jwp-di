package nextstep.di.factory;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QuestionRepository;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new BeanFactory("nextstep.di.factory.example");
        beanFactory.initialize();
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void getTypesAnnotatedWithTest() {
        Set<Class<?>> typesAnnotatedWithController = beanFactory.getTypesAnnotatedWith(Controller.class);
        assertThat(typesAnnotatedWithController).contains(QnaController.class);
        assertThat(typesAnnotatedWithController.size()).isEqualTo(1);

        Set<Class<?>> typesAnnotatedWithService = beanFactory.getTypesAnnotatedWith(Service.class);
        assertThat(typesAnnotatedWithService).contains(MyQnaService.class);
        assertThat(typesAnnotatedWithService.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("di된 객체와 BeanFactory가 가진 객체가 실제로 같은지 확인")
    public void beanTest() {
        MyQnaService myQnaService = beanFactory.getBean(MyQnaService.class);

        QuestionRepository actual = myQnaService.getQuestionRepository();
        QuestionRepository expected = beanFactory.getBean(JdbcQuestionRepository.class);

        assertThat(actual).isEqualTo(expected);
    }
}
