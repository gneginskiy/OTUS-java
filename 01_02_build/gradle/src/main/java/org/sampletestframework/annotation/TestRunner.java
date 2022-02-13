package org.sampletestframework.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TestRunner {

    public static void main(String... args) {
        String paths = args[0];
        getClassesFromPath(paths).forEach(TestRunner::processTestForClass);
    }

    private static void processTestForClass(Class aClass) {
        List<Method> declaredMethods = Arrays.asList(aClass.getDeclaredMethods());

        declaredMethods
                .parallelStream()
                .filter(TestRunner::isTestCase)
                .sorted(Comparator.comparing(Method::getName))
                .forEach(method -> {
                    Object instance = createInstance(aClass);
                    runMethodWithAnnotation(instance, declaredMethods, BeforeAll.class);
                    runMethodWithAnnotation(instance, declaredMethods, BeforeEach.class);
                    runTestCase(instance, method);
                    runMethodWithAnnotation(instance, declaredMethods, AfterEach.class);
                    runMethodWithAnnotation(instance, declaredMethods, AfterAll.class);
                });
    }

    private static void runTestCase(Object instance, Method method) {
        boolean isSuccess = true;
        String expectedExceptionSimpleName = method.getAnnotation(Test.class).shouldThrow();
        String actualExceptionName = "";
        try {
            runMethod(instance, method);
        } catch (Throwable e) {
            actualExceptionName = e.getClass().getSimpleName();
            if (!actualExceptionName.equals(expectedExceptionSimpleName)) {
                isSuccess = false;
                e.printStackTrace();
            }
        }
        if (!actualExceptionName.equals(expectedExceptionSimpleName)) {
            isSuccess = false;
        }
        System.out.println(
                method.getDeclaringClass().getTypeName() + "#" + method.getName() + " " +
                        (isSuccess ? "SUCCESSFUL" : "FAILED")
        );
    }

    private static boolean isTestCase(Method method) {
        return method.isAnnotationPresent(Test.class);
    }

    private static void runMethodWithAnnotation(Object instance,
                                                List<Method> declaredMethods,
                                                Class annotationClass) {
        declaredMethods
                .stream()
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .findFirst()
                .ifPresent(method -> runMethod(instance, method));
    }

    private static void runMethod(Object instance, Method method) {
        try {
            method.invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            sneakyThrow(e.getCause());
        }
    }

    private static Object createInstance(Class aClass) {
        try {
            return aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<? extends Class<?>> getClassesFromPath(String path) {
        return Arrays.stream(path.split(";"))
                .map(TestRunner::getaClass)
                .filter(Objects::nonNull)
                .toList();
    }

    private static Class<?> getaClass(String a)  {
        try {
            return Class.forName(a);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

}
