package ru.esstu.news.aggregator.utils;

import java.nio.charset.StandardCharsets;

public class ConsoleEncodingFix {
    public static String fixStringEncoding(String string) {
        if (string == null)
            return null;
        try {
            return new String(string.getBytes(StandardCharsets.UTF_8), "windows-1251");
        } catch (Exception ex) {
            System.err.println("Failed to change encoding for string: " +
                    (string.length() > 120 ? string.substring(0, 120) : string));
            return string;
        }
    }
}