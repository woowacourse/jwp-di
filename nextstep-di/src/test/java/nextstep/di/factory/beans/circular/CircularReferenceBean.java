package nextstep.di.factory.beans.circular;

import nextstep.stereotype.Controller;

@Controller
public class CircularReferenceBean {
    private CircularReferenceBean2 circularReferenceBean2;

    public CircularReferenceBean(CircularReferenceBean2 circularReferenceBean2) {
        this.circularReferenceBean2 = circularReferenceBean2;
    }
}
