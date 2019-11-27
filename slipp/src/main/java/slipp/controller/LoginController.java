package slipp.controller;


import nextstep.annotation.Inject;
import nextstep.mvc.ModelAndView;
import nextstep.mvc.tobe.AbstractNewController;
import nextstep.stereotype.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slipp.dto.UserLoginDto;
import slipp.exception.LoginFailException;
import slipp.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController extends AbstractNewController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    @Inject
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
    public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        return jspView("/user/login.jsp");
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        UserLoginDto userLoginDto = readUserLoginDto(request);
        if (!loginService.login(request.getSession(), userLoginDto)) {
            throw LoginFailException.from(userLoginDto.getUserId(), userLoginDto.getPassword());
        }
        return jspView("redirect:/");
    }

    private UserLoginDto readUserLoginDto(HttpServletRequest request) {
        return new UserLoginDto(request.getParameter("userId"), request.getParameter("password"));
    }

    @RequestMapping(value = "/users/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("begin");

        loginService.logout(request.getSession());
        return jspView("redirect:/");
    }
}
