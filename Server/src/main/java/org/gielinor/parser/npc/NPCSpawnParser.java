package org.gielinor.parser.npc;

import org.gielinor.game.world.World;
import org.gielinor.parser.Parser;
import org.gielinor.spring.service.SpawnService;

/**
 * Represents the class for parsing NPC spawns.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class NPCSpawnParser implements Parser {

    @Override
    public boolean parse() throws Throwable {
        SpawnService spawnService = (SpawnService) World.getWorld().getApplicationContext().getBean("npcSpawnService");
        spawnService.loadSpawns();
        return true;
    }
}