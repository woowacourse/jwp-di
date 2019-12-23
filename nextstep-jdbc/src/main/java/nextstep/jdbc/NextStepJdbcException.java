package nextstep.jdbc;

public class NextStepJdbcException extends RuntimeException {

    public NextStepJdbcException(Exception e) {
        super(e);
    }

    public static NextStepJdbcException from(Exception e) {
        return new NextStepJdbcException(e);
    }
}
