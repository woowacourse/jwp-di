package nextstep.di.factory;

public class BeanFactoryManager {
    @SuppressWarnings("unchecked")
    public static void addBeans(Object... basePackage) {
        BeanScanner beanScanner = new BeanScanner(basePackage);
        BeanFactory beanFactory = BeanFactory.getInstance();
        beanFactory.addBeans(beanScanner.getPreInstantiateBeans());
    }
}
