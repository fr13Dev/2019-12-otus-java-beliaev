package ru.otus.core.cachehw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class MyCacheTest {
    private final HwCache<Long, User> cache = new MyCache<>();
    private static final int SIZE = 1_000;

    @Test
    @DisplayName(" должен проверить наличие объекта в кэше до gc и отсутствие объекта в кэше после gc")
    public void shouldCheckObjectAvailabilityBeforeGcAndCheckObjectFailureAfterGc() {
        for (int i = 0; i < SIZE; i++) {
            cache.put((long) i, new User());
        }
        var objBeforeGc = cache.get(1L);
        assertThat(objBeforeGc).isNotNull();
        System.gc();
        var objAfterGc = cache.get(SIZE - 1L);
        assertThat(objAfterGc).isNull();
    }

    @Test
    @DisplayName(" должен добавить подписчика и удалить его")
    public void shouldAddAndRemoveListener() {
        HwListener<Long, User> listener = (new HwListener<>() {
            @Override
            public void notify(Long key, User value, String action) {
                System.out.println(String.format("listener in action: %s", action));
            }
        });
        cache.addListener(listener);
        cache.put(1L, new User());
        cache.get(1L);
        cache.removeListener(listener);
        cache.get(1L);
    }
}