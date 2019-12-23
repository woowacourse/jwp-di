package nextstep.di.scanner;

import nextstep.di.factory.example.*;
import nextstep.stereotype.Component;
import nextstep.stereotype.Controller;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TypeScannerTest {
    private static final String EXAMPLE_PACKAGE = "nextstep.di.factory.example.";

    @DisplayName("컨트롤러 어노테이션이 달린 타입 스캔")
    @Test
    void scanAnnotatedWith() {
        List<Class<?>> expectedTypes = Arrays.asList(QnaController.class);

        TypeScanner typeScanner = new TypeScanner(EXAMPLE_PACKAGE);
        Set<Class<?>> types = typeScanner.scanAnnotatedWith(Controller.class);

        assertThat(types)
                .hasSize(1)
                .containsExactlyInAnyOrderElementsOf(expectedTypes);
    }

    @DisplayName("컴포넌트 어노테이션을 포함한 타입 스캔")
    @Test
    void test() {
        Set<Class<?>> expectedTypes = Sets.newHashSet(Arrays.asList(
                DefaultScanConfig.class,
                IntegrationConfig.class,
                JdbcQuestionRepository.class,
                JdbcUserRepository.class,
                MyQnaService.class,
                QnaController.class
        ));

        TypeScanner typeScanner = new TypeScanner(EXAMPLE_PACKAGE);
        Set<Class<?>> typesIncludingComponent = typeScanner.scanAnnotatedWith(Component.class);

        assertThat(typesIncludingComponent).isEqualTo(expectedTypes);
    }
}