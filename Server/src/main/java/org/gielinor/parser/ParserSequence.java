package org.gielinor.parser;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.parser.item.GroundItemParser;
import org.gielinor.parser.item.ItemDefinitionsParser;
import org.gielinor.parser.misc.ComponentDefinitionParser;
import org.gielinor.parser.misc.RangeDataParser;
import org.gielinor.parser.music.MusicDefinitionParser;
import org.gielinor.parser.npc.NPCDefinitionsParser;
import org.gielinor.parser.npc.NPCDropsParser;
import org.gielinor.parser.npc.NPCSpawnParser;
import org.gielinor.parser.object.DoorDefinition;
import org.gielinor.parser.object.WorldObjectsParser;

/**
 * Represents the sequence of parsing {@link Parser} classes. The {@link Parser} classes are used to loadPlayer neccesary game world
 * information that are needed to run game world.
 *
 * @author Emperor
 * @author 'Vexia
 */
public final class ParserSequence {

    /**
     * Represents the <code>List</code> of sequenced {@link Parser} classes.
     */
    private static List<Parser> parsers;

    /**
     * Constructs a new {@code World.java} {@code Object}.
     */
    private ParserSequence() {
        /*
         * empty.
         */
    }

    /**
     * Method used to prepare the parsed being parsed.
     */
    public static void prepare() {
        List<Parser> parsers = getSequence();
        parsers.add(new NPCDefinitionsParser());
        //parsers.add(new ObjectDefinitionsParser());
        parsers.add(new ConfigFileDefinitionsParser());
        parsers.add(new CS2DefinitionsParser());
        parsers.add(new DoorDefinition());
        parsers.add(new ItemDefinitionsParser());
        parsers.add(new NPCDropsParser());
        parsers.add(new ComponentDefinitionParser());
        parsers.add(new RangeDataParser());
        parsers.add(new MusicDefinitionParser());
        parsers.add(new WorldObjectsParser());
    }

    /**
     * Method used to loadPlayer all <code>Parse</code> values in the {@link #parsers} list.
     *
     * @throws Throwable if exception occurs.
     */
    public static void parse() throws Throwable {
        if (parsers == null) {
            throw new IllegalStateException("Cannot parse unprepared sequence!");
        }
        for (Parser parser : parsers) {
            if (!parser.parse()) {
                System.err.println("Failed to loadPlayer - [parser=" + parser + ", class=" + parser.getClass().getSimpleName() + "].");
                return;
            }
        }
        parsers.clear();
        parsers = null;/**free's the objects space.*/
    }

    /**
     * Initializes the part of the world after the plugins are loaded.
     *
     * @throws Throwable When an exception occurs.
     */
    public static void post() throws Throwable {
        new ShopParser().parse();
        new NPCSpawnParser().parse();
        new GroundItemParser().parse();
    }

    /**
     * @return the sequence.
     */
    public static List<Parser> getSequence() {
        if (parsers == null) {
            parsers = new ArrayList<>();
        }
        return parsers;
    }
}
