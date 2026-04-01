package com.zorvyn.finance.util;

public class TokenUtil {
    public static long getExpiryTime() {
        return System.currentTimeMillis() + 3600000;
    }
}
