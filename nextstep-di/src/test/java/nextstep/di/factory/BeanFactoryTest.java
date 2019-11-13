package nextstep.di.factory;

import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    private ApplicationContext beanFactory;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactory = new ApplicationContext();
        beanFactory.scan("nextstep.di.factory.example");
        beanFactory.initialize();
    }

    //    // TODO NP 발생!!
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
    void UserRepository가_싱글_인스턴스가_맞는지_테스트() {
        final UserRepository beanFactoryUserRepository = beanFactory.getBean(JdbcUserRepository.class);

        final MyQnaService qnaService = beanFactory.getBean(MyQnaService.class);
        final UserRepository qnaServiceUserRepository = qnaService.getUserRepository();

        final NewQnaService newQnaService = beanFactory.getBean(NewQnaService.class);
        final UserRepository newQnaServiceUserRepository = newQnaService.getUserRepository();

        assertThat(beanFactoryUserRepository).isEqualTo(qnaServiceUserRepository);
        assertThat(qnaServiceUserRepository).isEqualTo(newQnaServiceUserRepository);
    }

    @Test
    public void register_simple() {
        beanFactory = new ApplicationContext();
        beanFactory.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

}
