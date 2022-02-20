package annotation;

import java.lang.annotation.*;
import java.util.Arrays;

public class AnnotationInheritanceExample {
    public static void main(String[] args) {
        Class<? extends A> aClass = new A().getClass();
        Class<? extends B> bClass = new B().getClass();
        System.out.println(aClass + " " + Arrays.toString(aClass.getAnnotations()));
        System.out.println(bClass + "" + Arrays.toString(bClass.getAnnotations()));
    }

    @InheritedAnnotation
    @UninheritedAnnotation
    static class A {
    }

    static class B extends A {
    }

    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface InheritedAnnotation {

    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UninheritedAnnotation {

    }
}
