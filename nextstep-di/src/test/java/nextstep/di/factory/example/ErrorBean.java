package nextstep.di.factory.example;

public class ErrorBean {
    private ErrorBean2 errorBean2;

    public ErrorBean() {
    }

    public ErrorBean(ErrorBean2 errorBean2) {
        this.errorBean2 = errorBean2;
    }
}
