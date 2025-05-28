package br.com.totvs.customermanagement.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilTest {

    @Test
    void isValidCpf() {
        assertTrue(NumberUtil.isValidCpf("12345678909"));
        assertTrue(NumberUtil.isValidCpf("123.456.789-09"));
    }

    @Test
    public void testInvalidCpfWrongDigits() {
        assertFalse(NumberUtil.isValidCpf("12345678900"));
    }

    @Test
    public void testInvalidCpfAllSameDigits() {
        assertFalse(NumberUtil.isValidCpf("11111111111"));
    }

    @Test
    public void testInvalidCpfWrongLength() {
        assertFalse(NumberUtil.isValidCpf("123"));
        assertFalse(NumberUtil.isValidCpf("123456789012"));
    }

    @Test
    public void testNullOrEmpty() {
        assertFalse(NumberUtil.isValidCpf(null));
        assertFalse(NumberUtil.isValidCpf(""));
    }
}