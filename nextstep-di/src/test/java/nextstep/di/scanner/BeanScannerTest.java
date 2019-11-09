package nextstep.di.scanner;

import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanScannerTest {

    public static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";

    @Test
    @DisplayName("beanScanner가 모든 Bean을 정상적으로 스캔한다.")
    void scanBeanSuccessfully() {
        BeanScanner beanScanner = new BeanScanner(TEST_BASE_PACKAGE);
        assertEquals(beanScanner.getBeans().size(), 4);
    }

    @Test
    @DisplayName("Controller 어노테이션 클래스를 정상적으로 스캔한다.")
    void scanControllersSuccessfully() {
        BeanScanner beanScanner = new BeanScanner(TEST_BASE_PACKAGE);
        assertTrue(beanScanner.getBeans().contains(QnaController.class));
    }

    @Test
    @DisplayName("Service 어노테이션 클래스를 정상적으로 스캔한다.")
    void scanServicesSuccessfully() {
        BeanScanner beanScanner = new BeanScanner(TEST_BASE_PACKAGE);
        assertTrue(beanScanner.getBeans().contains(QnaController.class));
    }

    @Test
    @DisplayName("Repository 어노테이션 클래스를 정상적으로 스캔한다.")
    void scanRepositoriesSuccessfully() {
        BeanScanner beanScanner = new BeanScanner(TEST_BASE_PACKAGE);
        assertTrue(beanScanner.getBeans().contains(JdbcQuestionRepository.class));
        assertTrue(beanScanner.getBeans().contains(JdbcUserRepository.class));
    }
}