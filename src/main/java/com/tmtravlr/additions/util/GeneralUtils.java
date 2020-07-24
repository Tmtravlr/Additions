package com.tmtravlr.additions.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tmtravlr.additions.ConfigLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralUtils {
    /**
     * Return an array of type L containing the contents
     * of input, excluding values of {@code null}.
     * @param input the array you want to change
     * @param <A> the type of array
     * @return input without any elements of null
     */
    public static <A> A[] removeNulls(A[] input) {
        if (input.length == 0) return input;
        List<A> outputList = new ArrayList<>();
        for (A a : input) {
            if (a != null) outputList.add(a);
        }
        return outputList.toArray(Arrays.copyOf(input, 1));
    }

    /**
     * Creates a new GsonBuilder, which auto-detects whether to pretty-print
     * based on the configuration files.
     */
    public static GsonBuilder newBuilder() {
        GsonBuilder builder = new GsonBuilder();
        if (ConfigLoader.prettyPrintGeneratedFiles.getBoolean(true)) builder.setPrettyPrinting();
        return builder;
    }

    /**
     * Creates a new Gson object with a single type adapter
     * @param clazz the class to serialize
     * @param serializer the class' serializer object
     * @param <S> a generic type to support all the serializers
     * @return a completed Gson object
     */
    public static <S> Gson newGson(Class clazz, S serializer) {
        return newBuilder().registerTypeHierarchyAdapter(clazz, serializer).create();
    }
}
