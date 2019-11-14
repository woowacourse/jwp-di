package nextstep.di.factory;

import nextstep.di.factory.example.config.ExampleConfig;
import nextstep.di.factory.example.controller.QnaController;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BeanDefinitionFactoryTest {
    @Test
    @DisplayName("Scan한 클래스로부터 BeanDefinition을 생성한다.")
    void createBeanDefinition() {
        Set<Class<?>> preInstantiateClazz = Sets.newHashSet(Arrays.asList(ExampleConfig.class, QnaController.class));
        Map<Class<?>, BeanDefinition> beanDefinitions = createBeanDefinitions(preInstantiateClazz);

        assertNotNull(beanDefinitions.get(ExampleConfig.class));
        assertNotNull(beanDefinitions.get(QnaController.class));
        assertNotNull(beanDefinitions.get(DataSource.class));
    }

    @Test
    @DisplayName("Bean이 Configuration을 통해 생성되는 경우, 생성된 BeanDefinition의 configType은 해당 Configuration 클래스다.")
    void checkConfigType_ifBeanIsGeneratedByConfiguration() {
        Set<Class<?>> preInstantiateClazz = Sets.newHashSet(Arrays.asList(ExampleConfig.class));
        Map<Class<?>, BeanDefinition> beanDefinitions = createBeanDefinitions(preInstantiateClazz);

        BeanDefinition dataSourceBeanDefinition = beanDefinitions.get(DataSource.class);
        assertThat(dataSourceBeanDefinition.getConfigType()).isEqualTo(ExampleConfig.class);
    }

    @Test
    @DisplayName("Configuration에서 생성하는 bean이 아닌 경우, BeanDefinition의 configType은 null이다.")
    void createBeanDefinitio() {
        Set<Class<?>> preInstantiateClazz = Sets.newHashSet(Arrays.asList(QnaController.class));
        Map<Class<?>, BeanDefinition> beanDefinitions = createBeanDefinitions(preInstantiateClazz);

        BeanDefinition qnaControllerBeanDefinition = beanDefinitions.get(QnaController.class);
        assertNull(qnaControllerBeanDefinition.getConfigType());
    }

    private Map<Class<?>, BeanDefinition> createBeanDefinitions(Set<Class<?>> preInstantiateClazz) {
        BeanDefinitionFactory beanDefinitionFactory = new BeanDefinitionFactory(preInstantiateClazz);
        return beanDefinitionFactory.createBeanDefinition();
    }
}