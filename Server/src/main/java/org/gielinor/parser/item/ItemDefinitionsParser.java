package org.gielinor.parser.item;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.parser.Parser;

/**
 * The item definition parser.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class ItemDefinitionsParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        ItemDefinition.parse();
        return true;
    }
}