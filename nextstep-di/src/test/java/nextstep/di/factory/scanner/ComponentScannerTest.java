package nextstep.di.factory.scanner;

import nextstep.di.factory.definition.BeanDefinition;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentScannerTest {

    @Test
    void scan() {
        ComponentScanner componentScanner = new ComponentScanner("nextstep.di.factory.example");
        Set<BeanDefinition> beanDefinitions = componentScanner.scan();
        assertThat(beanDefinitions.size()).isEqualTo(4);
    }
}
