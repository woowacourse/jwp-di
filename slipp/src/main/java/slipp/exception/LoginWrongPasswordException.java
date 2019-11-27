package slipp.exception;

public class LoginWrongPasswordException extends RuntimeException {
    public LoginWrongPasswordException(String s) {
        super(s);
    }

    public static LoginWrongPasswordException from(String userId, String password) {
        String s = String.format("(userId: %s, password: %s) 잘못된 비밀번호 입니다.", userId, password);
        return new LoginWrongPasswordException(s);
    }
}
