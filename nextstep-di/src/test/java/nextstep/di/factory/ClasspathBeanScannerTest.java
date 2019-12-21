package nextstep.di.factory;

import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.service.TestService;
import nextstep.di.factory.scanner.ClasspathBeanScanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClasspathBeanScannerTest {
    private ClasspathBeanScanner cbs;

    @BeforeEach
    void setUp() {
        cbs = new ClasspathBeanScanner("nextstep.di.factory.example.");
    }

    @DisplayName("bean을 스캔하여 Set<BeanDefinition>을 생성")
    @Test
    public void scanTest() {
        Set<BeanDefinition> actual = cbs.scan();

        assertThat(actual).isNotNull();
    }

    @DisplayName("bean을 스캔하여 Set<BeanDefinition>을 생성후 getClass로 scan한 class 확인")
    @Test
    public void scanAndGetClassTest() {
        Set<? extends Class<?>> actual = cbs.scan()
                .stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        Set<Class<?>> expected = new HashSet<>(Arrays.asList(MyQnaService.class, TestService.class, JdbcUserRepository.class,
                JdbcQuestionRepository.class, QnaController.class));

        assertEquals(expected, actual);
    }

    @AfterEach
    void tearDown() {
        cbs = null;
    }
}