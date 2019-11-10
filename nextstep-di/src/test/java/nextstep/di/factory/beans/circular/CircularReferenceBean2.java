package nextstep.di.factory.beans.circular;

import nextstep.stereotype.Controller;

@Controller
public class CircularReferenceBean2 {
    private CircularReferenceBean2 circularReferenceBean3;

    public CircularReferenceBean2(CircularReferenceBean2 circularReferenceBean3) {
        this.circularReferenceBean3 = circularReferenceBean3;
    }
}
