package org.gielinor.utilities.debug;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Used for disabling any output.
 * @author Emperor
 *
 */
public class NullLogger extends PrintStream {

    /**
     * Constructs a new {@code NullLogger} {@code Object}.
     * @param out The outputstream used.
     */
    public NullLogger(OutputStream out) {
        super(out);
    }

    @Override
    public void println(String s) {
        /*
         * empty
         */
    }

    @Override
    public void println(Object o) {
        /*
         * empty
         */
    }

    @Override
    public void println(int i) {
        /*
         * empty
         */
    }

    @Override
    public void println(boolean b) {
        /*
         * empty
         */
    }

    @Override
    public void println(double d) {
        /*
         * empty
         */
    }

    @Override
    public void print(String message) {
        /*
         * empty
         */
    }

    @Override
    public void print(boolean message) {
        /*
         * empty
         */
    }

    @Override
    public void print(int message) {
        /*
         * empty
         */
    }

}
