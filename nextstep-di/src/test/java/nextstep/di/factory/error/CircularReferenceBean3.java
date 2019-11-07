package nextstep.di.factory.error;

@Circular
public class CircularReferenceBean3 {
    private CircularReferenceBean circularReferenceBean;

    public CircularReferenceBean3(CircularReferenceBean circularReferenceBean) {
        this.circularReferenceBean = circularReferenceBean;
    }
}
