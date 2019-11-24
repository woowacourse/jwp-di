package nextstep.di.factory;

import java.util.Objects;

public class TestBeanDefinition {
    private Class<?> clazz;

    public TestBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBeanDefinition that = (TestBeanDefinition) o;
        return Objects.equals(clazz, that.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz);
    }

    @Override
    public String toString() {
        return "TestBeanDefinition{" +
                "clazz=" + clazz +
                '}';
    }
}
