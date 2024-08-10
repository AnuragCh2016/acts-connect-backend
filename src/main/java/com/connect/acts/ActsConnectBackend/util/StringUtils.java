package com.connect.acts.ActsConnectBackend.util;

public class StringUtils {

    // Check if a string is null or empty
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    // Check if a string is not null and not empty
    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    // Trim whitespace from both ends of a string
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    // Convert a string to uppercase
    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    // Convert a string to lowercase
    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    // Check if a string contains another string
    public static boolean contains(String str, String search) {
        if (str == null || search == null) {
            return false;
        }
        return str.contains(search);
    }

    // Check if a string starts with another string
    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null) {
            return false;
        }
        return str.startsWith(prefix);
    }

    // Check if a string ends with another string
    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null) {
            return false;
        }
        return str.endsWith(suffix);
    }

    // Repeat a string a specified number of times
    public static String repeat(String str, int times) {
        if (str == null || times < 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        return str.repeat(times);
    }

    // Join an array of strings with a separator
    public static String join(String[] strings, String separator) {
        if (strings == null || separator == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    // Capitalize the first letter of a string
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // Split a string by a delimiter
    public static String[] split(String str, String delimiter) {
        if (str == null || delimiter == null) {
            return new String[0];
        }
        return str.split(delimiter);
    }

    // Check if a string is numeric
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
