package nextstep.di.factory.beans.error;

import nextstep.di.factory.beans.noerror.ExampleBean;
import nextstep.stereotype.Controller;

@Controller
public class NoAnnotatedMultiConstructorsBean {
    private ExampleBean exampleBean;

    public NoAnnotatedMultiConstructorsBean() {
    }

    public NoAnnotatedMultiConstructorsBean(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
