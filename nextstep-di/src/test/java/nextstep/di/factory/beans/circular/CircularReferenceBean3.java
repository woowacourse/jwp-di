package nextstep.di.factory.beans.circular;

import nextstep.stereotype.Controller;

@Controller
public class CircularReferenceBean3 {
    private CircularReferenceBean circularReferenceBean;

    public CircularReferenceBean3(CircularReferenceBean circularReferenceBean) {
        this.circularReferenceBean = circularReferenceBean;
    }
}
