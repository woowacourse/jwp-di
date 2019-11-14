package nextstep.di.scanner;

import nextstep.annotation.ComponentScan;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.config.ExampleConfig;
import nextstep.di.factory.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassPathBeanScannerTest {
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        List<Object> basePackages = Collections.singletonList("nextstep.di.factory.example");
        scanner = new ClassPathBeanScanner(basePackages.toArray());
    }

    @Test
    void classPath_bean_가져오기() {
        Object[] expectedBeans = {QnaController.class, MyQnaService.class, SingletonTest1.class,
                SingletonTest2.class, JdbcUserRepository.class, JdbcQuestionRepository.class};

        Set<BeanDefinition> beans = scanner.scan();
        Set<? extends Class<?>> clazz = beans.stream()
                .map(BeanDefinition::getClazz)
                .collect(Collectors.toSet());

        assertTrue(clazz.containsAll(Arrays.asList(expectedBeans)));
    }
}