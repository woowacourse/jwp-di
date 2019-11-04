package nextstep.di.factory.error;

@Circular
public class CircularReferenceBean {
    private CircularReferenceBean2 circularReferenceBean2;

    public CircularReferenceBean(CircularReferenceBean2 circularReferenceBean2) {
        this.circularReferenceBean2 = circularReferenceBean2;
    }
}
