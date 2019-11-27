package slipp.controller;

import nextstep.annotation.Inject;
import nextstep.mvc.ModelAndView;
import nextstep.mvc.tobe.AbstractNewController;
import nextstep.stereotype.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.domain.User;
import slipp.dto.UserCreatedDto;
import slipp.dto.UserUpdatedDto;
import slipp.exception.NotAuthorizedUserException;
import slipp.exception.NotExistUserException;
import slipp.service.LoginService;
import slipp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController extends AbstractNewController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final LoginService loginService;

    @Inject
    public UserController(UserService userService, LoginService loginService) {
        log.debug("begin");

        this.userService = userService;
        this.loginService = loginService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        if (loginService.isNotLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        ModelAndView mav = jspView("/user/list.jsp");
        mav.addObject("users", userService.findAll());
        return mav;
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        ModelAndView mav = jspView("/user/profile.jsp");

        String userId = request.getParameter("userId");
        User user = userService.findByUserId2(userId)
                .orElseThrow(() -> NotExistUserException.fromUserId(userId));

        log.debug("user :{}", user);
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        return jspView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        UserCreatedDto userCreatedDto = readUserCreatedDto(request);

        log.debug("userCreatedDto : {}", userCreatedDto);
        userService.create(userCreatedDto);

        return jspView("redirect:/");
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        String userId = request.getParameter("userId");
        if (loginService.isNotLoginedUser(request.getSession(), userId)) {
            throw new NotAuthorizedUserException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        User user = userService.findByUserId2(userId)
                .orElseThrow(() -> NotExistUserException.fromUserId(userId));

        ModelAndView mav = jspView("/user/updateForm.jsp");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        String userId = request.getParameter("userId");
        // 서비스에는 어느정도까지 정보를 제공하면 좋을지
        if (loginService.isNotLoginedUser(request.getSession(), userId)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        UserUpdatedDto updateUser = readUserUpdatedDto(request);
        userService.update(userId, updateUser);

        log.debug("updateUser: {}", updateUser);
        log.debug("user: {}", userService.findByUserId(userId));


        return jspView("redirect:/");
    }

    private UserCreatedDto readUserCreatedDto(HttpServletRequest request) {
        return new UserCreatedDto(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
    }

    private UserUpdatedDto readUserUpdatedDto(HttpServletRequest req) {
        return new UserUpdatedDto(
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
    }
}
