package com.yeminjilim.ssafyworld.util;

import java.util.Random;

public class CustomStringUtil {
    private static Random random = new Random();
    public static String makeRandomString(int length) {
        return random.ints(97, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
