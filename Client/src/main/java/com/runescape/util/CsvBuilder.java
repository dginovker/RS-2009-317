package com.runescape.util;

/**
 * @author Corey
 */

public class CsvBuilder {

    private StringBuilder builder;

    public CsvBuilder() {
        builder = new StringBuilder();
    }

    public CsvBuilder(Object firstEntry) {
        builder = new StringBuilder(firstEntry.toString() + ",");
    }

    public void append(Object entry) {
        builder.append(entry + ",");
    }

    public void appendWithoutComma(Object entry) {
        builder.append(entry);
    }

    @Override
    public String toString() {
        return builder.toString();
    }

}
