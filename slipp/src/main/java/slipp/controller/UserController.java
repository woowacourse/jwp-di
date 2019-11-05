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
import slipp.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController extends AbstractNewController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Inject
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        if (!UserSessionUtils.isLogined(request.getSession())) {
            return jspView("redirect:/users/loginForm");
        }

        ModelAndView mav = jspView("/user/list.jsp");
        mav.addObject("users", userService.findAll());
        return mav;
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profile(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        ModelAndView mav = jspView("/user/profile.jsp");
        mav.addObject("user", userService.findByUserId(userId));
        return mav;
    }

    @RequestMapping(value = "/users/form", method = RequestMethod.GET)
    public ModelAndView form(HttpServletRequest request, HttpServletResponse response) {
        return jspView("/user/form.jsp");
    }

    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) {
        UserCreatedDto dto = new UserCreatedDto(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        log.debug("User : {}", dto);
        userService.insert(dto);
        return jspView("redirect:/");
    }

    @RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findByUserId(request.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(request.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        ModelAndView mav = jspView("/user/updateForm.jsp");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse response) {
        User user = userService.findByUserId(req.getParameter("userId"));

        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        UserUpdatedDto updateUser = new UserUpdatedDto(
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email"));
        log.debug("Update User : {}", updateUser);
        userService.update(user.getUserId(), updateUser);
        return jspView("redirect:/");
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
        return jspView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = userService.findByUserId(userId);

        if (user == null) {
            throw new NullPointerException("사용자를 찾을 수 없습니다.");
        }

        if (user.matchPassword(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return jspView("redirect:/");
        } else {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return jspView("redirect:/");
    }
}
