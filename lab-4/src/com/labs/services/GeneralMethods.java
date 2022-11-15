package com.labs.services;

import com.labs.domain.Item;

import java.util.ArrayList;
import java.util.List;

public class GeneralMethods {
    public static List<Item> generateItems(int count) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(new Item(randomNumber(2, 11), randomNumber(1, 6)));
        }
        return items;
    }

    public static int randomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
    public static double randomDouble(int min, int max) {
        return  ((Math.random() * (max - min)) + min);
    }
}
