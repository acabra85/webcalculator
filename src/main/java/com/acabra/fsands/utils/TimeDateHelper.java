package com.acabra.fsands.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeDateHelper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
    public static LocalDateTime fromEpoch(long epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
    }

    public static String toFormattedString(LocalDateTime ldt) {
        return ldt.format(FORMATTER);
    }

    public static String asStringFromEpoch(long epoch) {
        return toFormattedString(fromEpoch(epoch));
    }
}
