package nextstep.di.factory;

import beanscanner.Entity;
import beanscanner.Sample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClassPathBeanScannerTest {
    private BeanFactory beanFactory;
    private ClassPathBeanScanner classPathBeanScanner;

    @BeforeEach
    void setUp() {
        beanFactory = new BeanFactory();
        classPathBeanScanner = new ClassPathBeanScanner(beanFactory);
    }

    @Test
    @DisplayName("어노테이션 추가 후 스캔")
    void addAnnotationToScan() {
        classPathBeanScanner.addAnnotationToScan(Entity.class);
        classPathBeanScanner.doScan("beanscanner");
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(Sample.class));
    }
}