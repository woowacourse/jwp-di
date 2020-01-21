package nextstep.exception;

public class NotFoundConcreteClazzException extends RuntimeException {
    private static final String NOT_FOUND_CONCRETE_CLAZZ_MESSAGE =  "%s 인터페이스를 구현하는 Bean이 존재하지 않는다.";

    public NotFoundConcreteClazzException(String interfaceName) {
        super(String.format(NOT_FOUND_CONCRETE_CLAZZ_MESSAGE, interfaceName));
    }
}
