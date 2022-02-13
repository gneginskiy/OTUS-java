package annotation;

import java.lang.annotation.*;
import java.util.Arrays;

public class AnnotationInheritanceExample {

    public static void main(String[] args) {
        //2 annotations
        System.out.println(Arrays.toString(new A().getClass().getAnnotations()));
        //only 1 annotation
        System.out.println(Arrays.toString(new B().getClass().getAnnotations()));
    }

    @InheritedAnnotation
    @UninheritedAnnotation
    static class A {
    }

    static class B extends A{

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
