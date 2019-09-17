package org.gielinor.game.content.skill;

import org.gielinor.game.content.global.Skillcape;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MusicContext;
import org.gielinor.net.packet.out.MusicPacket;
import org.gielinor.utilities.string.TextUtils;

import plugin.interaction.inter.EmoteTabInterface.Emote;

/**
 * Represents the action of leveling a skill level up.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class LevelUp {

    /**
     * Represents an interface for leveling up.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum SkillInterface {

        ATTACK(6247, 6248, 6249, 37),
        DEFENCE(6253, 6255, 6254, 37),
        STRENGTH(6206, 6208, 6207, 37),
        HITPOINTS(6216, 6218, 6217, 37),
        RANGED(4443, 6114, 5453, 37),
        PRAYER(6242, 6244, 6243, 37),
        MAGIC(6211, 6213, 6212, 37),
        COOKING(6226, 6228, 6227, 37),
        WOODCUTTING(4272, 4274, 4273, 39),
        FLETCHING(6231, 6233, 6232, 37),
        FISHING(6258, 6260, 6259, 37),
        FIREMAKING(4282, 4284, 4283, 37),
        CRAFTING(6263, 6265, 6264, 37),
        SMITHING(6221, 6223, 6222, 37),
        MINING(4416, 4438, 4417, 53),
        HERBLORE(6237, 6239, 6238, 37),
        AGILITY(4277, 4279, 4278, 37),
        THIEVING(4261, 4264, 4263, 37),
        SLAYER(12122, 12124, 12123, 37),
        FARMING(12122, 12124, 12123, 37),
        RUNECRAFTING(4267, 4269, 4268, 37),
        CONSTRUCTION(12122, 12124, 12123, 37),
        HUNTER(12122, 12124, 12123, 37),
        CONSTRUCTION1(12122, 12124, 12123, 37);

        /**
         * The interface id.
         */
        private final int interfaceId;
        /**
         * The first string id.
         */
        private final int first;
        /**
         * The second string id.
         */
        private final int second;
        /**
         * The id of the sound played.
         */
        private final int soundId;

        /**
         * Creates a skill interface.
         *
         * @param interfaceId The id of the interface.
         * @param first       The first string id.
         * @param second      The second string id.
         */
        SkillInterface(int interfaceId, int first, int second, int soundId) {
            this.interfaceId = interfaceId;
            this.first = first;
            this.second = second;
            this.soundId = soundId;
        }

        /**
         * Gets the interface id.
         *
         * @return the interface id
         */
        public int getInterfaceId() {
            return interfaceId;
        }

        /**
         * @return The first.
         */
        public int getFirst() {
            return first;
        }

        /**
         * @return The second.
         */
        public int getSecond() {
            return second;
        }

        /**
         * @return The sound id.
         */
        public int getSoundId() {
            return soundId;
        }
    }

    /**
     * Sends the level up interface etc.
     *
     * @param player The player.
     * @param slot   The skill slot.
     */
    public static void levelUp(Player player, int slot) {
        if (slot > SkillInterface.values().length) {
            return;
        }
        SkillInterface skillInterface = SkillInterface.values()[slot];
        String skillName = Skills.SKILL_NAME[slot];
        if (skillInterface == null) {
            if (slot == Skills.PRAYER) {
                player.getSkills().incrementPrayerPoints(1);
            }
            return;
        }
        if (skillInterface.getSoundId() > -1) {
            PacketRepository.send(MusicPacket.class, new MusicContext(player, skillInterface.getSoundId(), true));
        }
        player.getActionSender().sendGraphic(199);
        player.getActionSender().sendString(skillInterface.getSecond(), "<col=00008B>Congratulations, you've just advanced a " + skillName + " level!");
        player.getActionSender().sendString(skillInterface.getFirst(), "Your " + skillName + " level is now " + player.getSkills().getStaticLevel(slot) + ".");
        // TODO Should this not be filtered?
        player.getActionSender().sendMessage("You've just advanced a " + skillName + " level! You have reached level " + player.getSkills().getStaticLevel(slot) + ".", 1);

        if (slot == Skills.PRAYER) {
            player.getSkills().incrementPrayerPoints(1);
            if (Perk.REPLENISHER.enabled(player)) {
                player.getSkills().rechargePrayerPoints();
            }
        } else if (slot == Skills.HITPOINTS && Perk.REPLENISHER.enabled(player)) {
            player.getSkills().heal(player.getSkills().getStaticLevel(Skills.HITPOINTS));
        }

        if (player.getSkills().getStaticLevel(slot) == 99) {
            Skillcape.trim(player);
            player.getEmotes().unlock(Emote.SKILLCAPE);
            player.sendGlobalNewsMessage("ff8c38", " has achieved 99 " + TextUtils.formatDisplayName(skillName), 5);
        }
        player.getInterfaceState().openChatbox(skillInterface.getInterfaceId());
        player.getAppearance().sync();
    }

}
