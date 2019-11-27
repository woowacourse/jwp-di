package nextstep.di.exception;

public class NotExistBeanException extends RuntimeException {
    private final Class<?> type;

    private NotExistBeanException(Class<?> type, String s) {
        super(s);
        this.type = type;
    }

    public static NotExistBeanException from(Class<?> type) {
        String s = String.format("빈으로 등록되지 않은 타입입니다. type: %s", type);
        return new NotExistBeanException(type, s);
    }

    public Class<?> getType() {
        return type;
    }
}
