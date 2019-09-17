package org.gielinor.game.node.entity.player.info;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.donor.DonorStatus;
import org.gielinor.game.node.entity.player.info.title.TitleCategory;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.misc.RandomUtil;
import org.gielinor.utilities.string.TextUtils;

import plugin.interaction.item.MaxCapePlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a player's name title.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum LoyaltyTitle {

    // RS P2P RUNECOINS | RS LOYALTY POINTS / 100
    LORD_LADY(1, "Lord", "Lady", "A35607", false, 10, TitleCategory.VOTING),
    SIR_DAME(2, "Sir", "Dame", "A35607", false, 10, TitleCategory.VOTING),
    CHEERFUL(3, "Cheerful", null, "A35607", false, -1),
    GRUMPY(4, "Grumpy", null, "A35607", false, -1),
    THE_TANNED(5, "the Tanned", null, "A35607", true, -1),
    DUDERINO_DUDETTE(6, "Duderino", "Dudette", "A35607", false, 20, TitleCategory.VOTING),
    LIONHEART(7, "Lionheart", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            int killCount = player.getSavedData().getGlobalData().getNpcKillCount();
            if (killCount < 1000) {
                player.getActionSender().sendMessage("You must kill a total of 1,000 NPCs to purchase that loyalty title.");
                player.getActionSender().sendMessage("You have only killed " + killCount + "/1000 NPCs total!");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    JUNIOR(8, "Junior", null, "A35607", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            long daysPlayed = player.getSavedData().getGlobalData().getTotalPlayTime() / 86400;
            long hoursPlayed = (player.getSavedData().getGlobalData().getTotalPlayTime() / 3600) - (daysPlayed * 24);
            if (hoursPlayed < 1) {
                player.getActionSender().sendMessage("You must have played for a total of 1 hour to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_OUTRAGEOUS(9, "The Outrageous", null, "A35607", false, -1),
    ATHLETE(10, "Athlete", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSkills().getLevel(Skills.AGILITY) < 99) {
                player.getActionSender().sendMessage("You must have an Agility level of 99 to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_STYLISH(11, "the Stylish", null, "A35607", true, -1),
    THE_MYSTERIOUS(12, "The Mysterious", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSkills().getLevel(Skills.RUNECRAFTING) < 99) {
                player.getActionSender().sendMessage("You must have a Runecrafting level of 99 to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_ADORABLE(13, "The Adorable", null, "A35607", false, -1),
    ESQUIRE_MS(14, "Esquire", "Ms", "A35607", false, 10, TitleCategory.REGULAR),
    MASTER_MISS(15, "Master", "Miss", "A35607", false, 20, TitleCategory.REGULAR),
    MR_MRS(16, "Mr", "Mrs", "A35607", false, -1),
    THE_UNTOUCHABLE(17, "The Untouchable", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getPlayerKills() < 100) {
                player.getActionSender().sendMessage("You must have killed at least 100 players to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_INTIMIDATING(18, "the Intimidating", null, "A35607", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getPlayerKills() < 10) {
                player.getActionSender().sendMessage("You must have killed at least 10 players to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    DOCTOR(19, "Doctor", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSkills().getLevel(Skills.HITPOINTS) < 99) {
                player.getActionSender().sendMessage("You must have a Hitpoints level of 99 to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_WILD(20, "the Wild", null, "A35607", true, 2_000_000, TitleCategory.COINS),
    THE_HOT(21, "the Hot", null, "A35607", true, 2_000_000, TitleCategory.COINS),
    THE_SUAVE_THE_ELEGANT(22, "the Suave", "the Elegant", "A35607", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (!player.getEquipment().containItems(13099, 13101)) { // rune cane, top hat
                player.getActionSender().sendMessage("You must have a rune cane and top hat equipped to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    CRUSADER(23, "Crusader", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSkills().getLevel(Skills.PRAYER) < 99 || player.getSkills().getLevel(Skills.ATTACK) < 99) {
                player.getActionSender().sendMessage("You must have a Prayer and Attack level of 99 to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    HELLRAISER(24, "Hellraiser", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSkills().calculateCombatLevel() < 136) {
                player.getActionSender().sendMessage("You must have a combat level of at least 136 to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_SKILFUL(25, "the Skilful", null, "A35607", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            boolean canPurchase = false;
            for (int i = 7; i <= 22; i++) {
                if (player.getSkills().getLevel(i) >= 99) {
                    canPurchase = true;
                    break;
                }
            }
            if (!canPurchase) {
                player.getActionSender().sendMessage("You must have a 99 in a non-combat skill to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE(26, "The", null, "A35607", false, 150, TitleCategory.REGULAR),
    THE_HANDSOME_THE_BEAUTIFUL(27, "the Handsome", "the Beautiful", "A35607", true, 5_000_000, TitleCategory.COINS),
    BARON_BARONESS(28, "Baron", "Baroness", "A35607", false, 25, TitleCategory.VOTING),
    DESPERADO(29, "Desperado", null, "A35607", false, 20, TitleCategory.REGULAR),
    COUNT_COUNTESS(30, "Count", "Countess", "A35607", false, 40, TitleCategory.REGULAR),
    OVERLORD(31, "Overlord", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getPlayerKills() < 200) {
                player.getActionSender().sendMessage("You must have killed at least 200 players to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    PRINCE_PRINCESS(32, "Prince", "Princess", "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (!player.getDonorManager().hasMembership()) {
                player.getActionSender().sendMessage("You must have purchased a membership rank (any) to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    BANDITO(33, "Bandito", null, "A35607", false, 30, TitleCategory.VOTING),
    DUKE_DUCHESS(34, "Duke", "Duchess", "A35607", false, 10, TitleCategory.VOTING),
    JUSTICIAR(35, "Justiciar", null, "A35607", false, 40, TitleCategory.VOTING),
    BIG_CHEESE(36, "Big Cheese", null, "A35607", false, 10_000_000, TitleCategory.COINS),
    BIGWIG(37, "Bigwig", null, "A35607", false, 100, TitleCategory.VOTING),
    KING_QUEEN(38, "King", "Queen", "A35607", false, 50, TitleCategory.VOTING),
    ARCHON(39, "Archon", null, "A35607", false, 10, TitleCategory.REGULAR),
    EMPEROR_EMPRESS(40, "Emperor", "Empress", "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (!player.getQuestRepository().hasCompletedAll()) {
                player.getActionSender().sendMessage("You must have completed all quests to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    WINDERKIND(41, "Wunderkind", null, "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (!player.getAchievementRepository().hasCompletedAll()) {
                player.getActionSender().sendMessage("You must have completed all achievements to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    WITCH_KING_WITCH_QUEEN(42, "Witch King", "Witch Queen", "A35607", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getFamiliarManager().getPetDetails().get(12475) == null) { // pet dragon item id
                player.getActionSender().sendMessage("You must have a fully grown pet dragon to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_AWESOME(43, "The Awesome", null, "A35607", false, -1),
    THE_MAGNIFICENT(44, "the Magnificent", null, "A35607", true, 75, TitleCategory.REGULAR),
    THE_UNDEFEATED(45, "the Undefeated", null, "A35607", true, 150, TitleCategory.REGULAR),
    THE_STRANGE(46, "the Strange", null, "A35607", true, 75, TitleCategory.REGULAR),
    THE_DIVINE(47, "the Divine", null, "A35607", true, 75, TitleCategory.REGULAR),
    THE_FALLEN(48, "the Fallen", null, "A35607", true, 75, TitleCategory.REGULAR),
    THE_WARRIOR(49, "the Warrior", null, "A35607", true, 75, TitleCategory.REGULAR),
    COWARDLY(50, "Cowardly", null, "8A3D8F", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getPlayerDeaths() < 10) {
                player.getActionSender().sendMessage("You must have died at least 10 times to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_REDUNDANT(51, "the Redundant", null, "8A3D8F", true, -1),
    EVERYONE_ATTACK(52, "Everyone attack", null, "8A3D8F", false, -1), // 10 hours total in wildy, need to figure that out
    SMELLY(53, "Smelly", null, "8A3D8F", false, -1),
    THE_IDIOT(54, "the Idiot", null, "8A3D8F", true, -1),
    SIR_LAME_DAME_LAME(55, "Sir Lame", "Dame Lame", "8A3D8F", false, -1),
    THE_FLAMBOYANT(56, "the Flamboyant", null, "8A3D8F", true, -1),
    WEAKLING(57, "Weakling", null, "8A3D8F", false, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getPlayerDeaths() < 20) {
                player.getActionSender().sendMessage("You must have died at least 20 times to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    WAS_PUNISHED(58, "was punished.", null, "8A3D8F", true, -1),
    LOST(59, "lost", null, "8A3D8F", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getDuelDeaths() < 30) {
                player.getActionSender().sendMessage("You must have died at least 30 times at the duel arena to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    YOU_FAIL(60, "...you fail", null, "8A3D8F", true, -1),
    NO_MATES(61, "No-mates", null, "8A3D8F", false, -1), // ???
    ATE_DIRT(62, "ate dirt", null, "8A3D8F", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSkills().getLevel(Skills.FARMING) < 99) {
                player.getActionSender().sendMessage("You must have a Farming level of 99 to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    DELUSIONAL(63, "Delusional", null, "8A3D8F", false, 10, TitleCategory.VOTING),
    THE_RESPAWNER(64, "the Respawner", null, "8A3D8F", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getPlayerDeaths() < 30) {
                player.getActionSender().sendMessage("You must have died at least 30 times to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    CUTEY_PIE(65, "Cutey-pie", null, "8A3D8F", false, 100, TitleCategory.VOTING),
    THE_FAIL_MAGNET(66, "the Fail Magnet", null, "8A3D8F", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getDuelDeaths() < 20) {
                player.getActionSender().sendMessage("You must have died at least 20 times at the duel arena to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    WAS_TERMINATED(67, "was terminated", null, "8A3D8F", true, 30),
    LAZY(68, "Lazy", null, "8A3D8F", false, -1),
    WHO(69, "<shad=1>? Who?</shad>", null, "3498DB", true, 0, TitleCategory.SPECIAL),
    THE_GAMEBREAKER(70, "the Gamebreaker", null, "C81E1E", true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!player.getSavedData().getGlobalData().hasBetaStatus()) {
                player.getActionSender().sendMessage("You must have beta status to purchase that loyalty title.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_GAMEBREAKER1(70, "the Gamebreaker", null, "C81E1E", true, false),
    THE_GAMEBREAKER2(70, "<col=000000>the Gam</col><col=C81E1E>ebreaker", null, "C81E1E", true, false),
    THE_GAMEBREAKER3(70, "<str>the Gamebreaker</str>", null, "C81E1E", true, false),
    THE_GAMEBREAKER4(70, "the   Gamebreaker", null, "C81E1E", true, false),
    THE_GAMEBREAKER5(70, "thE gAmEbrEAkEr", null, "C81E1E", true, false),
    THE_GAMEBREAKER6(70, "the Gaembreaker", null, "C81E1E", true, false),
    THE_GAMEBREAKER7(70, "the Gamebreakr", null, "C81E1E", true, false),
    THE_GAMEBREAKER8(70, "<str=FFFFFF>the Gamebreaker", null, "C81E1E", false, false),
    LE_TESTEUR_BETA(70, "le testeur beta", null, "C81E1E", true, false),
    HIPPITY_HOPPITY(71, "Hip<col=76ECFB>pit</col><col=C1FDA0>y Ho</col><col=9386E6>ppi</col><col=F298F4>ty</col>", null, "69FFB9", false, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!player.getSavedData().getGlobalData().unlockedEasterTitle()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by doing an Easter seasonal event.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_MAXED(72, "<shad=0>the Maxed</shad>", null, "941F0E", true, 0, TitleCategory.MISCELLANEOUS) {
        @Override
        public boolean hasRequirements(Player player, boolean message) {
            if (!MaxCapePlugin.hasRequirements(player, true)) {
                if (message) {
                    player.getActionSender().sendMessage("You must be maxed (and able to wear a Max cape) to use this title.");
                }
                return false;
            }
            return true;
        }

        @Override
        public boolean canPurchase(Player player) {
            if (!MaxCapePlugin.hasRequirements(player, true)) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by receiving a Max cape.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    IRONMAN_IRONWOMAN(73, "Ironman", "Ironwoman", "6D6D75", false, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!Ironman.isIronmanOnly(player)) {
                player.getActionSender().sendMessage("That loyalty title is for Iron Men and Iron Women.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_IRONMAN_THE_IRONWOMAN(74, "the Ironman", "the Ironwoman", "6D6D75", true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!Ironman.isIronmanOnly(player)) {
                player.getActionSender().sendMessage("That loyalty title is for Iron Men and Iron Women.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    ULTIMATE_IRONMAN_ULTIMATE_IRONWOMAN(75, "<shad=1>Ultimate Ironman</shad>", "<shad=1>Ultimate Ironwoman</shad>", "DBDBDB", false, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!Ironman.isUltimateIronman(player)) {
                player.getActionSender().sendMessage("That loyalty title is for Ultimate Iron Men and Ultimate Iron Women.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_ULTIMATE_IRONMAN_THE_ULTIMATE_IRONWOMAN(76, "<shad=1>the Ultimate Ironman</shad>", "<shad=1>the Ultimate Ironwoman</shad>", "DBDBDB", true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!Ironman.isUltimateIronman(player)) {
                player.getActionSender().sendMessage("That loyalty title is for Ultimate Iron Men and Ultimate Iron Women.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    HARDCORE_IRONMAN_HARDCORE_IRONWOMAN(77, "Hardcore Ironman", "Hardcore Ironwoman", "CE4C4C", false, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!Ironman.isHardcoreIronman(player)) {
                player.getActionSender().sendMessage("That loyalty title is for Hardcore Iron Men and Hardcore Iron Women.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_HARDCORE_IRONMAN_THE_HARDCORE_IRONWOMAN(78, "the Hardcore Ironman", "the Hardcore Ironwoman", "CE4C4C", true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (!Ironman.isHardcoreIronman(player)) {
                player.getActionSender().sendMessage("That loyalty title is for Hardcore Iron Men and Hardcore Iron Women.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_TRUTHFUL(79, "<shad=1>the Truthful</shad>", null, "3498DB", true, 0, TitleCategory.SPECIAL),

    OF_GIELINOR(80, "of Gielinor", null, "9932CC", true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getSavedData().getGlobalData().getTotalPlayTime() < 3_600_000) {
                player.getActionSender().sendMessage("This title can be unlocked by having at least one hour of play time.");
                player.getActionSender().sendMessage("Your current time played: " + TextUtils.getSmallPlaytime(player));
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_THRIFTY(81, "the Thrifty", null, "A35607", true, 1, TitleCategory.COINS),
    THE_SAPPHIRE_MEMBER(82, "the Sapphire Member", null, DonorStatus.SAPPHIRE_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.SAPPHIRE_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing sapphire membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_EMERALD_MEMBER(83, "the Emerald Member", null, DonorStatus.EMERALD_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.EMERALD_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing emerald membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_RUBY_MEMBER(84, "the Ruby Member", null, DonorStatus.RUBY_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.RUBY_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing ruby membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_DIAMOND_MEMBER(85, "the Diamond Member", null, DonorStatus.DIAMOND_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.DIAMOND_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing ruby membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_DRAGONSTONE_MEMBER(86, "the Dragonstone Member", null, DonorStatus.DRAGONSTONE_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.DRAGONSTONE_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing dragonstone membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_ONYX_MEMBER(87, "the Onyx Member", null, DonorStatus.ONYX_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.ONYX_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing onyx membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_ZENYTE_MEMBER(88, "<shad=0>the Zenyte Member</shad>", null, DonorStatus.ZENYTE_MEMBER.getColor(), true, 0, TitleCategory.MISCELLANEOUS) {
        public boolean canPurchase(Player player) {
            if (player.getDonorManager().getDonorStatus().ordinal() < DonorStatus.ZENYTE_MEMBER.ordinal()) {
                player.getActionSender().sendMessage("That loyalty title can be unlocked by purchasing zenyte membership.");
                return false;
            }
            return super.canPurchase(player);
        }
    },
    THE_ALPHA(89, "<shad=0>the Alpha</shad>", null, "FFC66D", true, 0, TitleCategory.SPECIAL),
    ALPHA(90, "<shad=0>Alpha</shad>", null, "FFC66D", false, 0, TitleCategory.SPECIAL),
    MASTER_FISHER(91, "<shad=0>Master Fisher</shad>", null, "4d94ff", false, 0, TitleCategory.SPECIAL),
    OF_TNG(92, "<shad=0>of TNG</shad>", null, "cc0099", true, 0, TitleCategory.SPECIAL),
    THE_WISHFUL(93, "<shad=1>the Wishful</shad>", null, "C86400", true, 0, TitleCategory.SPECIAL),
    THE_TOOL(94, "<shad=1>the Tool</shad>", null, "643200", true, 0, TitleCategory.SPECIAL),
    VYRELING(95, "Vyreling", null, "3A5BD1", false, 500000, TitleCategory.COINS),
    VYRE_GRUNT(96, "Vyre Grunt", null, "6338BC", false, 750000, TitleCategory.COINS),
    VYREWATCH(97, "Vyrewatch", null, "5F0d2A", false, 1000000, TitleCategory.COINS),
    VYRELORD_VYRELADY(98, "Vyrelord", "Vyrelady", "971F0D", false, 2000000, TitleCategory.COINS),
    MILLIONAIRE(99, "the Millionaire", null, "C86400", false, 999000000, TitleCategory.COINS),
    BILLIONAIRE(100, "the Billionaire", null, "FAB402", true, 2147000000, TitleCategory.COINS),
    DREAM_OF_MAH(101, "Dream of Mah", null, "9F20A5", false, 100, TitleCategory.REGULAR),
    THE_BETRAYED(102, "the Betrayed", null, "C120C1", true, 50, TitleCategory.REGULAR);
    /**
     * The id of this <code>LoyaltyTitle</code>.
     */
    private final int id;
    /**
     * The male version of this <code>LoyaltyTitle</code>.
     */
    private final String male;
    /**
     * The female version of this <code>LoyaltyTitle</code>.
     */
    private final String female;
    /**
     * The colour of this <code>LoyaltyTitle</code>.
     */
    private final String colour;
    /**
     * Whether or not this <code>LoyaltyTitle</code> is a suffix.
     */
    private final boolean suffix;
    /**
     * The cost of this <code>LoyaltyTitle</code>.
     */
    private final int cost;
    /**
     * The {@link org.gielinor.game.node.entity.player.info.title.TitleCategory}.
     */
    private final TitleCategory titleCategory;
    /**
     * Whether or not the title appears in the Title Shop
     */
    private final boolean showInShop;

    /**
     * Gets the cost string.
     *
     * @return The cost string.
     */
    public String getCostString() {
        switch (getTitleCategory()) {
            case REGULAR:
                return "<col=6C0000>" + getCost() + "</col> Loyalty Point" + (getCost() > 1 ? "s" : "");
            case COINS:
                String colourCode = "FFFF00";

                if (getCost() > 99_999) {
                    colourCode = "FFFFFF";
                }
                if (getCost() > 9_999_999) {
                    colourCode = "00FFFF";
                }

                return "<col=" + colourCode + ">" + TextUtils.getFormattedNumber(getCost()) + "</col> gp";
            case VOTING:
                return "<col=6C0000>" + getCost() + "</col> Voting Point" + (getCost() > 1 ? "s" : "");
            case MISCELLANEOUS:
                return "<col=6C0000>" + getCost() + "</col> Loyalty Point" + (getCost() > 1 ? "s" : "");
            default:
                return "<col=6C0000>" + getCost() + "</col> Loyalty Point" + (getCost() > 1 ? "s" : "");
        }
    }

    /**
     * Whether or not the player has the requirements to use this <code>LoyaltyTitle</code>.
     *
     * @param player  The player.
     * @param message If a message should be sent.
     */
    public boolean hasRequirements(Player player, boolean message) {
        return true;
    }

    /**
     * Whether or not the player can purchase this <code>LoyaltyTitle</code>.
     *
     * @param player The player.
     */
    public boolean canPurchase(Player player) {
        if (getTitleCategory() == null) {
            return false;
        }
        if (player.getTitleManager().getUnlockedTitles().contains(this)) {
            player.getActionSender().sendMessage("You already own that loyalty title.");
            return false;
        }
        switch (getTitleCategory()) {
            case REGULAR:
                if (player.getSavedData().getGlobalData().getLoyaltyPoints() < getCost()) {
                    player.getActionSender().sendMessage("You do not have enough Loyalty Points to purchase that loyalty title.");
                    return false;
                }
                return true;
            case COINS:
                if (player.getInventory().getCount(new Item(Item.COINS)) < getCost()) {
                    player.getActionSender().sendMessage("You do not have enough coins to purchase that loyalty title.");
                    return false;
                }
                return true;
            case VOTING:
                if (player.getSavedData().getGlobalData().getVotingPoints() < getCost()) {
                    player.getActionSender().sendMessage("You do not have enough Voting points to purchase that loyalty title.");
                    return false;
                }
                return true;
            case MISCELLANEOUS:
                if (player.getSavedData().getGlobalData().getLoyaltyPoints() < getCost()) {
                    player.getActionSender().sendMessage("You do not have enough points to purchase that loyalty title.");
                    return false;
                }
                return true;
            case SPECIAL:
                return true;
        }
        return false;
    }

    /**
     * Purchases this <code>LoyaltyTitle</code>.
     *
     * @param player The player.
     * @return The status message if purchase was successful.
     */
    public String purchase(Player player) {
        switch (getTitleCategory()) {
            case REGULAR:
                if (player.getSavedData().getGlobalData().getLoyaltyPoints() < getCost()) {
                    return null;
                }
                if (isUnlocked(player)) {
                    return null;
                }
                long gielinorPoints = player.getSavedData().getGlobalData().decreaseLoyaltyPoints(getCost());
                unlock(player);
                return "For " + getCost() + " Loyalty Points, you have " + gielinorPoints + " Loyalty Point" +
                        (gielinorPoints == 1 ? "" : "s") + " remaining.";
            case COINS:
                if (player.getInventory().getCount(new Item(Item.COINS)) < getCost()) {
                    return null;
                }
                if (isUnlocked(player)) {
                    return null;
                }
                player.getInventory().remove(new Item(Item.COINS, getCost()));
                unlock(player);
                return "For " + TextUtils.getFormattedNumber(getCost()) + "gp";
            case VOTING:
                if (player.getSavedData().getGlobalData().getVotingPoints() < getCost()) {
                    return null;
                }
                if (isUnlocked(player)) {
                    return null;
                }
                int votingPoints = player.getSavedData().getGlobalData().decreaseVotingPoints(getCost());
                unlock(player);
                return "For " + getCost() + " Loyalty Points, you now have " + votingPoints + " Voting points remaining.";
            case MISCELLANEOUS:
                return null;
            default:
                return null;
        }
    }

    LoyaltyTitle(int id, String male, String female, String colour, boolean suffix, boolean showInShop) {
        this(id, male, female, colour, suffix, 0, null, showInShop);
    }

    /**
     * Constructs a new <code>LoyaltyTitle</code>.
     *
     * @param male   The male version of this <code>LoyaltyTitle</code>.
     * @param female The female version of this <code>LoyaltyTitle</code>.
     * @param colour The colour of this <code>LoyaltyTitle</code>.
     * @param suffix Whether or not this <code>LoyaltyTitle</code> is a suffix.
     * @param cost   The cost of this <code>LoyaltyTitle</code>.
     */
    LoyaltyTitle(int id, String male, String female, String colour, boolean suffix, int cost) {
        this(id, male, female, colour, suffix, cost, null, true);
    }

    /**
     * Constructs a new <code>LoyaltyTitle</code>.
     *
     * @param male          The male version of this <code>LoyaltyTitle</code>.
     * @param female        The female version of this <code>LoyaltyTitle</code>.
     * @param colour        The colour of this <code>LoyaltyTitle</code>.
     * @param suffix        Whether or not this <code>LoyaltyTitle</code> is a suffix.
     * @param cost          The cost of this <code>LoyaltyTitle</code>.
     * @param titleCategory The {@link org.gielinor.game.node.entity.player.info.title.TitleCategory} of this <code>LoyaltyTitle</code>.
     */
    LoyaltyTitle(int id, String male, String female, String colour, boolean suffix, int cost, TitleCategory titleCategory) {
        this(id, male, female, colour, suffix, cost, titleCategory, true);
    }

    /**
     * Constructs a new <code>LoyaltyTitle</code>.
     *
     * @param male          The male version of this <code>LoyaltyTitle</code>.
     * @param female        The female version of this <code>LoyaltyTitle</code>.
     * @param colour        The colour of this <code>LoyaltyTitle</code>.
     * @param suffix        Whether or not this <code>LoyaltyTitle</code> is a suffix.
     * @param cost          The cost of this <code>LoyaltyTitle</code>.
     * @param titleCategory The {@link org.gielinor.game.node.entity.player.info.title.TitleCategory} of this <code>LoyaltyTitle</code>.
     */
    LoyaltyTitle(int id, String male, String female, String colour, boolean suffix, int cost, TitleCategory titleCategory, boolean showInShop) {
        this.id = id;
        this.male = male;
        this.female = female;
        this.colour = colour;
        this.suffix = suffix;
        this.cost = cost;
        this.titleCategory = titleCategory;
        this.showInShop = showInShop;
    }

    /**
     * Gets the formatted <code>LoyaltyTitle</code> with or without the player's username.
     *
     * @param player          The player.
     * @param includeUsername Whether or not the player's username should be included.
     * @param usernameColour  The colour of the username (if any).
     * @return The formatted title.
     */
    public String getFormattedTitle(Player player, boolean includeUsername, String usernameColour) {
        String colour = getColour();
        String titleName = get(player);
        String username = includeUsername ? ((usernameColour == null ? "" : usernameColour)
                + TextUtils.formatDisplayName(player.getUsername()) + (usernameColour == null ? "" : "</col>")) :
                "";
        return isSuffix() ? (username + " " + ("<col=" + colour + ">" + titleName + "</col>")) :
                (("<col=" + colour + ">" + titleName + "</col>") + " " + username);
    }

    public String getForumTitle(Player player) {
        if (!player.getTitleManager().hasTitle()) {
            return "";
        }
        String title = "<col=" + getColour() + ">" + get(player) + "</col>";

        title = title.replaceAll("<col=", "<font color=");
        title = title.replaceAll("</col>", "</font>");
        title = title.replaceAll("<shad=1>", "");
        title = title.replaceAll("</shad>", "");
        title = title.replaceAll("<str>", "<del>");
        title = title.replaceAll("</str>", "</del>");
        title = title.trim();

        // title = title.replaceAll("<[^>]*>", ""); // removes all tags

        return title;
    }


    /**
     * Gets the id of this <code>LoyaltyTitle</code>.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the male version of this <code>LoyaltyTitle</code>.
     *
     * @return The male version.
     */
    public String getMale() {
        return male;
    }

    /**
     * Gets the female version of this <code>LoyaltyTitle</code>.
     *
     * @return The female version.
     */
    public String getFemale() {
        return female;
    }

    /**
     * Gets this title name for the player's gender.
     *
     * @param player The player.
     * @return The title name,
     */
    public String get(Player player) {
        return player.getAppearance().getGender() == Gender.MALE ? getMale() : (getFemale() == null) ? getMale() : getFemale();
    }

    /**
     * Gets the colour of this <code>LoyaltyTitle</code>.
     *
     * @return The colour.
     */
    public String getColour() {
        return colour;
    }

    /**
     * Gets whether or not this <code>LoyaltyTitle</code> is a suffix.
     *
     * @return <code>True</code> if so.
     */
    public boolean isSuffix() {
        return suffix;
    }

    /**
     * Gets the cost of this <code>LoyaltyTitle</code>.
     *
     * @return The cost.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the {@link org.gielinor.game.node.entity.player.info.title.TitleCategory}.
     *
     * @return The title category.
     */
    public TitleCategory getTitleCategory() {
        return titleCategory;
    }

    public boolean getShowInShop(Player player) {
        if (titleCategory == TitleCategory.SPECIAL) {
            if (isUnlocked(player)) {
                return true;
            } else {
                return false;
            }
        }
        return showInShop;
    }

    /**
     * Gets a <code>LoyaltyTitle</code> by the id.
     *
     * @param id The id of the title.
     * @return The <code>LoyaltyTitle</code>.
     */
    public static LoyaltyTitle forId(int id) {
        for (LoyaltyTitle loyaltyTitle : LoyaltyTitle.values()) {
            if (loyaltyTitle.getId() == id) {
                if (!loyaltyTitle.showInShop) {
                    continue;
                }
                return loyaltyTitle;
            }
        }
        return null;
    }

    /**
     * Gets a <code>LoyaltyTitle</code> by name.
     *
     * @param name The name of the <code>LoyaltyTitle</code>.
     * @return The <code>LoyaltyTitle</code>.
     */
    public static LoyaltyTitle forName(String name) {
        for (LoyaltyTitle loyaltyTitle : LoyaltyTitle.values()) {
            if (loyaltyTitle.name().equalsIgnoreCase(name)) {
                return loyaltyTitle;
            }
        }
        return null;
    }

    /**
     * Checks if this <code>LoyaltyTitle</code> is unlocked for a player.
     *
     * @param player The player.
     */
    public boolean isUnlocked(Player player) {
        return player.getTitleManager().getUnlockedTitles().contains(this);
    }

    /**
     * Unlocks this <code>LoyaltyTitle</code> for a player.
     *
     * @param player The player.
     */
    public void unlock(Player player) {
        Arrays.stream(LoyaltyTitle.values())
            .filter(title -> title.getId() == this.id)
            .forEach(player.getTitleManager().getUnlockedTitles()::add);
    }

    /**
     * Removes this <code>LoyaltyTitle</code> for a player.
     *
     * @param player The player.
     * @return <code>True</code> if removed.
     */
    public boolean remove(Player player) {
        return player.getTitleManager().getUnlockedTitles().remove(this);
    }

    public void set(Player player) {
        set(player, false);
    }

    /**
     * Sets this <code>LoyaltyTitle</code> for a player.
     *
     * @param player The player.
     * @param useRandomVariation Whether or not we should select a random title if the title has any other variants
     */
    public void set(Player player, boolean useRandomVariation) {
        List<LoyaltyTitle> variations = Arrays.stream(LoyaltyTitle.values())
            .filter(title -> title.getId() == this.id)
            .collect(Collectors.toList());

        if (useRandomVariation && variations.size() > 1) {
            LoyaltyTitle actualChoice = RandomUtil.random(variations);
            player.getTitleManager().setLoyaltyTitle(actualChoice);
        } else {
            player.getTitleManager().setLoyaltyTitle(this);
        }
    }
}
