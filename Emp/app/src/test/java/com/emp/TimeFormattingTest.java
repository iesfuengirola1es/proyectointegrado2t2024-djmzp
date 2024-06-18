package com.emp;

import static org.junit.Assert.*;

import com.emp.utils.Time;

import org.junit.Test;

public class TimeFormattingTest {
    @Test
    public void normalFormatTest() {
        assertEquals("0:59", Time.formatMilliseconds(59000));
        assertEquals("2:01", Time.formatMilliseconds(121000));
        assertEquals("59:59", Time.formatMilliseconds(3599000));
    }

    @Test
    public void hourFormatTest() {
        assertEquals("1:02:03", Time.formatMilliseconds(3723000));
        assertEquals("24:01:02", Time.formatMilliseconds(86462000));
    }

    @Test
    public void roundingTest() {
        assertEquals("0:00", Time.formatMilliseconds(0));
        assertEquals("0:00", Time.formatMilliseconds(999));
        assertEquals("0:01", Time.formatMilliseconds(1000));

        assertEquals("1:23", Time.formatMilliseconds(83000));
        assertEquals("1:23", Time.formatMilliseconds(83200));
        assertEquals("1:23", Time.formatMilliseconds(83800));
        assertEquals("1:23", Time.formatMilliseconds(83999));

        assertNotEquals("1:23", Time.formatMilliseconds(84000));
    }
}