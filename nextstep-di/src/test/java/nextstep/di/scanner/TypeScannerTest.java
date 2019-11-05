package nextstep.di.scanner;

import nextstep.di.factory.example.QnaController;
import nextstep.stereotype.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TypeScannerTest {

    @DisplayName("컨트롤러 어노테이션이 달린 타입 스캔")
    @Test
    void scanAnnotatedWith() {
        List<Class<?>> expectedTypes = Arrays.asList(QnaController.class);

        TypeScanner typeScanner = new TypeScanner("nextstep.di.factory.example");
        Set<Class<?>> types = typeScanner.scanAnnotatedWith(Controller.class);

        assertThat(types.size()).isEqualTo(1);
        for (Class<?> expectedType : expectedTypes) {
            assertThat(types).contains(expectedType);
        }
    }
}