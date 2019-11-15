package nextstep.di.scanner;

import nextstep.di.bean.BeanDefinition;
import nextstep.di.example.ExampleConfig;
import nextstep.di.example.IntegrationConfig;
import nextstep.di.example.MyJdbcTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanScannerTest {
    private ConfigurationBeanScanner scanner = new ConfigurationBeanScanner("nextstep.di.example");

    @Test
    @DisplayName("BeanDefinitions 생성확인")
    void createBeanDefinitionTest() {
        // given
        Set<Class<?>> expected = Set.of(ExampleConfig.class, IntegrationConfig.class, DataSource.class, MyJdbcTemplate.class);

        // when
        List<BeanDefinition> beanDefinitions = scanner.getBeanDefinitions();
        Set<Class<?>> actual = beanDefinitions.stream()
                .map(BeanDefinition::getBeanClass)
                .collect(Collectors.toSet());

        // then
        assertThat(actual).isEqualTo(expected);
    }
}