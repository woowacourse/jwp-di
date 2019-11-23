package nextstep.di.factory.scanner;

import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.example.IntegrationConfig;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationBeanScannerTest {
    @Test
    public void scanTest() {
        ConfigurationBeanScanner configurationBeanScanner = new ConfigurationBeanScanner(IntegrationConfig.class);
        Set<BeanDefinition> beanDefinitions = configurationBeanScanner.scan();
        Set<Class<?>> classTypes = getClassTypes(beanDefinitions);

        assertThat(classTypes).contains(DataSource.class);
        assertThat(classTypes).contains(MyJdbcTemplate.class);
    }

    private Set<Class<?>> getClassTypes(Set<BeanDefinition> beanDefinitions) {
        return beanDefinitions.stream()
                .map(BeanDefinition::getClassType)
                .collect(Collectors.toSet());
    }
}
