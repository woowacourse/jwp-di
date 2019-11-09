package nextstep.di.factory;

import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        BeanFactoryManager.addBeans(Arrays.asList(Controller.class, Service.class, Repository.class), "nextstep.di.factory.example");
        beanFactory = BeanFactory.getInstance();
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
    void getAnnotatedBeans() {
        Set<Class<?>> annotatedBeans = beanFactory.getAnnotatedClasses(Controller.class);

        assertThat(annotatedBeans.contains(QnaController.class)).isTrue();
    }
}
