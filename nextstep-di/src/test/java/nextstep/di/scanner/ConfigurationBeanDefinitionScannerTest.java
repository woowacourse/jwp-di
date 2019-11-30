package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.MyJdbcTemplate;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationBeanDefinitionScannerTest {

    @Test
    void scan() {
        Set<String> samplePackages = Sets.newHashSet("nextstep.di.factory.example.scan");
        BeanDefinitionScanner configurationScanner = new ConfigurationBeanDefinitionScanner(samplePackages);
        Set<BeanDefinition> beanDefinitions = configurationScanner.scan();

        List<Class> scannedClass = beanDefinitions.stream()
                .map(BeanDefinition::getClazz)
                .collect(toList());

        assertThat(scannedClass).contains(DataSource.class, MyJdbcTemplate.class);
    }
}