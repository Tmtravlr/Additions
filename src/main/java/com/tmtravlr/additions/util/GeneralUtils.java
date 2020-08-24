package com.tmtravlr.additions.util;

import com.google.gson.GsonBuilder;
import com.tmtravlr.additions.ConfigLoader;

public class GeneralUtils {
    /**
     * Creates a new GsonBuilder, which auto-detects whether to pretty-print
     * based on the configuration files.
     */
    public static GsonBuilder newBuilder() {
        GsonBuilder builder = new GsonBuilder();
        if (ConfigLoader.prettyPrintGeneratedFiles.getBoolean(true)) builder.setPrettyPrinting();
        return builder;
    }
}
