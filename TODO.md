# 프레임워크 구현

### todo



### 고려할 점들

- BeanFactory 에서 '@Component 이 안 달린 클래스'를 생성해야 할 경우
    - @Resource 등 처리
- @Bean 이라는 것이 무엇인지?
- 사이클 있는 경우
    - 애러처리
        - 좀 더 좋은 예외(사이클에 포함된 클래스들 표시)

todo
- 추가요구사항
    - slipp 코드에 di 프레임워크 적용
        - ControllerScanner 를 di 프레임워크로 이동
        - 이후 BeanScanner 로 리팩토링(@Service, @Repository 등도 적용하기 위함)



컴포넌트 스캔?!

에공... beanScanner 가 하는 역할이 유니가 말했던 것인듯하구먼..
- 결국엔... beanScanner 로 찾아서 BeanFactory 에 등록해주는??
- @Configuration 으로 등록된 클래스를 찾아서
    - 내부에 등록된 @Bean 을 처리하는 것도 비슷한 경우이려나?
    - + @ComponentScan 으로 특정 경로에 있는 것들을 더 찾아주고

- 그렇다면 spring boot 에서 @Controller, @Service, @Repository 는 누가 찾아준걸까? 왜 찾아준걸까?

- 어떻게보면... scan 을 하고서... 해당 결과를 가지고 생성?
    - 지금으로 보면 typeScanner 를 적용했는데... 이 부분이 해당 부분인듯하다...
        - 그렇다면 typeScanner 에 들어갈 패키지 주소가 모두가 되는건가?
            - 

- 결국 @Controller 도 @Component
    - 그렇다면 

- 프로퍼티 (spring boot application properties)
https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-application-properties.html

https://engkimbs.tistory.com/765

- 내가 만든 datasource 등록하기
    - https://jdm.kr/blog/230

- coponentScan 예시
    - https://dzone.com/articles/spring-component-scan

???
그렇다면 스프링 부트 앱은 (@SpringBootApplication) 누가 빈에 등록하는 걸까? (이 친구도 @Configuration 인 건데...)

@Configuration
@EnableAutoConfiguration
    - 이 친구가 하는 역할은?
@ComponentScan
    - default 설정이 .. @Configuration 이 존재하는 패키지와 그 자식 패키지들을 검사한다고 합니다.

https://www.baeldung.com/spring-component-scanning
- 결국 @SpringBootApplication 이 달린 클래스도 빈임을 알 수 있음 (@Configuration 달리면 빈인 것 처럼)


@SpringBootApplication vs @EnableAutoConfiguration
-https://www.java67.com/2018/05/difference-between-springbootapplication-vs-EnableAutoConfiguration-annotations-Spring-Boot.html
- https://www.java67.com/2012/08/what-is-path-and-classpath-in-java-difference.html
    - classpath 라는게 무엇인가?? (@EnableAutoConfiguratin 이 이를 이용한다는데...)
    - 그렇다면 내 spring boot 앱애서는 ... gradle 에 적용된 애들은... 기본적으로 autoConfig 가 되는건지?
        - application.properties 와 해깔린다...
        - 즉... gradle 에 적용한 애들은 classpath 에 추가된 애들처럼 인식하는지..
            - org.springframework.boot:spring-boot-starter-web 이런애들이 필요한 jar 를 classpath 에 추가해주는 듯(https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started)
            < The second class-level annotation is @EnableAutoConfiguration. This annotation tells Spring Boot to “guess” how you want to configure Spring, based on the jar dependencies that you have added. Since spring-boot-starter-web added Tomcat and Spring MVC, the auto-configuration assumes that you are developing a web application and sets up Spring accordingly. >
- https://www.javadevjournal.com/spring-boot/how-spring-boot-auto-configuration-works/
// 어떻게 검증할 수 있을까? 확인할 수 있을까?


스프링부트 문서!
https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-structuring-your-code

### todo
현재는 @Component 가 존재하지 않는다
    - 즉... @Controller, @Service, @Configuration 등 빈으로 인식하는 애들을 구분할 방법이 존재하지 않는다
        - 스프링 부트에서는 @Component 를 찾으니... 
