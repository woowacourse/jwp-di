package nextstep.exception;

import java.util.function.Consumer;

public class ExceptionUtils {
    public static <T, E extends Exception> Consumer<T> consumerWrapper(ConsumerWithException<T, E> fe) {
        return arg -> {
            try {
                fe.accept(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
