package nextstep.di.exception;

import java.util.List;

public class MultipleBeanImplementationException extends RuntimeException {
    public MultipleBeanImplementationException(String s) {
        super(s);
    }

    public static MultipleBeanImplementationException from(Class<?> injectedInterfece, List<Class<?>> concreteclasses) {
        String s = String.format("해당 인터페이스를 구현한 여러 클래스가 존재합니다. interface: %s, concreteclasses: %s",
                injectedInterfece,
                concreteclasses);

        return new MultipleBeanImplementationException(s);
    }
}
