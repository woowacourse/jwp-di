package nextstep.di.factory;

import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QnaController2;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    public void setup() {
        beanFactory = new BeanFactory("nextstep.di.factory.example");
        beanFactory.initialize();
    }

    @Test
    public void di() {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void getController() {
        Map<Class<?>, Object> controllers = beanFactory.getControllers();
        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(QnaController.class);
        expected.add(QnaController2.class);
        assertThat(controllers.size()).isEqualTo(2);
        assertThat(controllers.keySet()).isEqualTo(expected);
        assertThat(controllers.get(QnaController.class)).isInstanceOf(QnaController.class);
        assertThat(controllers.get(QnaController2.class)).isInstanceOf(QnaController2.class);
    }
}
