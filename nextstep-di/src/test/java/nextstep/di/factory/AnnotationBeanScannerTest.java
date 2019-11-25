package nextstep.di.factory;

import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationBeanScannerTest {
    private AnnotationBeanScanner annotationBeanScanner;

    @BeforeEach
    void setUp() {
        annotationBeanScanner = new AnnotationBeanScanner("nextstep.di.factory.example");
    }

    @Test
    void beanScan() {
        BeanCreateMatcher createMatcher = new BeanCreateMatcher();
        annotationBeanScanner.scanBean(createMatcher, Controller.class);
        assertTrue(createMatcher.containsKey(QnaController.class));
    }
}