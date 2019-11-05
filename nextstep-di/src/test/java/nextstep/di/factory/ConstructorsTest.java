package nextstep.di.factory;

import nextstep.di.factory.example.ExampleConstructors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstructorsTest {
    private Constructors constructors;

    @BeforeEach
    void setUp() {
        constructors = new Constructors(ExampleConstructors.class.getConstructors());
    }

    @Test
    void isFirst() {
        assertThat(constructors.isOneSize()).isFalse();
    }

    @Test
    void getFirstConstructor() {
        assertThat(constructors.getFirstConstructor()).isEqualTo(ExampleConstructors.class.getConstructors()[0]);
    }
}