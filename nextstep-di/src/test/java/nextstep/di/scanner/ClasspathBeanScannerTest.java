package nextstep.di.scanner;

import nextstep.di.factory.BeanFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClasspathBeanScannerTest {
    private static final String TEST_BASE_PACKAGE = "nextstep.di.factory.example";

    @Test
    @DisplayName("스캔한 Component bean 생성정보")
    void getDefinitionBean() {
        BeanFactory beanFactory = new BeanFactory();
        ClasspathBeanScanner cpbs = new ClasspathBeanScanner(beanFactory);

        cpbs.registerPackage(TEST_BASE_PACKAGE);
    }
}