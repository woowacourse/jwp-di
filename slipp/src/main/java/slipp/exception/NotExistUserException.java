package slipp.exception;

public class NotExistUserException extends RuntimeException {
    private NotExistUserException(String s) {
        super(s);
    }

    public static NotExistUserException fromUserId(String userId) {
        return new NotExistUserException(String.format("userId: %s 인 유저가 존재하지 않습니다.", userId));
    }
}
