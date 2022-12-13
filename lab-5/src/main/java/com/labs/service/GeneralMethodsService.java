package com.labs.service;

public class GeneralMethodsService {
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static double getRandomDouble(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static double getRoundedNumber(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
