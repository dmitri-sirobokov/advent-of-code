package dms.adventofcode;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = TestInputs.class)
@ArgumentsSource(TestInputProvider.class)
public @interface TestInput {

    /**
     * The name of the static variable
     */
    String input();

    String expected() default "0";
}

