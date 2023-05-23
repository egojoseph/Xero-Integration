package com.wayapay.xerointegration.storage;


import com.wayapay.xerointegration.constant.Item;

import java.util.HashMap;
import java.util.Map;

public class ZooItemKeeper
{
    public static final Map<Item, Object> memoryStorage = new HashMap<>();

    public static void saveItem(Item item, Object value){
        memoryStorage.put(item, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getItem(Item key){
        return (T) memoryStorage.get(key);
    }
}
