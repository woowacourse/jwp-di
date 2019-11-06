package nextstep.di.factory.beans.noerror;

import nextstep.stereotype.Controller;

@Controller
public class OneConstructorBean {
    private ExampleBean exampleBean;

    public OneConstructorBean(ExampleBean exampleBean) {
        this.exampleBean = exampleBean;
    }
}
