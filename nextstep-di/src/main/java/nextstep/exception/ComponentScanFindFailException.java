package nextstep.exception;

public class ComponentScanFindFailException extends RuntimeException {
    private static final String COMPONENT_SCAN_FIND_FAIL_MESSAGE = "ComponentScan 어노테이션을 찾을 수 없습니다.";

    public ComponentScanFindFailException() {
        super(COMPONENT_SCAN_FIND_FAIL_MESSAGE);
    }
}
