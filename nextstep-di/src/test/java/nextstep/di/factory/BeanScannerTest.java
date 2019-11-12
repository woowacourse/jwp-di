package nextstep.di.factory;

import beanscanner.Entity;
import beanscanner.Sample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {
    private BeanScanner beanScanner;

    @BeforeEach
    void setUp() {
        beanScanner = new BeanScanner("beanscanner");
    }

    @Test
    @DisplayName("어노테이션 추가 후 스캔")
    void addAnnotationToScan() {
        beanScanner.addAnnotationToScan(Entity.class);

        Set<Class<?>> classes = beanScanner.scanBeans();

        assertThat(classes.contains(Sample.class)).isTrue();
    }
}