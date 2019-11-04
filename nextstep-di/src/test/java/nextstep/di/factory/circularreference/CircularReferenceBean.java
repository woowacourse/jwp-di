package nextstep.di.factory.circularreference;

import nextstep.stereotype.Controller;

@Controller
public class CircularReferenceBean {
    public CircularReferenceBean(CircularReferenceBean2 circularReferenceBean2) {

    }
}
