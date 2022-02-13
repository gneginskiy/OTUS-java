package samples;

import framework.annotation.AfterAll;
import framework.annotation.BeforeAll;
import framework.annotation.Test;
import framework.Assertions;

public class SampleTest {
    Calculator calculator;

    @BeforeAll
    public void setUp() {
        calculator = new Calculator();
        System.err.println("BeforeAll teardown called");
    }

    @Test
    public void testSum() {
        Assertions.assertEquals(4, calculator.sum(2, 2));
    }

    @Test
    public void testSubtract() {
        Assertions.assertEquals(4, calculator.subtract(6, 2));
    }

    @Test
    public void testMultiply() {
        Assertions.assertEquals(4, calculator.multiply(2, 2));
    }

    @Test
    public void testDivide1() {
        Assertions.assertEquals(4, calculator.divide(8, 2));
    }

    @Test(shouldThrow = "ArithmeticException")
    public void testDivide2() {
         calculator.divide(8, 0);
    }

    @AfterAll
    public void tearDown() {
        calculator = null; //not needed
        System.err.println("afterAll teardown called");
    }

}
