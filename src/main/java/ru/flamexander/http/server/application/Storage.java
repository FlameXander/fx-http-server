package ru.flamexander.http.server.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Storage {
    private static List<Item> items;

    public static void init() {
        System.out.println("Хранилище проинициализировано");
        items = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int)(Math.random() * 1000)));
        }
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static void save(Item item) {
        item.setId(UUID.randomUUID());
        items.add(item);
    }
}
