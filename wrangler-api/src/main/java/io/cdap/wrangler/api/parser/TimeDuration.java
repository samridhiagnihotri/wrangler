package io.cdap.wrangler.api.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Token to represent time durations like "10ms", "2.5s", "3m", "1h".
 */
public class TimeDuration implements Token {
    private static final Pattern TIME_PATTERN = Pattern.compile("(?i)(\\d+(\\.\\d+)?)(ns|us|ms|s|m|h)");

    private final double originalValue;
    private final String unit;

    public TimeDuration(String value) {
        Matcher matcher = TIME_PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time duration format: " + value);
        }
        this.originalValue = Double.parseDouble(matcher.group(1));
        this.unit = matcher.group(3).toLowerCase(); // normalize to lowercase
    }

    /**
     * Converts to milliseconds.
     */
    public long getMillis() {
        switch (unit) {
            case "ns":
                return (long) (originalValue / 1_000_000);
            case "us":
                return (long) (originalValue / 1_000);
            case "ms":
                return (long) originalValue;
            case "s":
                return (long) (originalValue * 1_000);
            case "m":
                return (long) (originalValue * 60_000);
            case "h":
                return (long) (originalValue * 3_600_000);
            default:
                throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }

    /**
     * Converts to seconds.
     */
    public double getSeconds() {
        return getMillis() / 1000.0;
    }

    /**
     * Converts to nanoseconds.
     */
    public long getNanos() {
        switch (unit) {
            case "ns":
                return (long) originalValue;
            case "us":
                return (long) (originalValue * 1_000);
            case "ms":
                return (long) (originalValue * 1_000_000);
            case "s":
                return (long) (originalValue * 1_000_000_000);
            case "m":
                return (long) (originalValue * 60_000_000_000L);
            case "h":
                return (long) (originalValue * 3_600_000_000_000L);
            default:
                throw new IllegalArgumentException("Unknown time unit: " + unit);
        }
    }

    public String getOriginalUnit() {
        return unit;
    }

    public double getOriginalValue() {
        return originalValue;
    }

    public static TimeDuration parse(String input) {
        return new TimeDuration(input);
    }

    @Override
    public TokenType type() {
        return TokenType.TIME_DURATION;
    }

    @Override
    public String toString() {
        return originalValue + unit;
    }
}
