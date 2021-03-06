package com.company;

import com.company.annotation.Autowired;
import com.company.annotation.Qualifier;
import com.company.annotation.Value;
import com.company.pojo.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class Container implements ApplicationContext{
    private Map<String, Object> name2obj;
    private Map<Class<?>, Object> class2obj;
    private Map<Class<?>, List<Class<?>>> interface2impl;

    public Container() throws Throwable {
        name2obj = new HashMap<>();
        class2obj = new HashMap<>();
        interface2impl = new HashMap<>();

        List<Class<?>> classes = getComponentClasses();
        for (Class<?> clazz : classes) {
            createBean(clazz);
        }
    }

    private Object createBean(String className) throws Throwable {
        Class<?> aClass = Class.forName(className);
        return createBean(aClass);
    }

    private Object createBean(Class<?> clazz) throws Throwable {
        if (class2obj.containsKey(clazz)) {
            return class2obj.get(clazz);
        }

        Constructor<?> constructor = clazz.getConstructor(null);
        Object object = constructor.newInstance();
        // inject dependency(field) one by one
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            // if the field is a dependency, inject recursively
            if (autowired != null) {
                Object dependency = null;
                Class<?> type = field.getType();
                // we have two cases, interface and class, because interface may have multiple implementation,
                // which is trickier

                // 1. interface, we have to make sure there aren't multiple impl, if it has multiple impl,
                // clients have to use qualifier annotation for providing exact impl.
                if (type.isInterface()) {
                    if (interface2impl.get(type).size() > 1 && field.isAnnotationPresent(Qualifier.class)){
                        String name = field.getAnnotation(Qualifier.class).value();
                        dependency = name2obj.get(name) == null ? createBean(name) : name2obj.get(name);
                    }
                    else if (interface2impl.get(type).size() == 1) {
                        Class<?> implementation = interface2impl.get(type).get(0);
                        dependency = class2obj.get(implementation) == null ? createBean(implementation) : class2obj.get(implementation);
                    }
                    else {
                        throw new RuntimeException("more than 1 implementation and no qualifier used");
                    }
                }
                // 2. class
                else {
                    dependency = class2obj.get(type) == null ? createBean(type) : class2obj.get(type);
                }

                field.setAccessible(true);
                field.set(object, dependency);
            }
            // this else clause is for ordinary values injection such as String, Integer etc.
            // for now we only provide String & Integer types injection
            else {
                Value value = field.getAnnotation(Value.class);
                if (value != null) {
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    // here I assume we only have int/integer and String type
                    if (type == int.class || type == Integer.class) {
                        field.set(object, Integer.valueOf(value.value()));
                    }
                    else {
                        field.set(object, value.value());
                    }
                }
            }
        }

        class2obj.put(clazz, object);
        name2obj.put(clazz.getSimpleName(), object);

        return object;
    }

    @Override
    public <T> T getBean(Class<?> clazz) {
        return (T)class2obj.get(clazz);
    }

    @Override
    public Object getBean(String className) {
        return name2obj.get(className);
    }

    // currently we have to add class manually, automatically scan is not yet implemented
    private List<Class<?>> getComponentClasses() {
        List<Class<?>> classes = Arrays.asList(Person.class, Address.class, Engineer.class,
                Sales.class, Manager.class);

        // scan and keep record of interface and its implementations
        for (Class<?> implementation : classes) {
            if (implementation.isInterface()) continue;
            Class<?>[] interfaces = implementation.getInterfaces();
            for (Class<?> interfaceClass : interfaces) {
                interface2impl.putIfAbsent(interfaceClass, new ArrayList<>());
                interface2impl.get(interfaceClass).add(implementation);
            }
        }

        return classes;
    }

}
