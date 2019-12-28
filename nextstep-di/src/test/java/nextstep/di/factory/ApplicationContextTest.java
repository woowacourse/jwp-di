package nextstep.di.factory;

import nextstep.di.factory.duplication.ExceptionConfig;
import nextstep.di.factory.example.IntegrationConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationContextTest {

    private ApplicationContext applicationContext;

    @Test
    void scanBeans() {
        applicationContext = new ApplicationContext(IntegrationConfig.class);
        assertDoesNotThrow(() -> applicationContext.scanBeans());
    }

    @Test
    void scanBeans_중복_빈_생성_예외() {
        applicationContext = new ApplicationContext(ExceptionConfig.class);
        assertThrows(DuplicateBeanException.class, () -> applicationContext.scanBeans());
    }
}