package nextstep.di.factory;

import nextstep.di.factory.example.*;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    @Test
    public void di() {
        BeanFactory beanFactory = new BeanFactory();
        TestScanner scanner = new TestScanner(beanFactory);
        scanner.doScan("nextstep.di.factory.example");

        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    void 애노테이션이_있는_인터페이스() {
        BeanFactory beanFactory = new BeanFactory();
        TestScanner scanner = new TestScanner(beanFactory);
        assertThrows(InvalidBeanClassTypeException.class,
                () -> scanner.doScan("nextstep.di.factory.fail"));
    }

    @Test
    void 빈_싱글턴_보장_여부() {
        BeanFactory beanFactory = new BeanFactory();
        TestScanner scanner = new TestScanner(beanFactory);
        scanner.doScan("nextstep.di.factory.example");
        SingletonTest1 singletonTest1 = beanFactory.getBean(SingletonTest1.class);
        SingletonTest2 singletonTest2 = beanFactory.getBean(SingletonTest2.class);
        assertThat(singletonTest1.getQnaService()).isEqualTo(singletonTest2.getQnaService());
    }
}
