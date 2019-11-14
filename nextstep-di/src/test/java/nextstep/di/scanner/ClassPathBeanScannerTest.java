package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.*;
import nextstep.di.factory.exception.InvalidBeanClassTypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassPathBeanScannerTest {
    private List<Object> exampleBasePackages = Collections.singletonList("nextstep.di.factory.example");
    private List<Object> failBasePackages = Collections.singletonList("nextstep.di.factory.fail");

    @Test
    void classPath_bean_가져오기() {
        Scanner scanner = new ClassPathBeanScanner(exampleBasePackages.toArray());
        Object[] expectedBeans = {QnaController.class, MyQnaService.class, SingletonTest1.class,
                SingletonTest2.class, JdbcUserRepository.class, JdbcQuestionRepository.class};

        Set<BeanDefinition> beans = scanner.scan();
        Set<? extends Class<?>> clazz = beans.stream()
                .map(BeanDefinition::getClazz)
                .collect(Collectors.toSet());

        assertTrue(clazz.containsAll(Arrays.asList(expectedBeans)));
    }

    @Test
    void 애노테이션이_있는_인터페이스() {
        Scanner classPathBeanScanner = new ClassPathBeanScanner(failBasePackages.toArray());
        assertThrows(InvalidBeanClassTypeException.class, classPathBeanScanner::scan);
    }
}
