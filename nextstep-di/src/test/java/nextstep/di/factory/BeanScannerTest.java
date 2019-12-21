package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.definition.BeanDefinition;
import nextstep.di.factory.example.*;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanScannerTest {
    private static final String NEXTSTEP_DI_FACTORY_EXAMPLE = "nextstep.di.factory.example";

    @Test
    void Bean_어노테이션이_달린_메서드_스캔_테스트() throws NoSuchMethodException {
        BeanScanner beanScanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Map<Class<?>, BeanDefinition> preInstanticateBeanClasses = beanScanner.getPreInstanticateBeanClasses();

        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(IntegrationConfig.class.getMethod("dataSource").getReturnType());
        expected.add(IntegrationConfig.class.getMethod("jdbcTemplate", DataSource.class).getReturnType());

        assertThat(preInstanticateBeanClasses.keySet().containsAll(expected)).isTrue();
    }

    @Test
    void Repository_어노테이션이_달린_클래스_스캔_테스트() throws NoSuchMethodException {
        BeanScanner beanScanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Map<Class<?>, BeanDefinition> preInstanticateBeanClasses = beanScanner.getPreInstanticateBeanClasses();

        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(JdbcUserRepository.class);
        expected.add(JdbcQuestionRepository.class);

        assertThat(preInstanticateBeanClasses.keySet().containsAll(expected)).isTrue();
    }

    @Test
    void Configuration_어노테이션이_달린_클래스_스캔_테스트() throws NoSuchMethodException {
        BeanScanner beanScanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Map<Class<?>, BeanDefinition> preInstanticateBeanClasses = beanScanner.getPreInstanticateBeanClasses();

        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(ExampleConfig.class);
        expected.add(IntegrationConfig.class);

        assertThat(preInstanticateBeanClasses.keySet().containsAll(expected)).isTrue();
    }

    @Test
    void Service_어노테이션이_달린_클래스_스캔_테스트() throws NoSuchMethodException {
        BeanScanner beanScanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Map<Class<?>, BeanDefinition> preInstanticateBeanClasses = beanScanner.getPreInstanticateBeanClasses();

        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(MyQnaService.class);

        assertThat(preInstanticateBeanClasses.keySet().containsAll(expected)).isTrue();
    }

    @Test
    void Controller_어노테이션이_달린_클래스_스캔_테스트() throws NoSuchMethodException {
        BeanScanner beanScanner = new BeanScanner(NEXTSTEP_DI_FACTORY_EXAMPLE);
        Map<Class<?>, BeanDefinition> preInstanticateBeanClasses = beanScanner.getPreInstanticateBeanClasses();

        Set<Class<?>> expected = Sets.newHashSet();
        expected.add(QnaController.class);

        assertThat(preInstanticateBeanClasses.keySet().containsAll(expected)).isTrue();
    }
}