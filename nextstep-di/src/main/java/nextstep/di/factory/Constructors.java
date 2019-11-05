package nextstep.di.factory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class Constructors {

    private static final int ONE = 1;
    private static final int ZERO = 0;

    private final List<Constructor<?>> constructors;

    public Constructors(Constructor<?>[] constructors) {
        this.constructors = Arrays.asList(constructors);
    }

    public Constructors(List<Constructor<?>> constructors) {
        this.constructors = constructors;
    }

    public boolean isOneSize() {
        return constructors.size() == ONE;
    }

    public Constructor<?> getFirstConstructor() {
        return constructors.get(ZERO);
    }
}
