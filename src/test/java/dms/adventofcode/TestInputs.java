package dms.adventofcode;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestInputs {
    TestInput[] value();
}
