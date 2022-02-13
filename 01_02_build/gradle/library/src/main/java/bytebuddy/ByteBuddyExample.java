package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

public class ByteBuddyExample {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        generateAndLoadBytecodeInRuntime();

        DynamicType.Loaded<MockBaseClass> loaded = new ByteBuddy()
                .subclass(MockBaseClass.class)
                .method(isDeclaredBy(MockBaseClass.class))
                .intercept(MethodDelegation.to(Replacement.class))
                .make()
                .load(MockBaseClass.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        MockBaseClass instance = loaded.getLoaded().getDeclaredConstructor().newInstance();
        instance.ouch();
    }

    private static void generateAndLoadBytecodeInRuntime() throws InstantiationException, IllegalAccessException {
        DynamicType.Unloaded unloadedType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.isToString())
                .intercept(FixedValue.value("Hello World ByteBuddy!"))
                .make();

        Class<?> dynamicType = unloadedType.load(ByteBuddyExample.class
                        .getClassLoader())
                .getLoaded();

        System.out.println(dynamicType.newInstance().toString());
    }

    public static class Replacement {
        public static void ouch() {
            System.out.println("REPLACED!");
        }
    }
}
