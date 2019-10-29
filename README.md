# 프레임워크 구현

## 요구사항

* 효과적인 실습을 위해 애노테이션(nextstep.annotation, nextstep.stereotype 패키지),
DI가 설정되어 있는 예제 코드(nextstep.di.factory.example) 를 이용하여
테스트 코드(nextstep.di.factory.BeanFactoryTest)를 동작하도록 코드를 구현한다.
    * core.di.factory.BeanFactoryUtils 클래스를 이용한다.

* 인스턴스 생성
    * 자바 리플렉션 API
    * 자바 리플렉션 API를 추상화한 Spring 프레임워크에서 제공하는 org.springframework.beans.BeanUtils의 instantiateClass() 메소드

## 추가 요구사항

* MVC 프레임워크와의 통합
    * @Controller이 설정되어 있는 클래스를 찾는 ControllerScanner를 DI 프레임워크가 있는 패키지로 이동한다.
    * @Controller, @Service, @Repository에 대한 지원이 가능하도록 개선한다.

* 클래스 이름을 BeanScanner로 리팩토링한다.

* MVC 프레임워크의 AnnotationHandlerMapping이 BeanFactory와 BeanScanner를 활용해 동작하도록 리팩토링한다.