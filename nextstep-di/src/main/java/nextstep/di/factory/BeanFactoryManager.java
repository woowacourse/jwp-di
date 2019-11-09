package nextstep.di.factory;

public class BeanFactoryManager {
    @SuppressWarnings("unchecked")
    public static BeanFactory createBeanFactory(Object... basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.addBeans(beanScanner.getPreInstantiateBeans());
        return beanFactory;
    }
}
