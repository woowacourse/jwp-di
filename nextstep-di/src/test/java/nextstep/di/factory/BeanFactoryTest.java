package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nextstep.di.factory.beancreator.ClassBeanCreator;
import nextstep.di.factory.example.component.*;
import nextstep.di.factory.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanFactoryTest {

    @Test
    public void recursiveReferenceException() {
        assertThrows(RecursiveFieldException.class, () ->
                new BeanFactory(Maps.asMap(Sets.newHashSet(RecursiveController.class), ClassBeanCreator::new)));
    }

    @Test
    public void noDefaultConstructorException() {
        assertThrows(NoDefaultConstructorException.class, () ->
                new BeanFactory(Maps.asMap(Sets.newHashSet(NoDefaultCtorController.class), ClassBeanCreator::new)));
    }

    @Test
    public void implClassNotFoundException() {
        assertThrows(ImplClassNotFoundException.class, () ->
                new BeanFactory(Maps.asMap(Sets.newHashSet(NoImplService.class), ClassBeanCreator::new)));
    }

    @Test
    public void interfaceCannotInstantiatedException() {
        assertThrows(InterfaceCannotInstantiatedException.class, () ->
                new BeanFactory(Maps.asMap(Sets.newHashSet(NoImplRepository.class), ClassBeanCreator::new)));
    }

    @Test
    public void primitiveTypeInjectionFailException() {
        assertThrows(PrimitiveTypeInjectionFailException.class, () ->
                new BeanFactory(Maps.asMap(Sets.newHashSet(PrimitiveTypeInjectController.class), ClassBeanCreator::new)));
    }

    @Test
    public void interfaceExtendsInterfaceSuccess() {
        assertDoesNotThrow(() ->
                new BeanFactory(Maps.asMap(Sets.newHashSet(NoImplService.class, ImplIntermediateRepository.class), ClassBeanCreator::new)));
    }
}