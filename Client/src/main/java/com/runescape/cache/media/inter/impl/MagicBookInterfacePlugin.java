package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The {@link com.runescape.cache.media.inter.InterfacePlugin} for fixing the old magic books.
 *
 * @author Gielinor
 */
public class MagicBookInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        RSComponent rsComponent;
        // Name, description, tooltip id, spell desc, bottom message
        Object[][] TELEPORTS = new Object[][]{
                {"Home Teleport", "Requires no runes - teleports\\nyou home", 19210, 19220, 19222},
                {"Varrock Teleport", "Teleports you to Varrock", 1164, 19641, 19642},
                {"Lumbridge Teleport", "Teleports you to Lumbridge", 1167, 19722, 19723},
                {"Falador Teleport", "Teleports you to Falador", 1170, 19803, 19804},
                {"Camelot Teleport", "Teleports you to Camelot", 1174, 19960, 19961},
                {"Ardougne Teleport", "Teleports you to Ardougne", 1540, 20195, 20196},
                {"Watchtower Teleport", "Teleports you to Watchtower", 1541, 20354, 20355},
                {"Trollheim Teleport", "Teleports you to Trollheim", 7455, 20570, 20571},
                {"Home Teleport", "Requires no runes - teleports\\nyou home", 21741, 21756, 21757},
                {"Paddewwa Teleport", "A teleportation spell", 13035, 21833, 21834},
                {"Senntisten Teleport", "A teleportation spell", 13045, 21933, 21934},
                {"Kharyrll Teleport", "A teleportation spell", 13053, 22052, 22053},
                {"Lassar Teleport", "A teleportation spell", 13061, 22123, 22124},
                {"Dareeyak Teleport", "A teleportation spell", 13069, 22232, 22233},
                {"Carrallangar Teleport", "A teleportation spell", 13079, 22307, 22308},
                {"Annakarl Teleport", "A teleportation spell", 13087, 22415, 22416},
                {"Ghorrock Teleport", "A teleportation spell", 13095, 22490, 22491}
        };
        for (Object[] teleport : TELEPORTS) {
            rsComponent = RSComponent.getComponentCache()[(int) teleport[2]];
            rsComponent.tooltip = "Cast <col=00FF00>" + teleport[0];
            rsComponent = RSComponent.getComponentCache()[(int) teleport[3]];
            rsComponent.disabledMessage = "Level 0: " + teleport[0];
            rsComponent = RSComponent.getComponentCache()[(int) teleport[4]];
            rsComponent.disabledMessage = (String) teleport[1];
        }
        RSComponent.forId(349).scripts[0][0] = 5;
        RSComponent.forId(24111).scripts[0][0] = 5;
        rsComponent = RSComponent.forId(12424);
        rsComponent.getInterfaceChild(19210).getRSComponent().height = 26;
        return null;
    }
}
