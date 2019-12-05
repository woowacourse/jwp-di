package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.bean.BeanDefinition;
import nextstep.di.factory.example.JdbcQuestionRepository;
import nextstep.di.factory.example.JdbcUserRepository;
import nextstep.di.factory.example.MyQnaService;
import nextstep.di.factory.example.QnaController;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class ConstructorBeanDefinitionScannerTest {
    @Test
    void scan() {
        Set<String> samplePackages = Sets.newHashSet("nextstep.di.factory.example");
        BeanDefinitionScanner constructorScanner = new ConstructorBeanDefinitionScanner(samplePackages);
        Set<BeanDefinition> beanDefinitions = constructorScanner.scan();
        List<Class> scannedClass = beanDefinitions.stream()
                .map(BeanDefinition::getClazz)
                .collect(toList());

        assertThat(scannedClass)
                .contains(MyQnaService.class)
                .contains(QnaController.class)
                .contains(JdbcQuestionRepository.class)
                .contains(JdbcUserRepository.class);
    }
}