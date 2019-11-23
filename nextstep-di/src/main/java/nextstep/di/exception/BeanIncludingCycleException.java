package nextstep.di.exception;

public class BeanIncludingCycleException extends RuntimeException {
    public BeanIncludingCycleException(Class<?> clazz) {
        super(String.format("사이클이 존재합니다. 해당 클래스를 확인해주세요. class: %s", clazz));
    }
}
