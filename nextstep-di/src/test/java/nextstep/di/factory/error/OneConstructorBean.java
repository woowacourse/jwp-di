package nextstep.di.factory.error;

@GoodBean
public class OneConstructorBean {
    private ExampleBean exampleBean;

    public OneConstructorBean(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
