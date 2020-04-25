package ru.otus.appcontainer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.config.AppConfig;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName(" провряет корректность работы рефлексии")
class ReflectionTest {
    private final Reflection reflection = new Reflection(AppConfig.class);


    @Test
    @DisplayName(" должен вернуть отсортированный список аннотированных методов")
    public void shouldReturnSortedListOfAnnotatedMethods() {
        var methods = reflection.getSortedAnnotatedMethods();
        assertThat(methods).isNotEmpty()
                .isSortedAccordingTo(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .hasSize(4);
    }

}