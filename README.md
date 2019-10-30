# DI 프레임워크 구현

## 페어 규칙
1. 미리 요구사항을 정해놓고 각자 학습/생각하는 시간을 가지기.
2. 일일 요구사항 명세를 정하고 실천하기.
3. 드라이버는 30분 간격으로 교체한다.
4. TDD로 할 수 있다면 최대한 적용한다.

## 요구사항

- @Service, @Repository 클래스 스캔
- @Inject에 필요한 객체를 찾아서 주입 / 생성
- 기존의 @Controller를 스캔하는 ControllerScanner 클래스를 확장된 BeanScanner와 통합
