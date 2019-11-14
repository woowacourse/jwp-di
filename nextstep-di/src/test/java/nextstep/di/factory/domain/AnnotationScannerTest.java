package nextstep.di.factory.domain;

import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnotationScannerTest {
    private AnnotationScanner annotationScanner;

    @Test
    public void classPathScan() {
        BeanFactory beanFactory = new BeanFactoryImpl();
        annotationScanner = new AnnotationScanner(beanFactory);
        annotationScanner.scan("nextstep.di.factory.example");
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService);
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    public void singleInstanceTest() {
        BeanFactory beanFactory = new BeanFactoryImpl();
        annotationScanner = new AnnotationScanner(beanFactory);
        annotationScanner.scan("nextstep.di.factory.example");
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        MyQnaService qnaService = qnaController.getQnaService();
        assertThat(beanFactory.getBean(MyQnaService.class)).isEqualTo(qnaService);
    }
}