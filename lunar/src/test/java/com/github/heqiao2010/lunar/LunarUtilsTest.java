package com.github.heqiao2010.lunar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LunarUtilsTest {

    @Test
    public void lengthOfMonth() {
        assertEquals(30, LunarUtils.lengthOfMonth(2020, 12, false));
    }
}