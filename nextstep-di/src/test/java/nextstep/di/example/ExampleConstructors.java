package nextstep.di.example;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class ExampleConstructors {

    private final List<Constructor<?>> constructors;

    public ExampleConstructors(Constructor<?>[] constructors) {
        this.constructors = Arrays.asList(constructors);
    }

    public ExampleConstructors(List<Constructor<?>> constructors) {
        this.constructors = constructors;
    }
}
