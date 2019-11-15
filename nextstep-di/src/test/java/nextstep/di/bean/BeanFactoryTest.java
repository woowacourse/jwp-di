package nextstep.di.bean;

import nextstep.di.bean.example.IntegrationConfig;
import nextstep.di.bean.example.MyQnaService;
import nextstep.di.bean.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger( BeanFactoryTest.class );

    private BeanFactory beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        this.beanFactory = (new BeanFactory(
                new ApplicationContext(IntegrationConfig.class))
        ).initialize();
    }

    @Test
    public void di() throws Exception {
        final QnaController qnaController = this.beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        final MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());

        assertTrue(qnaController.getQnaService() == this.beanFactory.getBean(MyQnaService.class));
    }
}