package nextstep.di.factory;

import com.google.common.collect.Sets;
import nextstep.di.factory.beandefinition.BeanDefinition;
import nextstep.di.factory.example.core.ExampleConfig;
import nextstep.di.factory.scanner.ConfigurationBeanScanner;
import org.junit.jupiter.api.Test;

import javax.activation.DataSource;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationBeanScannerTest {

    @Test
    public void register_simple() {
        BeanFactory beanFactory = new BeanFactory();
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(beanFactory);
        cbs.register(ExampleConfig.class);
        beanFactory.initialize();

        assertNotNull(beanFactory.getBean(DataSource.class));
    }

    @Test
    public void scanTest() {
        // given
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(Sets.newHashSet("nextstep.di.factory.example."));

        // when
        Set<BeanDefinition> beanDefinitions = cbs.scan();

        // then
        assertThat(beanDefinitions).isNotNull();
    }

}