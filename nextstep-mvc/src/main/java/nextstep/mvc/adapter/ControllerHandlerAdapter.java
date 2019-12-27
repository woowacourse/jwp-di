package nextstep.mvc.adapter;

import nextstep.mvc.Controller;
import nextstep.mvc.ModelAndView;
import nextstep.mvc.view.JspView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String forwardView = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(forwardView));
    }
}