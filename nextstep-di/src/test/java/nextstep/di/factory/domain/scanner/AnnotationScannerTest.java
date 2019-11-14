package nextstep.di.factory.domain.scanner;

import nextstep.di.factory.domain.BeanFactory;
import nextstep.di.factory.domain.GenericBeanFactory;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AnnotationScannerTest {
    private AnnotationScanner annotationScanner;

    @Test
    public void classPathScan() {
        BeanFactory beanFactory = new GenericBeanFactory();
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
        BeanFactory beanFactory = new GenericBeanFactory();
        annotationScanner = new AnnotationScanner(beanFactory);
        annotationScanner.scan("nextstep.di.factory.example");
        beanFactory.initialize();

        QnaController qnaController = beanFactory.getBean(QnaController.class);
        MyQnaService qnaService = qnaController.getQnaService();
        assertThat(beanFactory.getBean(MyQnaService.class)).isEqualTo(qnaService);
    }
}