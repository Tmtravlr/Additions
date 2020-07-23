package com.tmtravlr.additions.util;

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
}
