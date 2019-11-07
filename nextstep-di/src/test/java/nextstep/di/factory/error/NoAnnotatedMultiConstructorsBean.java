package nextstep.di.factory.error;

@BadBean
public class NoAnnotatedMultiConstructorsBean {
    private ExampleBean exampleBean;

    public NoAnnotatedMultiConstructorsBean() {
    }

    public NoAnnotatedMultiConstructorsBean(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
