package nextstep.di.factory;

import nextstep.di.factory.example.TestApplication;
import nextstep.di.factory.example.config.ExampleConfig;
import nextstep.di.factory.example.config.IntegrationConfig;
import nextstep.di.factory.example.controller.QnaController;
import nextstep.di.factory.example.repository.JdbcQuestionRepository;
import nextstep.di.factory.example.repository.JdbcUserRepository;
import nextstep.di.factory.example.service.MyQnaService;
import nextstep.di.factory.example.service.TestService;
import nextstep.di.factory.outside.OutsideController;
import nextstep.di.factory.outside.OutsideRepository;
import nextstep.di.factory.outside.OutsideService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanScannerTest {

    @Test
    @DisplayName("scan메서드에 패키지명을 넣어주면 해당 패키지의 @Controller, @Service, @Repository 구현 클래스를 스캔한다.")
    void scanAll() {
        Set<Class<?>> preInstantiateClazz = BeanScanner.scan("nextstep.di.factory.example");

        assertThat(preInstantiateClazz).contains(
                QnaController.class,
                MyQnaService.class,
                TestService.class,
                JdbcQuestionRepository.class,
                JdbcUserRepository.class
        );
        assertThat(preInstantiateClazz.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("scan 메서드에 해당하는 패키지와 하위 패키지 이외의 클래스는 스캔하지 않는다.")
    void notScanOutsideOfPackage() {
        Set<Class<?>> preInstantiateClazz = BeanScanner.scan("nextstep.di.factory.example");

        assertThat(preInstantiateClazz).doesNotContain(
                OutsideController.class,
                OutsideService.class,
                OutsideRepository.class
        );
    }

    @Test
    @DisplayName("@Controller, @Service, @Repository 이외의 애노테이션이 선언된 클래스는 스캔하지 않는다.")
    void ignoreInvalidAnnotation() {
        Set<Class<?>> preInstantiateClazz = BeanScanner.scan("nextstep.di.factory.example");

        assertThat(preInstantiateClazz).doesNotContain(TestApplication.class);
    }

    @Test
    @DisplayName("빈 설정 파일을 스캔한다.")
    void configurationScan() {
        Set<Class<?>> configurations = BeanScanner.scanConfiguration("nextstep.di.factory.example.config");

        assertThat(configurations).contains(
                ExampleConfig.class,
                IntegrationConfig.class,
                QnaController.class,
                MyQnaService.class,
                TestService.class,
                JdbcQuestionRepository.class,
                JdbcUserRepository.class
        );
        assertThat(configurations.size()).isEqualTo(7);
    }
}
