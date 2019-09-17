package org.gielinor.parser;

/**
 * An interface implemented by parsing class which are used upon runtime.
 * @author Emperor
 *
 */
public interface Parser {

    /**
     * Parses whatever needs to be parsed, depending on the implementation.
     * @return <code>True</code> if succesful.
     * @throws Throwable When an exception occurs.
     */
    boolean parse() throws Throwable;

}
