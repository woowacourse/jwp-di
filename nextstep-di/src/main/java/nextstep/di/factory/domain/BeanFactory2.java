package nextstep.di.factory.domain;

import nextstep.di.factory.support.Beans;

public class BeanFactory2 {
    private Beans beans;

    public BeanFactory2() {
        this.beans = new Beans();
    }

    public void addBean(Class<?> clazz, Object instance) {
        beans.put(clazz, () -> instance);
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) beans.get(clazz);
    }
}
