package nextstep.di.factory;

import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.ClasspathBeanScanner;
import nextstep.di.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanFactoryImplTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryImplTest.class);
    private static final String BASE_PACKAGE = "nextstep.di.factory.example";

    private BeanFactoryImpl beanFactoryImpl;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        beanFactoryImpl = new BeanFactoryImpl();

        ClasspathBeanScanner classpathBeanScanner = new ClasspathBeanScanner();
        classpathBeanScanner.setBasePackage(BASE_PACKAGE);
        classpathBeanScanner.doScan(beanFactoryImpl);

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        configurationBeanScanner.setBasePackage(BASE_PACKAGE);
        configurationBeanScanner.doScan(beanFactoryImpl);

        beanFactoryImpl.initialize();
    }

    @Test
    public void di() throws Exception {
        QnaController qnaController = beanFactoryImpl.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }
}
