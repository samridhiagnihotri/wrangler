package io.cdap.wrangler.api.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Token to represent byte sizes like "10KB", "1.5MB", "2GB", "512B".
 */
public class ByteSize implements Token {
    private static final Pattern BYTE_PATTERN = Pattern.compile("(?i)(\\d+(\\.\\d+)?)(b|kb|mb|gb|tb|pb)");

    private final double originalValue;
    private final String unit;

    public ByteSize(String value) {
        Matcher matcher = BYTE_PATTERN.matcher(value.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid byte size format: " + value);
        }
        this.originalValue = Double.parseDouble(matcher.group(1));
        this.unit = matcher.group(3).toLowerCase(); // normalize to lowercase
    }

    /**
     * Returns the size in bytes (canonical unit).
     */
    public long getBytes() {
        switch (unit) {
            case "b":
                return (long) originalValue;
            case "kb":
                return (long) (originalValue * 1024);
            case "mb":
                return (long) (originalValue * 1024 * 1024);
            case "gb":
                return (long) (originalValue * 1024 * 1024 * 1024);
            case "tb":
                return (long) (originalValue * 1024L * 1024 * 1024 * 1024);
            case "pb":
                return (long) (originalValue * 1024L * 1024 * 1024 * 1024 * 1024);
            default:
                throw new IllegalArgumentException("Unknown byte unit: " + unit);
        }
    }

    /**
     * Converts to MB for output (optional convenience).
     */
    public double getMB() {
        return getBytes() / (1024.0 * 1024.0);
    }

    public String getOriginalUnit() {
        return unit;
    }

    public double getOriginalValue() {
        return originalValue;
    }

    public static ByteSize parse(String input) {
        return new ByteSize(input);
    }

    @Override
    public TokenType type() {
        return TokenType.BYTE_SIZE;
    }

    @Override
    public String toString() {
        return originalValue + unit;
    }
}
