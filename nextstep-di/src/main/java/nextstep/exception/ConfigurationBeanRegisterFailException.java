package nextstep.exception;

public class ConfigurationBeanRegisterFailException extends RuntimeException {
    private static final String CONFIGURATION_BEAN_REGISTER_FAIL_MESSAGE = "설정 클래스의 빈 메소드의 리턴타입이 없습니다.";

    public ConfigurationBeanRegisterFailException() {
        super(CONFIGURATION_BEAN_REGISTER_FAIL_MESSAGE);
    }
}
