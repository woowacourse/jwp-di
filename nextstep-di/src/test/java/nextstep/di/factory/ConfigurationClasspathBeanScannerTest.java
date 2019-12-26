package nextstep.di.factory;

import nextstep.annotation.Configuration;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationClasspathBeanScannerTest {

    @Test
    void scanBeans() {
        ConfigurationBeanScanner cbs = new ConfigurationBeanScanner(Collections.singletonList(Configuration.class), "nextstep.di.factory.conf");
        Map<Class<?>, BeanDefinition> classBeanDefinitionMap = cbs.scanBeans();

        assertThat(classBeanDefinitionMap.size()).isEqualTo(2);
    }
}
