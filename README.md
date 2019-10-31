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


## 의문사항
### 재귀 함수 리펙토링
`initializeInjectedBean2` 메서드가 리펙토링 전의 재귀함수인데, 현재 저희는 `initializeInjectedBean()` 메서드와 같이 메서드로 분리해서 리펙토링했습니다. 
그런데 이와 같이 하면 재귀 함수를 따라가기가 힘들다고 느껴졌습니다.
혹시 `initializeInjectedBean()`와 같이 분리하는 것이 좋은지, `initializeInjectedBean2()` 처럼 그대로 두는 것이 좋은지 리뷰어님의 생각이 궁금합니다.

### assert 구문
`inject()` 메서드에서 `injectedConstructor` 변수에 대한 NULL체크를 `assert`를 통해 하고 있습니다. 그런데 이전에 호출되는 함수인 `initializeNotInjectedBeans()` 메서드에서 
이미 NULL체크를 하고 있습니다. 그러면 NULL체크를 두 번 해야 하는 상황인데, 이렇게 중복해서 체크하더라도 명시적으로 `assert`를 통해 체크하는 것이 좋은지 리뷰어님의 생각이 궁금합니다.