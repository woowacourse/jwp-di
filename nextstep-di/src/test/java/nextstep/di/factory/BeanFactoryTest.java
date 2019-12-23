package nextstep.di.factory;

import nextstep.di.exception.BeanIncludingCycleException;
import nextstep.di.exception.MultipleBeanImplementationException;
import nextstep.di.exception.NotExistBeanException;
import nextstep.di.factory.example.DefaultScanConfig;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.di.scanner.ConfigurationScanner;
import nextstep.di.scanner.TypeScanner;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(BeanFactoryTest.class);

    @Test
    @DisplayName("클래스 경로를 통한 빈 등록하기")
    public void di_usingPackagePath() throws Exception {
        // "example." 으로 끝나야 함 (그렇지 않으면 exampleforinvalid 도 prefix 에 포함이 됨)
        BeanFactory beanFactory = createWithInit("nextstep.di.factory.example.");

        test_di_atExamplePackage(beanFactory);
    }

    @Test
    @DisplayName("ComponentScan 을 통한 빈 등록하기")
    public void di_usingComponentScan() {
        BeanFactory beanFactory = createWithConfig(DefaultScanConfig.class);

        test_di_atExamplePackage(beanFactory);
    }

    private void test_di_atExamplePackage(BeanFactory beanFactory) {
        QnaController qnaController = beanFactory.getBean(QnaController.class);

        assertNotNull(qnaController);
        assertNotNull(qnaController.getQnaService());

        MyQnaService qnaService = qnaController.getQnaService();
        assertNotNull(qnaService.getUserRepository());
        assertNotNull(qnaService.getQuestionRepository());
    }

    @Test
    @DisplayName("순환 참조가 존재하는 경우")
    public void initialize_cyclicBeanReference() {
        assertThrows(BeanIncludingCycleException.class, () -> createWithInit("nextstep.di.factory.exampleforinvalid.cycle"));
    }

    @Test
    @DisplayName("생성에 필요한 빈이 존재하지 않을 경우")
    public void initialize_notExistInjectedBean() {
        assertThrows(NotExistBeanException.class, () -> createWithInit("nextstep.di.factory.exampleforinvalid.notexistbean"));
    }

    @Test
    @DisplayName("특정 인터페이스에 대한 빈이 존재하지 않을 경우")
    public void initialize_interfaceHasNoConcreteClass() {
        assertThrows(NotExistBeanException.class, () -> createWithInit("nextstep.di.factory.exampleforinvalid.notexistconcreteclass"));
    }

    @Test
    @DisplayName("특정 인터페이스에 대한 빈이 여러개 존재하는 경우")
    public void initialize_interfaceHasSeveralConcreteClass() {
        assertThrows(MultipleBeanImplementationException.class, () -> createWithInit("nextstep.di.factory.exampleforinvalid.multipleconcreteclass"));
    }

    private BeanFactory createWithInit(Object... params) {
        TypeScanner typeScanner = new TypeScanner(params);
        return BeanFactory.initializeWith(typeScanner.scanAnnotatedWith(Controller.class, Service.class, Repository.class));
    }

    private BeanFactory createWithConfig(Class<?> configClass) {
        Set<Class<?>> typesAnnotatedByComponent = ConfigurationScanner.of(DefaultScanConfig.class).scan();
        return BeanFactory.initializeWith(typesAnnotatedByComponent);
    }
}
