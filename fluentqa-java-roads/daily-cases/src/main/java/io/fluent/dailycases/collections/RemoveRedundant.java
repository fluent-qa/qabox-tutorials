package io.fluent.dailycases.collections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RemoveRedundant {

    List<String> storedString = Arrays.asList("123", "456", "213", "123", "456");

    public List<DemoClass> setupRedundantList() {
        List<DemoClass> list = new ArrayList<>();
        list = new ArrayList<>();
        list.add(DemoClass.builder().api("api").name("api").path("/api").build());
        list.add(DemoClass.builder().api("api1").name("api1").path("/api").requestBody("test").build());
        list.add(DemoClass.builder().api("api1").name("api2").path("/api").requestBody("test").build());
        list.add(DemoClass.builder().api("api3").name("api3").path("/api3").requestBody("test").build());
        list.add(DemoClass.builder().api("api").name("api").path("/api").requestBody("test").build());
        return list;
    }

    public Comparator<DemoClass> setupComparator() {
        return Comparator.comparing(DemoClass::getApi).thenComparing(DemoClass::getPath)
                .thenComparing(DemoClass::getRequestBody);
    }

    public void removeRedundantString() {
        HashSet<String> distinct = storedString.stream().distinct().collect(
                Collectors.toCollection(HashSet::new)
        );
        System.out.println(distinct.size());
        distinct.forEach(System.out::println);
    }

    public <T> void removeRedundantObject() {
        List<DemoClass> objects = setupRedundantList();
        Comparator<DemoClass> comparator = setupComparator();
        ArrayList<DemoClass> distinct = objects.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<DemoClass>(
                                comparator
                        )), ArrayList::new
                )
        );
        System.out.println(distinct.size());
        distinct.forEach(System.out::println);
    }

    public <T> void removeRedundantGeneralizedObject(List<T> objects, Comparator<T> comparator) {
        ArrayList<T> distinct = objects.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<T>(
                                comparator
                        )), ArrayList::new
                )
        );
        System.out.println(distinct.size());
        distinct.forEach(System.out::println);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return new Predicate<T>() {
            @Override
            public boolean test(T t) {
                return seen.add(keyExtractor.apply(t));
            }
        };
    }

    public void filterToReduceRedundant() {
        List<DemoClass> objects = setupRedundantList();
        List<DemoClass> result = objects.stream().filter(distinctByKey(new Function<DemoClass, String>() {
            @Override
            public String apply(DemoClass demoClass) {
                return demoClass.getApi() +
                        "-" + demoClass.getPath()
                        + "-" + demoClass.getRequestBody();
            }
        })).toList();
        System.out.println(result.size());
        result.forEach(System.out::println);
    }

}
