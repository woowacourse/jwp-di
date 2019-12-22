# 프레임워크 구현

### done
- [x] @Component 적용
    - 빈에 등록하기 위해 사용하는 어노테이션을 확장 가능하도록
        - 추가할 어노테이션에 @Component 를 달면 됨..!
- [x] @ComponentScan 적용
    - 디폴트 경로는 현재 위치
    - 경로 설정 가능
    - 스캔 중 새로운 @ComponentScan 이 발견되면 추가로 탐색
        - 해당 범위내에 존재하는 모든 @ComponentScan 이 적용될 때 까지 반복

### todo
- [ ] BeanDefinition 을 통한 등록
    - 빈을 생성하는 데 필요한 정보들(생성방식 포함)을 모아놓음(추상화해놓음)
    - 빈을 등록하는데 여러가지 방법이 존재하는데 이를 추상화했다고 생각하면 좋을 것 같다
        - 클래스를 통한 등록
        - @Bean 메소드를 통한 등록
            - 일단 DeclaringClass 도 필요 (+ 객체)
                - 의존한다고 생각하면 될 듯 (위상정렬을 적용할 때에)
                    - 즉.. Configuration 빈이 먼저 생성되어야 함
                    - @Bean 메소드에서 Configuration 에 @Inject 된 애들을 사용할 수 있기 때문(혹은 필드)
        - 생성을 할 때... 필요한 정보를 어떻게 제공해줄까? .... BeanFactory 에다가 필요한 빈들을 달라고 하면 되겠네 (타입으로 조회)
    - 무슨 정보가 필요할까?
        - 빈을 생성하는 방식
            - 아마 BeanFactory 가 보여야 할 것 같다
            - 생성하는 함수를 제공하는게 나을까?
                - 왜? 자기가 만들면 무엇이 안좋은데??
        - 생성시에 의존하는 빈을 찾기위한 정보
            - 현재는 타입으로 검색
            - 타입으로 특정 BeanDefinition 을 쉽게 찾을 수 있어야 할 듯

- [ ] BeanDefinitionRegister
    - 여러 매체를 통해서 그에 알맞는 BeanDefinition 을 생성하고 등록함
        - 결과로 BeanDefinitionRegistry 를 생성, 리턴
        
- [ ] BeanDefinitionRegistry
    - BeanDefinition 을 등록하고 조회하도록
    - 조회
        - 타입을 통한 조회
        - 위상정렬에서 사용된다면??
            - 의존하는 타입 정보들로 해당 BeanDefinition 을 찾아내야 함
- [ ] BeanFactory
    - 일단 Map<BeanDefinition, Object> 의 형태로 관리를 하면 어떨까?


### 생각나는 시나리오??

일단은 .. scanner 를 통해서 필요한 class 를 얻어내고 있다.
- 여기서 BeanDefinition 을 얻어내는게 좋을지?
    - 그냥 일단은 클래스만 얻어낼 수도 있을 것 같다
    - 클래스에서 BeanDefinition 을 만들어 내는 애는 또다른 역할을 가지는 것이랄까?
        - 나중에.. xml 에서 BeanDefinition 으로 만들어 낼 수 도 있는 거고..

- 현재는.. Register 를 통해서 등록하자
    - 결과로 registry 를 리턴하도록
    
- beanFactory 는 registry 를 가지고 빈을 생성하기
    - 위상정렬을 통한 생성 순서 정하기
        - validate
    - 순서대로 생성하기

또 나중에 Configuration 클래스에서는 @Bean 메소드를 통해서 BeanDefinition 을 추출해내야 한다. 이 작업을 어디서 해주는게 좋을까?
- 현재는 Configuration 을 생성할 때... 같이 등록시켜도 좋을 것 같다
    -  
    
    

### 내 시나리오를 검증하려면??
- 일단 간단한 케이스부터


### 작은 것 부터??
- 일단은 기존에 동작하던 애들이 돌도록 beanFactory 를 바꿔가는 것도 좋을 것 같다
    - BeanDefinition 을 만들면서 무엇이 필요한지도 볼 수 있도록
    - 전체적인 그림이 그려지도록
        - beanFactory 가 적용되는 과정??
        - 그리고나면.. 각 부분을 보강하는 느낌으로 구현이 되지 않을까?
            - 좀 더 ... 전반적인 부분이 돌아가는 형태로?
