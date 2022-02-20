package samples;


import org.sampletestframework.annotation.AfterAll;
import org.sampletestframework.annotation.Assertions;
import org.sampletestframework.annotation.BeforeAll;
import org.sampletestframework.annotation.Test;

public class CalculatorTest {
    Calculator calculator;

    @BeforeAll
    public void setUp() {
        calculator = new Calculator();
        System.err.println("BeforeAll teardown called");
    }

    @Test
    public void testSum() {
        System.out.println(this.hashCode());
        Assertions.assertEquals(4, calculator.sum(2, 2));
    }

    @Test
    public void testSubtract() {
        System.out.println(this.hashCode());
        Assertions.assertEquals(4, calculator.subtract(6, 2));
    }

    @Test
    public void testMultiply() {
        System.out.println(this.hashCode());
        Assertions.assertEquals(4, calculator.multiply(2, 2));
    }

    @Test
    public void testDivide1() {
        System.out.println(this.hashCode());
        Assertions.assertEquals(4, calculator.divide(8, 2));
    }

    @Test(shouldThrow = ArithmeticException.class)
    public void testDivide2() {
        System.out.println(this.hashCode());
        calculator.divide(8, 0);
    }

    @AfterAll
    public void tearDown() {
        calculator = null; //not needed
        System.err.println("afterAll teardown called");
    }

}
