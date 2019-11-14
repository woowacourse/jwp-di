package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.example.component.*;
import nextstep.di.factory.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {
    private BeanFactory beanFactory;

    @Test
    public void recursiveReferenceException() {
        beanFactory = new BeanFactory(Maps.asMap(Sets.newHashSet(RecursiveController.class), ClassBeanCreator::new));
        assertThrows(RecursiveFieldException.class, () -> beanFactory.initializeBeans());
    }

    @Test
    public void noDefaultConstructorException() {
        beanFactory = new BeanFactory(Maps.asMap(Sets.newHashSet(NoDefaultCtorController.class), ClassBeanCreator::new));
        assertThrows(NoDefaultConstructorException.class, () -> beanFactory.initializeBeans());
    }

    @Test
    public void implClassNotFoundException() {
        beanFactory = new BeanFactory(Maps.asMap(Sets.newHashSet(NoImplService.class), ClassBeanCreator::new));
        assertThrows(ImplClassNotFoundException.class, () -> beanFactory.initializeBeans());
    }

    @Test
    public void interfaceCannotInstantiatedException() {
        beanFactory = new BeanFactory(Maps.asMap(Sets.newHashSet(NoImplRepository.class), ClassBeanCreator::new));
        assertThrows(InterfaceCannotInstantiatedException.class, () -> beanFactory.initializeBeans());
    }

    @Test
    public void primitiveTypeInjectionFailException() {
        beanFactory = new BeanFactory(Maps.asMap(Sets.newHashSet(PrimitiveTypeInjectController.class), ClassBeanCreator::new));
        assertThrows(PrimitiveTypeInjectionFailException.class, () -> beanFactory.initializeBeans());
    }

    @Test
    public void interfaceExtendsInterfaceSuccess() {
        beanFactory = new BeanFactory(Maps.asMap(Sets.newHashSet(NoImplService.class, ImplIntermediateRepository.class), ClassBeanCreator::new));
        assertDoesNotThrow(() -> beanFactory.initializeBeans());
    }
}