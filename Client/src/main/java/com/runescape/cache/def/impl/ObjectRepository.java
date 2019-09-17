package com.runescape.cache.def.impl;

import com.runescape.cache.def.object.ObjectDefinition;

/**
 * Represents a repository for an {@link ObjectDefinition}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ObjectRepository {
    /**
     * Gets an {@link ObjectDefinition} by id.
     *
     * @param objectId The object id.
     * @return The {@link ObjectDefinition}.
     */
    public static ObjectDefinition forId(int objectId, ObjectDefinition objectDefinition) {

        if (objectDefinition.isInteractive && objectDefinition.hasAction("Use") && objectDefinition.hasAction("Use-quickly")) {
            objectDefinition.interactions[0] = "Use-quickly";
            objectDefinition.interactions[1] = "Exchange";
            objectDefinition.interactions[2] = "History";
            objectDefinition.interactions[3] = "Sets";
            return objectDefinition;
        }
        switch (objectId) {
            case 12163:
            case 12164:
            case 12165:
            case 12166:
                objectDefinition.isInteractive = true;
                return objectDefinition;

            case 7149:
            case 7147:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions = new String[1];
                objectDefinition.interactions[0] = "Squeeze-Through";
                objectDefinition.name = "Gap";
                return objectDefinition;

            case 2164:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions[0] = "Fix";
                objectDefinition.interactions[1] = null;
                objectDefinition.name = "Trawler Net";
                return objectDefinition;

            case 1293:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions[0] = "Teleport";
                objectDefinition.interactions[1] = null;
                objectDefinition.name = "Spirit Tree";
                return objectDefinition;

            case 7152:
            case 7144:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions = new String[1];
                objectDefinition.interactions[0] = "Chop";
                objectDefinition.name = "Tendrils";
                return objectDefinition;

            case 7153:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions = new String[1];
                objectDefinition.interactions[0] = "Mine";
                objectDefinition.name = "Rock";
                return objectDefinition;

            case 2452:
                objectDefinition.isInteractive = true;
                objectDefinition.name = "Mysterious Ruins";
                objectDefinition.interactions = new String[1];
                objectDefinition.interactions[0] = "Go Through";
                objectDefinition.name = "Passage";
                return objectDefinition;

            case 2455:
            case 2456:
            case 2454:
            case 2453:
            case 2461:
            case 2457:
            case 2459:
            case 2460:
                objectDefinition.isInteractive = true;
                objectDefinition.name = "Mysterious Ruins";
                return objectDefinition;

            case 23268:
            case 23261:
            case 23262:
            case 23263:
            case 23264:
            case 23265:
            case 23266:
            case 23267:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions = new String[]{"Cross", null, null, null, null};
                objectDefinition.name = "Wilderness Ditch";
                return objectDefinition;

            case 26439:
                objectDefinition.modelIds = new int[]{21617};
                objectDefinition.interactions = new String[]{"Climb-off", null, null, null, null};
                objectDefinition.isInteractive = true;
                return objectDefinition;

            case 10638:
                objectDefinition.isInteractive = true;
                return objectDefinition;

            case 2311:
            case 14435:
                objectDefinition.scaleX = 170;
                objectDefinition.scaleY = 170;
                break;

            case 25465:
                objectDefinition.animation = 1732;
                break;

            case 880:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions = new String[]{"Drink-from", null, null, null, null};
                return objectDefinition;

            case 2782:
            case 2783:
            case 4306:
            case 6150:
            case 22725:
            case 26817:
                objectDefinition.isInteractive = true;
                objectDefinition.interactions = new String[]{"Smith", null, null, null, null};
                return objectDefinition;

            case 28094:
                objectDefinition.name = "Hidden trapdoor";
                objectDefinition.interactions = new String[]{"Open", null, null, null, null};
                objectDefinition.modelIds = new int[]{45697};
                objectDefinition.projectileClipped = true;
                objectDefinition.isInteractive = true;
                objectDefinition.mapscene = 7;
                return objectDefinition;
        }
        return objectDefinition;
    }
}
