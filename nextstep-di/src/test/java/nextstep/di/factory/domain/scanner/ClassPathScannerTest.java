package nextstep.di.factory.domain.scanner;

import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.GenericBeanFactory;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClassPathScannerTest {
    private ClassPathScanner classPathScanner;

    @Test
    public void classPathScan() {
        BeanFactory beanFactory = new GenericBeanFactory();
        classPathScanner = new ClassPathScanner(beanFactory);
        classPathScanner.scan("nextstep.di.factory.example");
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }
}