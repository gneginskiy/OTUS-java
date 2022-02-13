package org.sampletestframework.annotation;

import java.util.Objects;

public class Assertions {
    public static void assertEquals(Object expected, Object actual){
        if (!Objects.equals(expected,actual)){
            throw new AssertionError("expected = " + expected +
                    " | actual = " + actual);
        }
    }
}
