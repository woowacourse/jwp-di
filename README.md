### di 프레임워크

### 요구사항
- [ ] slipp 코드에 di 프레임워크 적용
    - [x] `@Controller, @Service, @Repository` 등록
        - [x] Service 적용
            - [x] UserService
            - [x] LoginService
            - 어느정도 역할을 적용해줄까요?
        - [x] UserDao `@Respository` 등록
    - [ ]테스트 적용
        - [ ] 컨트롤러 테스트
            - 리턴된 modelAndView 를 확인하면 되지 않을까?!
                - 원하는 뷰와 모델을 잘 반환하는지
                - 너무 구현을 많이 아는 건 아닐까? (view 나 모델이 바뀌면 테스트도 같이 바뀌기에...ㄷㄷ)
                    - 그래도.. 필요한 것 같다.. ㄷ
            - UserController
            - LoginController

- [ ] beanFactory 생성시에 애러를 발생 시켜야 하는 경우?
    - [ ] 싸이클 (순환의존성)
        - [ ] 예외 발생
            - 어느 곳에서 순환 참조가 발생하는지
            - 어느 빈이 해당 문제를 발생 시키는지
    - 생성에 필요한 정보를 기록하는 부분이 필요한 것 같다..
        - 특정 어노테이션 스캔 이외에도 Configuration 등 여러가지가 동시에 등록되는 경우가 많다...
            - 특정 인스턴스가 생성될 때 필요한 정보(ex. di 되어야 하는 애들) 는 어떻게 알 수 있을까?
                - 생성 전에 필요한 정보가 모두 존재할까?
                - 정보를 어떤 형태로 포함할까?
    - [ ] `@Inject` 로 설정된 빈이 존재하지 않는 경우
        - 이 경우 어떤 곳에서 어떤 빈이 존재 하지 않는지 알려줄 수 있는 방안이 필요함
    - [ ] `@Inject` 로 설정된 곳에 가능한 빈이 여러개 존재할 경우
            - `@Qualifier` 구현하기

- [ ] `@Inject` 에 대한 대응
    - 해당 타입에 대해서 가능한 빈이 유일해야 함
        - 여러개일 때 beanName 과 동일한 빈이 등록되어 있다면?
        - `@Qulifier` 가 존재한다면?
            - 이런 애들이 더 많아진다면... 빈 생성시에 필요한 정보를 미리 셋팅해놓는 작업이 필요할 것 같구만

- [ ] `@Configuration` 을 통한 빈 등록
    - [ ] `@Component` 가 달린애들을 등록
        - 확장포인트?

### 내가 파악한? 혹은 구상하는 요구사항
- [ ] 현재는 생성시 필요한 정보가 모두 존재해야함 (생성자 기반 DI)
    - 생성자를 통해서만 di 가능 (`@Inject`)
        - setter 나 `@PostConstruct` 등은 현재는 잊자....
    - 순환 참조가 발생할 경우 애러 발생
        - 생성자를 통해서는 해결할 방법이 딱히 없음으로
- [ ] 빈은 이름으로 관리하는게 좋을듯
    - 타입은 거들뿐..!
        - 특정 타입에 대해서 여러개의 후보가 존재하면... 어떻게 처리할까요?

### 보강해야할 부분
- [ ] TopologySortTest
    - 현재 생성시 한번만 order 를 계산해놓음
        - 테스트코드를 통해서 의도 보여주기
- [ ] Properties 를 통한 등록
    - Datasource 를 채울 때 사용하기
    - 요구사항 참고하기 (Spring boot)
        - http://www.appsdeveloperblog.com/reading-application-properties-spring-boot/
- [ ] 특정 인터페이스 + 여러 구현체
    - 해당 타입(인터페이스)에 대해서 여러 구현체가 존재할 때 어떻게 처리할지
        - https://blog.outsider.ne.kr/777

### 구현하면서 필요하다고 생각된 기능
- [ ] 로깅 + aop
- [ ] beanFactory 관리
    - 현재 요구사항(slipp 에서 사용하는 부분)에서는 한 곳에서만 beanFactory 를 생성한다. 그리고 이후에 다른 곳에서 beanFactory 를 사용하지 않는다
        - 보통 이런 식으로 사용되나? (생성하는 곳이 한 군데?)
            - 이후에 다른 곳에서 필요할 때는 di 되는 방식일까? (`@ Autowired ApplicationContext 같이?)
- [ ] 트랜잭션... (개인적으로 이번 기회에 꼭 구현해보고 싶구먼...)

### 구현하면서 집중하고 싶은 것들
- 각 기능들이 왜 필요한지를 살펴보고 이해가 되면 구현해보자
    - 그렇지 않고 무작정 스프링을 따라하기엔.. 너무 방대하다...
- slipp 에 필요한 것이 무엇인지 생각하면서 요구사항을 늘려갑시다

### 궁금한 부분들
- 스프링은 프록시를 어떤 부분에서 적용할까?
- BeanPostProcessor 가 존재하는 이유
- 어떤 확장 포인트들을 두고 있는 걸까?
    - di 를 할 때, 공통적으로 확장이 필요한 부분이 무엇일까?
    - https://blog.outsider.ne.kr/776
        - BeanPostProcessor, BeanFactoryPostProcessor
        - FactoryBean
- `@Bean` 에서 inner-bean-reference 가 어떻게 동작하는건가?? ㅠㅠ
    - 프록시를 사용하는 것 같은데.. 아직은 짐작이 안간다...