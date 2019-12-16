package nextstep.di.factory;

import nextstep.di.factory.bean.BeanDefinition;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.service.NotBeanService;
import nextstep.di.factory.example.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ClasspathBeanScannerTest {
    private ClasspathBeanScanner classpathBeanScanner;

    @BeforeEach
    void setUp() {
        classpathBeanScanner = new ClasspathBeanScanner("nextstep.di.factory.example.");
    }

    @Test
    @DisplayName("주입 받은 basePackage에서 해당되는 bean을 스캔하는지 테스트")
    void beanScanTest() {
        Set<Class<?>> actual = classpathBeanScanner.getPreInstantiateClazz();
        Set<Class<?>> expected = new HashSet<>(Arrays.asList(MyQnaService.class, TestService.class, JdbcUserRepository.class,
                JdbcQuestionRepository.class, QnaController.class));

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("주입 받은 basePackage에서 해당되지 않는 bean을 스캔하지 않는지 테스트")
    void beanScanTest2() {
        Set<Class<?>> actual = classpathBeanScanner.getPreInstantiateClazz();

        assertFalse(actual.contains(NotBeanService.class));
    }

    @DisplayName("bean을 스캔하여 Set<BeanDefinition>을 생성")
    @Test
    public void scanTest() throws Exception {
        Set<BeanDefinition> actual = classpathBeanScanner.scan();

        assertThat(actual).isNotNull();
    }

    @DisplayName("bean을 스캔하여 Set<BeanDefinition>을 생성후 getClass로 scan한 class 확인")
    @Test
    public void scanAndGetClassTest() throws Exception {
        Set<? extends Class<?>> actual = classpathBeanScanner.scan()
                .stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(MyQnaService.class, TestService.class, JdbcUserRepository.class,
                JdbcQuestionRepository.class, QnaController.class));

        assertEquals(actual, expected);
    }

}