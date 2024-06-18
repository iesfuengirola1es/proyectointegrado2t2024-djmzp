package com.emp.utils;

public class Time {
    public static String formatMilliseconds(long milliseconds) {
        final long ss = (milliseconds / 1000) % 60;
        final long mm = (milliseconds / (1000 * 60)) % 60;
        final long hs = (milliseconds / (1000 * 60 * 60));

        if(hs > 0)
            return String.format("%d:%02d:%02d", hs, mm, ss);

        return String.format("%d:%02d", mm, ss);
    }
}
