# DI 프레임워크 구현

## 페어 규칙
1. 미리 요구사항을 정해놓고 각자 학습/생각하는 시간을 가지기.
2. 일일 요구사항 명세를 정하고 실천하기.
3. 드라이버는 30분 간격으로 교체한다.
4. TDD로 할 수 있다면 최대한 적용한다.

## step-1 요구사항

- @Service, @Repository 클래스 스캔
    
    1. 빈 주입시 Service가 다른 Service를 주입하는 경우 고려
        @Inject가 없는 경우 기본생성자를 이용하여 빈으로 등록
        그 외 경우는 initializeInjectedBeans로 빈 등록

- @Inject에 필요한 객체를 찾아서 주입 / 생성
- 기존의 @Controller를 스캔하는 ControllerScanner 클래스를 확장된 BeanScanner와 통합

## step-2 요구사항

- @Configuration 어노테이션으로 빈 설정
    
    1. @ComponentScan
    2. @Bean으로 빈 수동 생성
    
    @Configuration을 찾고 @ComponentScan의 값을 BeanScanner에 전달하여 빈 생성
    @Configuration도 빈으로 등록하여 내부의 @Bean 메서드 결과를 빈으로 등록한다.


### 의문사항
- `@ComponentScan` 어노테이션에 value와 basepackage가 공통된 정보를 나타내서 이를 어떻게 처리해야할지 궁금합니다.