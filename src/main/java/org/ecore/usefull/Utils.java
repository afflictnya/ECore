package org.ecore.usefull;

import arc.func.Cons;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final Pattern timePattern = Pattern.compile("^(\\d+)([yYdDhHmMsS])$");
    public static String formatTime(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(pattern);
    }
    public static long toMillis(String input) {
        Matcher matcher = timePattern.matcher(input);
        if (!matcher.matches()) {
            return -1;
        }
        long value = Long.parseLong(matcher.group(1));
        return switch (matcher.group(2).toLowerCase()) {
            case "y", "л" -> value * 365L * 24 * 60 * 60;
            case "d", "д" -> value * 24L * 60 * 60;
            case "h", "ч" -> value * 60L * 60;
            case "m", "м" -> value * 60L;
            case "s", "с" -> value;
            default -> -1;
        } * 1000;
    }
    public static <T> T apply(T obj, Cons<T> func){
        func.get(obj);
        return obj;
    }
}
