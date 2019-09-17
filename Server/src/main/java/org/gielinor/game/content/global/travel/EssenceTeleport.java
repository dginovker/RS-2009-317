package org.gielinor.game.content.global.travel;

import org.gielinor.game.content.global.achievementold.AchievementDiary;
import org.gielinor.game.content.global.achievementold.impl.AchievementTask;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents a utilitity class for rune essence teleporting.
 *
 * @author 'Vexia
 */
public final class EssenceTeleport {

    /**
     * Array of all the possible {@code Location} locations.
     */
    public static final Location LOCATIONS[] = { Location.create(2911, 4832, 0), Location.create(2913, 4837, 0), Location.create(2930, 4850, 0), Location.create(2894, 4811, 0), Location.create(2896, 4845, 0), Location.create(2922, 4820, 0), Location.create(2931, 4813, 0) };

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(437);

    /**
     * Method used to teleport a player.
     *
     * @param npc    the npc.
     * @param player the player.
     */
    public static void teleport(final NPC npc, final Player player) {
        npc.animate(ANIMATION);
        npc.faceTemporary(player, 1);
        npc.graphics(new Graphics(108));
        player.lock();
        //  player.getAudioManager().send(195);
        Projectile.create(npc, player, 109).send();
        npc.sendChat("Senventior Disthinte Molesko!");
        World.submit(new Pulse(1) {

            int counter = 0;

            @Override
            public boolean pulse() {
                switch (counter++) {
                    case 2:
                        player.getSavedData().getGlobalData().setEssenceTeleporter(npc.getId());
                        player.getProperties().setTeleportLocation(getLocation());
                        break;
                    case 3:
                        player.graphics(new Graphics(110));
                        player.unlock();
                        if (npc.getId() == Wizard.SEDRIDOR.getNpc()) {
                            AchievementDiary.finalize(player, AchievementTask.SEDRIDOR_RUNE_ESS_TELEPORT);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Method used to teleport back to the home.
     *
     * @param player the prayer.
     */
    public static void home(final Player player) {
        final Wizard wizard = EssenceTeleport.Wizard.forNPC(player.getSavedData().getGlobalData().getEssenceTeleporter());
        Projectile.create(player, player, 345).send();
        player.getProperties().setTeleportLocation(wizard.getLocation());
    }


    /**
     * Method used to get a random location.
     *
     * @return the location.
     */
    public static Location getLocation() {
        int count = RandomUtil.random(LOCATIONS.length);
        return LOCATIONS[count];
    }

    /**
     * Represents the wizard npc who can teleport.
     *
     * @author 'Vexia
     */
    public static enum Wizard {
        AUBURY(553, new Location(3253, 3401, 0)),
        SEDRIDOR(300, new Location(3107, 9573, 0)),
        DISTENTOR(462, new Location(2591, 3085, 0));

        /**
         * Constructs a new {@code WizardTowerPlugin} {@code Object}.
         *
         * @param npc      the npc.
         * @param location the location.
         */
        Wizard(final int npc, final Location location) {
            this.npc = npc;
            this.location = location;
        }

        /**
         * Represents the npc of this wizard.
         */
        private final int npc;

        /**
         * Represents the returining location.
         */
        public final Location location;

        /**
         * Method used to get a wizard by the npc.
         *
         * @param npc the npc.
         * @return the wizard.
         */
        public static Wizard forNPC(final int npc) {
            for (Wizard wizard : Wizard.values()) {
                if (wizard.getNpc() == npc) {
                    return wizard;
                }
            }
            return Wizard.AUBURY;
        }

        /**
         * Gets the npc.
         *
         * @return The npc.
         */
        public int getNpc() {
            return npc;
        }

        /**
         * Gets the location.
         *
         * @return The location.
         */
        public Location getLocation() {
            return location;
        }
    }

}
