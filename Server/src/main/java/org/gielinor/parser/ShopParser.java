package org.gielinor.parser;

import org.gielinor.game.world.World;
import org.gielinor.spring.service.DefinitionService;

/**
 * Parses the {@link org.gielinor.game.content.global.shop.Shop} classes.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ShopParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        ((DefinitionService) World.getWorld().getApplicationContext().getBean("shopService")).initializeDefinitions();
        return true;
    }
}
