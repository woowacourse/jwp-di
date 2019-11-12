package nextstep.di.scanner;

import java.util.Set;

public interface Scanner {
    Set<Class<?>> getAnnotatedClasses();
}
