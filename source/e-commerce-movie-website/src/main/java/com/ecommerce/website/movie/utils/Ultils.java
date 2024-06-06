package com.ecommerce.website.movie.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class Ultils {
    /**
     * This method is used to create a predicate that filters out duplicate objects based on multiple key extractors.
     * It uses a ConcurrentHashMap to store the keys of the objects that have already been seen.
     *
     * @param <T>           The type of the objects to be filtered.
     * @param keyExtractors Functions that extract the keys from the objects.
     * @return A predicate that filters out duplicate objects based on the extracted keys.
     */
    @SafeVarargs
    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        // A ConcurrentHashMap is used to store the keys of the objects that have already been seen.
        // This allows for efficient and thread-safe checking of duplicate keys.
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
        // The returned predicate checks if the keys of the given object have already been seen.
        // If they have, the predicate returns false, indicating that the object is a duplicate.
        // If they have not, the predicate returns true, indicating that the object is not a duplicate.
        return t -> {
            // Extract the keys from the object using the provided key extractors.

            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
            // Check if the keys have already been seen.
            // If they have, return false to indicate a duplicate.
            // If they have not, add the keys to the seen map and return true to indicate a non-duplicate.
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }
}
