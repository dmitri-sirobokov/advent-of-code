package dms.adventofcode;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Named.named;

class TestInputProvider implements ArgumentsProvider {

    // private TestInput[] testInputs;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        var testInputs = context.getElement()
                .map(e -> e.getAnnotation(TestInputs.class))
                .map(t -> Arrays.asList(t.value()))
                .orElse(new ArrayList<>());
        if (testInputs.isEmpty()) {
            context.getElement()
                    .map(e -> e.getAnnotation(TestInput.class))
                    .ifPresent(testInputs::add);
        }

        return testInputs.stream().map(testInput ->
                {
                    try {
                        return Arguments.of(
                                named(testInput.input(), TestBase.readResourceFile(testInput.input())),
                                testInput.expected().isEmpty() ? testInput.expected() : named(testInput.expected(), testInput.expected())
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }


}