package slipp.dto;

public class UserLoginDto {
    private final String userId;
    private final String password;

    public UserLoginDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
