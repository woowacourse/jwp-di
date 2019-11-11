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

### 보강해야할 부분
- [ ] TopologySortTest
    - 현재 생성시 한번만 order 를 계산해놓음
        - 테스트코드를 통해서 의도 보여주기

### 구현하면서 필요하다고 생각된 기능
- [ ] 로깅 + aop