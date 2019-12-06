package nextstep.di.factory;

import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import nextstep.exception.CircularReferenceException;
import nextstep.exception.ParameterIsNotBeanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentFactoryTest {
    private ComponentFactory componentFactory;

    @Test
    public void initialize() {
        bindComponentFactory("nextstep.di.factory.example");
        Map<Class<?>, Object> components = componentFactory.initialize();

        assertNotNull(components.get(QnaController.class));
        assertNotNull(components.get(MyQnaService.class));
        assertNotNull(components.get(JdbcQuestionRepository.class));
    }

    @Test
    @DisplayName("다른 패키지의 클래스를 찾으려고 할 때")
    void another_package_class() {
        bindComponentFactory("nextstep.di.factory.example");
        Map<Class<?>, Object> components = componentFactory.initialize();
        assertNull(components.get(BeanScanner.class));
    }

    @Test
    @DisplayName("빈이 아닌 클래스가 빈인 클래스의 파라미터로 있을 때")
    void parameter_is_no_bean() {
        bindComponentFactory("nextstep.di.factory.notbean");
        assertThrows(ParameterIsNotBeanException.class, () -> componentFactory.initialize());
    }

    @Test
    @DisplayName("Inject가 붙은 두개 이상의 빈이 순환참조일 경우")
    void beans_is_circular_reference() {
        bindComponentFactory("nextstep.di.factory.circularreference");
        assertThrows(CircularReferenceException.class, () -> componentFactory.initialize());
    }

    private void bindComponentFactory(final String basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        Set<Class<?>> preInstantiateComponents = beanScanner.scan();

        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner();
        Set<Class<?>> preInstantiateConfigurationBeans = configurationBeanScanner.scan(IntegrationConfig.class);
        BeanDefinition beanDefinition = new BeanDefinition(preInstantiateComponents, preInstantiateConfigurationBeans);

        componentFactory = new ComponentFactory(beanDefinition);
    }
}
