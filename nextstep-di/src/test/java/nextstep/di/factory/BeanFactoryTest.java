package nextstep.di.factory;

import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.factory.example.QuestionRepository;
import nextstep.di.factory.example.UserRepository;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        String path = "nextstep.di.factory.example";
        BeanScanner beanScanner = new BeanScanner(Arrays.asList(Controller.class, Service.class, Repository.class), path);
        beanFactory = new BeanFactory(beanScanner.scanBeans());
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
    public void getControllers() throws Exception {
        Map<Class<?>, Object> beanByAnnotation = beanFactory.getBeanByAnnotation(Controller.class);

        assertNotNull(beanByAnnotation.get(QnaController.class));
    }

    @Test
    public void getBean() throws Exception {
        Object bean1 = beanFactory.getBean(MyQnaService.class).getUserRepository();
        Object bean2 = beanFactory.getBean(UserRepository.class);

        Object bean3 = beanFactory.getBean(MyQnaService.class).getQuestionRepository();
        Object bean4 = beanFactory.getBean(QuestionRepository.class);

        assertThat(bean1).isEqualTo(bean2);
        assertThat(bean3).isEqualTo(bean4);
    }


}
