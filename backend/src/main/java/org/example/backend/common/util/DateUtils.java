package org.example.backend.common.util;

import java.time.Instant;

public final class DateUtils {

    private DateUtils() {
    }

    public static String now() {
        return Instant.now().toString();
    }
}