package nextstep.di.scanner;

import java.util.Set;

public interface BeanScanner {
    Set<Class<?>> doScan();
}
