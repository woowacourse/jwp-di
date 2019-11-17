package nextstep.di.bean;

public interface BeanDefinition {

    /**
     * 해당 Bean 의 Class 타입을 가져옵니다.
     *
     * @return bean 의 클래스 타입
     */
    Class<?> getClazz();

    /**
     * 해당 Bean 의 파라미터의 Class 타입을 가져옵니다.
     *
     * @return bean parameters 의 클래스 타입
     */
    Class<?>[] getParametersType();

    /**
     * 해당 Bean 을 파라미터들로 인스턴스화 합니다.
     *
     * @param parameters Bean 의 파라미터
     * @return bean 의 인스턴스
     */
    Object invoke(Object[] parameters);
}
