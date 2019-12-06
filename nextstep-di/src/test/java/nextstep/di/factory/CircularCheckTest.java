package nextstep.di.factory;

import annotation.Service;
import nextstep.di.factory.circularreference.A;
import nextstep.di.factory.circularreference.B;
import nextstep.di.factory.circularreference.C;
import nextstep.di.factory.circularreference.D;
import nextstep.exception.CircularReferenceException;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CircularCheckTest {
    private CircularChecker circularChecker;

    @BeforeEach
    void setUp() {
        BeanScanner beanScanner = new BeanScanner("nextstep.di.factory.circularreference");
        circularChecker = new CircularChecker(beanScanner.scan());
    }

    @Test
    void check_if_circular_reference() {
        assertThrows(CircularReferenceException.class, () -> circularChecker.check(A.class));
    }

    @Test
    void check_if_not_circular_reference() {
        assertDoesNotThrow(() -> circularChecker.check(D.class));
    }
}