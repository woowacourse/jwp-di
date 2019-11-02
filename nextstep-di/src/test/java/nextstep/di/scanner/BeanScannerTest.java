package nextstep.di.scanner;

import nextstep.di.scanner.example.ExampleController;
import nextstep.di.scanner.example.ExampleRepository;
import nextstep.di.scanner.example.ExampleService;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanScannerTest {

    @Test
    void single_annotation() {
        BeanScanner scanner = new BeanScanner("nextstep.di.scanner.example");
        assertThat(scanner.getTypesAnnotatedWith(Controller.class))
                .hasSize(1)
                .contains(ExampleController.class);
    }

    @Test
    void multiple_annotations() {
        BeanScanner scanner = new BeanScanner("nextstep.di.scanner.example");
        assertThat(scanner.getTypesAnnotatedWith(Controller.class, Service.class, Repository.class))
                .hasSize(3)
                .contains(ExampleController.class, ExampleService.class, ExampleRepository.class);
    }
}
