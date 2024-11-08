package ru.zmaev.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    private MapUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <K, V> Map<K, V> deepClone(Map<K, V> originalMap) {
        Map<K, V> clonedMap = new HashMap<>();
        for (Map.Entry<K, V> entry : originalMap.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            clonedMap.put(key, value);
        }
        return clonedMap;
    }
}
