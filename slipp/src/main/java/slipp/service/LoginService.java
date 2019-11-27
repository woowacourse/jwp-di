package slipp.service;

import nextstep.annotation.Inject;
import nextstep.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.controller.UserSessionUtils;
import slipp.domain.User;
import slipp.dto.UserLoginDto;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final UserService userService;

    @Inject
    public LoginService(UserService userService) {
        this.userService = userService;
    }

    public boolean isLoginedUser(HttpSession session, String userId) {
        log.debug("begin");

        User user = userService.findByUserId(userId);
        return UserSessionUtils.isSameUser(session, user);
    }

    public boolean isNotLoginedUser(HttpSession session, String userId) {
        return !isLoginedUser(session, userId);
    }

    public Optional<User> getLoginedUser() {
        throw new UnsupportedOperationException("아직 구현이 되지 않았습니다.");
    }

    public boolean login(HttpSession session, UserLoginDto userLoginDto) {
        String userId = userLoginDto.getUserId();
        String password = userLoginDto.getPassword();

        return userService.findByUserId2(userId)
                .filter(user -> user.matchPassword(password))
                .map(user -> addUserToSession(session, user))
                .orElse(false);
    }

    private boolean addUserToSession(HttpSession session, User user) {
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

        return true;
    }

    public void logout(HttpSession session) {
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);
    }

    public boolean isLogined(HttpSession session) {
        return session.getAttribute(UserSessionUtils.USER_SESSION_KEY) != null;
    }

    public boolean isNotLogined(HttpSession session) {
        return !isLogined(session);
    }
}
