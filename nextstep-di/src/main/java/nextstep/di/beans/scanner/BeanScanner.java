package nextstep.di.beans.scanner;

import nextstep.di.beans.specification.BeanSpecification;

import java.util.Set;

public interface BeanScanner {
    Set<BeanSpecification> scan();
}
