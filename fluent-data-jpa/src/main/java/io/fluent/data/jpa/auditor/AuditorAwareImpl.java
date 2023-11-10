package io.fluent.data.jpa.auditor;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // use Spring Security to retrieve the currently logged-in user(s)
        return Optional.of(Arrays.asList("test1", "test2", "test3")
                .get(new Random().nextInt(3)));
    }

}
