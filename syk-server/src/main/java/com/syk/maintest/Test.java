package com.syk.maintest;

/**
 * @author sunyukun
 * @since 2020/3/13 10:49
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(getOneByte("62"));
        System.out.println(getOneByte("63"));
        System.out.println(getOneByte("2"));
    }

    private static Integer getOneByte(String d1) {
        return Integer.valueOf(d1, 16) > 100 ? Integer.valueOf(100) : Integer.valueOf(d1, 16);
    }
}
