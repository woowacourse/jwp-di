package nextstep.di.factory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    private Set<Class<?>> visited;

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        visited = Sets.newHashSet();

        for (Class<?> root : preInstanticateBeans) {
            if (beans.containsKey(root)) {
                continue;
            }

            try {
                fill(root);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    // beans
    private void fill(Class<?> from) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (visited.contains(from)) {
            if (!beans.containsKey(from)) {
                throw new IllegalArgumentException("사이클..!!");
            }

            return ;
        }
        visited.add(from);

        List<Class<?>> toNodes = toNodes(from);
        for (Class<?> to : toNodes) {
            fill(to);
        }

        // 생성자 채우기
        beans.put(from, from.getConstructors()[0].newInstance(intances(toNodes)));
    }

    private Object[] intances(List<Class<?>> toNodes) {
        return toNodes.stream()
                .map(node -> beans.get(node))
                .collect(Collectors.toList()).toArray();
    }

    // from 이란 애를 받으면
    // @Injects 가 달린 생성자에서 요구하는 클래스들을 리턴
    // 없으면 빈 리스트 리턴
    public List<Class<?>> toNodes(Class<?> from) {
        Constructor<?> constructor =  BeanFactoryUtils.getInjectedConstructor(from);
        if (constructor == null) {
            return Collections.emptyList();
        }

        // 파라미터 인터페이스가 존재할 수 있음
        // 등록된 구현체를 넣어주어야?
        return Arrays.asList(constructor.getParameterTypes()).stream()
                .map(parameter -> BeanFactoryUtils.findConcreteClass(parameter, preInstanticateBeans))
                .collect(Collectors.toList());
    }
}
