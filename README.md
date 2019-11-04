# DI 프레임워크 구현

1. 생성자 찾기
- @Controller, @Service, @Repository가 붙은 클래스를 찾는다.
- 찾은 클래스에서 @Inject가 있는 생성자를 찾는다.
- 없으면 생성자의 개수를 판단한다.
- 생성자의 개수가 하나이면 그 생성자로 생성하고 그 이상이면 에러를 발생시킨다.

2. 객체 생성하기
- 생성자의 파라미터를 확인한다.
- 파라미터로 넣을 객체가 beans에 있는지 확인한다.
- 없으면 그 클래스로 2.객체 생성하기를 재귀 호출한다.
- 생성자로 객체를 생성한다.

3. 추가 요구사항
- MVC 프레임워크에 있는 ControllerScanner를 DI 프레임워크로 이동
- @Controller, @Service, @Repository에 대한 지원이 가능하도록 개선
- BeanScanner로 이름을 리팩토링
- MVC 프레임워크의 AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩토링