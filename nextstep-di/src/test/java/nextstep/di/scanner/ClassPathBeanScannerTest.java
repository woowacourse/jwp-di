package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassPathBeanScannerTest {
    private Scanner2 scanner;

    @BeforeEach
    void setUp() {
        scanner = new ClassPathBeanScanner();
    }

    @Test
    void configuration_bean_가져오기() {
        Set<BeanDefinition> beans = scanner.scan("nextstep.di.factory.example");
        Set<? extends Class<?>> clazz = beans.stream()
                .map(BeanDefinition::getClazz)
                .collect(Collectors.toSet());

        assertThat(beans.size()).isEqualTo(6);
        assertTrue(clazz.contains(QnaController.class));
    }
}