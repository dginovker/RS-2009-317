package org.gielinor.parser.object;

import org.gielinor.game.world.World;
import org.gielinor.parser.Parser;
import org.gielinor.spring.service.impl.ObjectSpawnService;

/**
 * Represents the world objects parser.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WorldObjectsParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        ((ObjectSpawnService) World.getWorld().getApplicationContext().getBean("objectSpawnService")).initializeWorldObjects();
        return true;
    }
}
