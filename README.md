# DI 프레임워크 구현

## 페어 규칙
1. 미리 요구사항을 정해놓고 각자 학습/생각하는 시간을 가지기.
2. 일일 요구사항 명세를 정하고 실천하기.
3. 드라이버는 30분 간격으로 교체한다.
4. TDD로 할 수 있다면 최대한 적용한다.

## 요구사항

- @Service, @Repository 클래스 스캔
    
    1. 빈 주입시 Service가 다른 Service를 주입하는 경우 고려
        @Inject가 없는 경우 기본생성자를 이용하여 빈으로 등록
        그 외 경우는 initializeInjectedBeans로 빈 등록

- @Inject에 필요한 객체를 찾아서 주입 / 생성
- 기존의 @Controller를 스캔하는 ControllerScanner 클래스를 확장된 BeanScanner와 통합

## TO DO
- `BeanFactoryUtils.getInjectedConstructor` 메서드에서 `@Inject`가 붙은 생성자가 여러개일 때 처리 방법 고민 
-  BeanScannerTest 추가
    - base 패키지 바깥의 클래스를 스캔하지 않는지 확인
    - 다른 종류의 애노테이션이 선언된 클래스를 스캔하지 않는지 확인