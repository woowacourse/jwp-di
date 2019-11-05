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