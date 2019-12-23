package nextstep.di.beandefinition;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanDefinitionRegistryTest {

    @Test
    @DisplayName("동일한 타입으로 찾기")
    void findByType_exactType() {
        Class<?> type = Car.class;
        BeanDefinition expectedDefinition = TypeBeanDefinition.of(type);
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry(Sets.newHashSet(Arrays.asList(
                expectedDefinition
        )));

        assertThat(registry.findByType(type))
                .hasSize(1)
                .contains(expectedDefinition);
    }

    @Test
    @DisplayName("인터페이스로 찾기")
    void findByType_fromInterface() {
        Class<?> type = Movable.class;
        BeanDefinition expectedDefinition = TypeBeanDefinition.of(Car.class);
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry(Sets.newHashSet(Arrays.asList(
                expectedDefinition
        )));

        assertThat(registry.findByType(type))
                .hasSize(1)
                .contains(expectedDefinition);
    }

    @Test
    @DisplayName("인터페이스로 찾기. 인터페이스를 구현한 타입이 여러 개 일 때")
    void findByType_fromInterface_findSeveral() {
        Class<?> type = Movable.class;
        Set<BeanDefinition> expectedDefinitions = Sets.newHashSet(Arrays.asList(
                TypeBeanDefinition.of(Car.class),
                TypeBeanDefinition.of(Bicycle.class)
        ));
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry(expectedDefinitions);

        assertThat(registry.findByType(type)).isEqualTo(expectedDefinitions);
    }

    @Test
    @DisplayName("부모 클래스로 찾기")
    void findByType_fromParent() {
        Class<?> type = Car.class;
        BeanDefinition expectedDefinition = TypeBeanDefinition.of(Bus.class);
        BeanDefinitionRegistry registry = new BeanDefinitionRegistry(Sets.newHashSet(Arrays.asList(
                expectedDefinition
        )));

        assertThat(registry.findByType(type))
                .hasSize(1)
                .contains(expectedDefinition);
    }

    interface Movable {
    }

    class Car implements Movable {
        public Car() {
        }
    }

    class Bus extends Car {
        public Bus() {
            super();
        }
    }

    class Bicycle implements Movable {
        public Bicycle() {
        }
    }
}