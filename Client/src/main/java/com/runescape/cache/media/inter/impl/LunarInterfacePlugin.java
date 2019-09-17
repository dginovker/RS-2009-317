package com.runescape.cache.media.inter.impl;

import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * The 400+ Lunar {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class LunarInterfacePlugin extends RSComponent implements InterfacePlugin {

    public static void constructLunar() {
        RSComponent rsComponent = new RSComponent().addTabInterface(29999);
        int[] CHILDREN_IDS = {
                30000, 30017, 30025, 30032, 30040,
                30048, 30056, 30064, 30075, 30083,
                30091, 30099, 30106, 30114, 30122,
                30130, 30138, 30146, 30154, 30162,
                30170, 30178, 30186, 30194, 30202,
                30210, 30218, 30226, 30234, 30242,
                30250, 30258, 30266, 30274, 30282,
                30290, 30298, 30306, 30330, 30314, 30322,
                30001, 30018, 30026, 30033, 30041,
                30049, 30057, 30065, 30076, 30084,
                30092, 30100, 30107, 30115, 30123,
                30131, 30139, 30147, 30155, 30163,
                30171, 30179, 30187, 30195, 30203,
                30211, 30219, 30227, 30235, 30243,
                30251, 30259, 30267, 30275, 30283,
                30291, 30299, 30307, 30323, 30315
        };
        int[] X_OFFSETS = {
                11, 40, 71, 103, 133,
                162, 8, 41, 71, 103,
                134, 165, 12, 42, 71,
                103, 135, 165, 14, 42,
                71, 101, 135, 168, 10,
                42, 74, 103, 135, 164,
                10, 42, 71, 103, 138,
                162, 13, 42, 71, 100, 135,
                6, 5, 5, 5, 5,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5
        };
        int[] Y_OFFSETS = {
                10, 9, 12, 10, 12,
                10, 38, 39, 39, 39,
                39, 37, 68, 68, 66,
                68, 68, 68, 97, 97,
                97, 97, 98, 98, 126,
                124, 125, 125, 125, 126,
                155, 155, 155, 155, 155,
                155, 185, 185, 185, 182, 184,
                184, 176, 176, 163, 176,
                176, 176, 176, 163, 176,
                176, 176, 176, 163, 176,
                163, 163, 163, 176, 176,
                176, 163, 176, 149, 176,
                163, 163, 176, 149, 176,
                5, 5, 5, 5, 5,
                5, 5, 5, 5, 5, 5
        };
        for (int index = 0; index < CHILDREN_IDS.length; index++) {
            rsComponent.addChild(CHILDREN_IDS[index], X_OFFSETS[index], Y_OFFSETS[index]);
        }
    }

    @Override
    public RSComponent loadInterface(GameFont[] rsFonts) {
        homeTeleport();
        constructLunar();
        String[] RUNE = new String[]{
                "FIRE", "WATER", "AIR", "EARTH",
                "MIND", "BODY", "DEATH", "NATURE",
                "CHAOS", "LAW", "COSMIC", "BLOOD",
                "SOUL", "ASTRAL"
        };
        for (int index = 0; index < 14; index++) {
            drawRune(30003 + index, ImageLoader.forName("RUNE_" + RUNE[index]));
        }
        addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004, 64, "Bake Pie", "Bake pies without a stove", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_BAKE_PIE_ON"), ImageLoader.forName("LUNAR_BAKE_PIE_OFF")), 16, 2);
        addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant", "Cure disease on farming patch", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_CURE_PLANT_ON"), ImageLoader.forName("LUNAR_CURE_PLANT_OFF")), 4, 2);
        addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65, "Monster Examine", "Detect the combat statistics of a\\nmonster", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_MONSTER_EXAMINE_ON"), ImageLoader.forName("LUNAR_MONSTER_EXAMINE_OFF")), 2, 2);
        addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005, 66, "NPC Contact", "Speak with varied NPCs", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_NPC_CONTACT_ON"), ImageLoader.forName("LUNAR_NPC_CONTACT_OFF")), 0, 5);
        addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006, 67, "Cure Other", "Cure poisoned players", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_CURE_OTHER_ON"), ImageLoader.forName("LUNAR_CURE_OTHER_OFF")), 8, 2);
        addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003, 67, "Humidify", "Fills certain vessels with water", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_HUMIDIFY_ON"), ImageLoader.forName("LUNAR_HUMIDIFY_OFF")), 0, 5);
        addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006, 68, "Moonclan Teleport", "Teleports you to moonclan island", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_MOONCLAN_TELEPORT_ON"), ImageLoader.forName("LUNAR_MOONCLAN_TELEPORT_OFF")), 0, 5);
        addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69, "Tele Group Moonclan", "Teleports players to Moonclan\\nisland", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_MOONCLAN_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_MOONCLAN_OFF")), 0, 5);
        addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006, 70, "Ourania Teleport", "Teleports you to ourania rune altar", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_OURANIA_TELEPORT_ON"), ImageLoader.forName("LUNAR_OURANIA_TELEPORT_OFF")), 0, 5);
        addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012, 70, "Cure Me", "Cures Poison", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_CURE_ME_ON"), ImageLoader.forName("LUNAR_CURE_ME_OFF")), 0, 5);
        addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit", "Get a kit of hunting gear", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_HUNTER_KIT_ON"), ImageLoader.forName("LUNAR_HUNTER_KIT_OFF")), 0, 5);
        addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004, 71, "Waterbirth Teleport", "Teleports you to Waterbirth island", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_WATERBIRTH_TELEPORT_ON"), ImageLoader.forName("LUNAR_WATERBIRTH_TELEPORT_OFF")), 0, 5);
        addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72, "Tele Group Waterbirth", "Teleports players to Waterbirth\\nisland", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_WATERBIRTH_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_WATERBIRTH_OFF")), 0, 5);
        addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012, 73, "Cure Group", "Cures Poison on players", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_CURE_GROUP_ON"), ImageLoader.forName("LUNAR_CURE_GROUP_OFF")), 0, 5);
        addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74, "Stat Spy", "Cast on another player to see their\\nskill levels", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_STAT_SPY_ON"), ImageLoader.forName("LUNAR_STAT_SPY_OFF")), 8, 2);
        addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74, "Barbarian Teleport", "Teleports you to the Barbarian\\noutpost", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_BARBARIAN_TELEPORT_ON"), ImageLoader.forName("LUNAR_BARBARIAN_TELEPORT_OFF")), 0, 5);
        addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75, "Tele Group Barbarian", "Teleports players to the Barbarian\\noutpost", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_BARBARIAN_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_BARBARIAN_OFF")), 0, 5);
        addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005, 76, "Superglass Make", "Make glass without a furnace", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_SUPERGLASS_MAKE_ON"), ImageLoader.forName("LUNAR_SUPERGLASS_MAKE_OFF")), 16, 2);
        addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004, 77, "Khazard Teleport", "Teleports you to Port khazard", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_KHAZARD_TELEPORT_ON"), ImageLoader.forName("LUNAR_KHAZARD_TELEPORT_OFF")), 0, 5);
        addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004, 78, "Tele Group Khazard", "Teleports players to Port khazard", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_KHAZARD_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_KHAZARD_OFF")), 0, 5);
        addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78, "Dream", "Take a rest and restore hitpoints 3\\n times faster", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_DREAM_ON"), ImageLoader.forName("LUNAR_DREAM_OFF")), 0, 5);
        addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004, 79, "String Jewellery", "String amulets without wool", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_STRING_JEWELLERY_ON"), ImageLoader.forName("LUNAR_STRING_JEWELLERY_OFF")), 0, 5);
        addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004, 80, "Stat Restore Pot\nShare", "Share a potion with up to 4 nearby\\nplayers", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_STAT_RESTORE_POT_ON"), ImageLoader.forName("LUNAR_STAT_RESTORE_POT_OFF")), 16, 2);
        addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004, 81, "Magic Imbue", "Combine runes without a talisman", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_MAGIC_IMBUE_ON"), ImageLoader.forName("LUNAR_MAGIC_IMBUE_OFF")), 0, 5);
        addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82, "Fertile Soil", "Fertilise a farming patch with super\\ncompost", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_FERTILE_SOIL_ON"), ImageLoader.forName("LUNAR_FERTILE_SOIL_OFF")), 4, 2);
        addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83, "Boost Potion Share", "Shares a potion with up to 4 nearby\\nplayers", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_BOOST_POTION_SHARE_ON"), ImageLoader.forName("LUNAR_BOOST_POTION_SHARE_OFF")), 16, 2);
        addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004, 84, "Fishing Guild Teleport", "Teleports you to the fishing guild", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_FISHING_GUILD_TELEPORT_ON"), ImageLoader.forName("LUNAR_FISHING_GUILD_TELEPORT_OFF")), 0, 5);
        addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004, 85, "Tele Group Fishing Guild", "Teleports players to the Fishing\\nGuild", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_FISHING_GUILD_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_FISHING_GUILD_OFF")), 0, 5);
        addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010, 85, "Plank Make", "Turn Logs into planks", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_PLANK_MAKE_ON"), ImageLoader.forName("LUNAR_PLANK_MAKE_OFF")), 16, 2);
        addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004, 86, "Catherby Teleport", "Teleports you to Catherby", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_CATHERBY_TELEPORT_ON"), ImageLoader.forName("LUNAR_CATHERBY_TELEPORT_OFF")), 0, 5);
        addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004, 87, "Tele Group Catherby", "Teleports players to Catherby", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_CATHERBY_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_CATHERBY_OFF")), 0, 5);
        addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004, 88, "Ice Plateau Teleport", "Teleports you to Ice Plateau", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_ICE_PLATEAU_TELEPORT_ON"), ImageLoader.forName("LUNAR_ICE_PLATEAU_TELEPORT_OFF")), 0, 5);
        addLunar3RunesLargeBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89, "Tele Group Ice\\nPlateau", "Teleports players to Ice Plateau", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_TELE_GROUP_ICE_PLATEAU_ON"), ImageLoader.forName("LUNAR_TELE_GROUP_ICE_PLATEAU_OFF")), 0, 5);
        addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90, "Energy Transfer", "Spend HP and SA energy to\\n give another SA and run energy", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_ENERGY_TRANSFER_ON"), ImageLoader.forName("LUNAR_ENERGY_TRANSFER_OFF")), 8, 2);
        addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91, "Heal Other", "Transfer up to 75% of hitpoints\\n to another player", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_HEAL_OTHER_ON"), ImageLoader.forName("LUNAR_HEAL_OTHER_OFF")), 8, 2);
        addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92, "Vengeance Other", "Allows another player to rebound\\ndamage to an opponent", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_VENGEANCE_OTHER_ON"), ImageLoader.forName("LUNAR_VENGEANCE_OTHER_OFF")), 8, 2);
        addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006, 93, "Vengeance", "Rebound damage to an opponent", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_VENGEANCE_ON"), ImageLoader.forName("LUNAR_VENGEANCE_OFF")), 0, 5);
        addLunar3RunesBigBox(30330, 9075, 560, 557, 4, 2, 10, 30009, 30006, 95, "Vengeance Group", "Group 7x7", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_VENGEANCE_GROUP_ON"), ImageLoader.forName("LUNAR_VENGEANCE_GROUP_OFF")), 0, 5);
        addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94, "Heal Group", "Transfer up to 75% of hitpoints\\n to a group", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_HEAL_GROUP_ON"), ImageLoader.forName("LUNAR_HEAL_GROUP_OFF")), 0, 5);
        addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95, "Spellbook Swap", "Change to another spellbook for 1\\nspell cast", rsFonts, new SpriteSet(ImageLoader.forName("LUNAR_SPELLBOOK_SWAP_ON"), ImageLoader.forName("LUNAR_SPELLBOOK_SWAP_OFF")), 0, 5);
        return this;
    }

    public RSComponent homeTeleport() {
        RSComponent rsComponent = addInterface(30000);
        rsComponent.tooltip = "Cast <col=00FF00>Lunar Home Teleport";
        rsComponent.id = 30000;
        rsComponent.parentId = 30000;
        rsComponent.type = 5;
        rsComponent.optionType = 5;
        rsComponent.contentType = 0;
        rsComponent.opacity = 0;
        rsComponent.hoverType = 30001;
        rsComponent.getSpriteSet().setDisabled(ImageLoader.forName("ICON_HOME_TELEPORT"));
        rsComponent.width = 20;
        rsComponent.height = 20;
        RSComponent rsComponentTab = addInterface(30001);
        rsComponentTab.hoverOnly = true;
        rsComponentTab.hoverType = -1;
        addLunarSprite(30002, ImageLoader.forName("LUNAR_BOX_6"));
        rsComponentTab.setChildBounds(30002, 0, 0);
        return rsComponent;
    }

}
