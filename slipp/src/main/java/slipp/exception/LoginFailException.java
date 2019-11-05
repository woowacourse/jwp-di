package slipp.exception;

public class LoginFailException extends RuntimeException {
    public LoginFailException(String s) {
        super(s);
    }

    public static LoginFailException from(String userId, String password) {
        String s = String.format("(userId: %s, password: %s) userId 나 password 가 잘못입력되었습니다.");
        return new LoginFailException(s);
    }
}
