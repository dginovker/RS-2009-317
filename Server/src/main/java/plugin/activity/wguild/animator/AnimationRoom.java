package plugin.activity.wguild.animator;

import java.security.SecureRandom;

import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.ForceMovement;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneBuilder;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.task.impl.ItemLogoutTask;
import org.gielinor.rs2.task.impl.LogoutTask;

/**
 * Handles the animation
 *
 * @author Emperor
 */
public final class AnimationRoom extends MapZone implements Plugin<Object> {

    /**
     * Represents the {@link java.security.SecureRandom} instance.
     */
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Represents an armour set to be used on the animator.
     *
     * @author Emperor
     */
    static enum ArmourSet {
        BRONZE(4278, 5, 1155, 1117, 1075),
        IRON(4279, 10, 1153, 1115, 1067),
        STEEL(4280, 15, 1157, 1119, 1069),
        BLACK(4281, 20, 1165, 1125, 1077),
        MITHRIL(4282, 25, 1159, 1121, 1071),
        ADAMANT(4283, 30, 1161, 1123, 1073),
        RUNE(4284, 40, 1163, 1127, 1079);

        /**
         * The NPC id.
         */
        private final int npcId;

        /**
         * The amount of tokens to add.
         */
        private final int tokenAmount;

        /**
         * The pieces.
         */
        private final int[] pieces;

        /**
         * Constructs a new {@code ArmourSet} {@code Object}.
         *
         * @param npcId  The NPC id.
         * @param pieces The pieces.
         */
        private ArmourSet(int npcId, int tokenAmount, int... pieces) {
            this.npcId = npcId;
            this.tokenAmount = tokenAmount;
            this.pieces = pieces;
        }

        /**
         * Gets the npcId.
         *
         * @return The npcId.
         */
        public int getNpcId() {
            return npcId;
        }

        /**
         * Gets the tokenAmount.
         *
         * @return The tokenAmount.
         */
        public int getTokenAmount() {
            return tokenAmount;
        }

        /**
         * Gets the pieces.
         *
         * @return The pieces.
         */
        public int[] getPieces() {
            return pieces;
        }

    }

    /**
     * Constructs a new {@code AnimationRoom} {@code Object}.
     */
    public AnimationRoom() {
        super("wg animation", true);
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            NPC npc = e.getAttribute("animated_set");
            if (npc != null && npc.isActive()) {
                npc.finalizeDeath(null);
            }
        }
        return true;
    }

    /**
     * Animates the armour set.
     *
     * @param player The player.
     * @param object The animator.
     * @param set    The set to animate.
     */
    private void animateArmour(final Player player, final GameObject object, final ArmourSet set) {
        if (!player.getInventory().containItems(set.getPieces())) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a plate body, plate legs and full helm of the same type to", "activate the armour animator.");
            return;
        }
        if (player.getAttribute("animated_set") != null) {
            player.getActionSender().sendMessage("You already have a set animated.");
            return;
        }
        player.lock(10);
        player.animate(Animation.create(827));
        player.getDialogueInterpreter().sendPlaneMessage(true, "You place your armour on the platform where it disappears....");
        player.addExtension(LogoutTask.class, new ItemLogoutTask(6, new Item(set.getPieces()[0]), new Item(set.getPieces()[1]), new Item(set.getPieces()[2])));
        World.submit(new Pulse(6, player) {

            boolean spawn;

            @Override
            public boolean pulse() {
                if (!spawn) {
                    for (int id : set.getPieces()) {
                        if (!player.getInventory().remove(new Item(id))) {
                            return true;
                        }
                    }
                    player.getAudioManager().send(1909);
                    player.getDialogueInterpreter().sendPlaneMessage(true, "The animator hums, something appears to be working. You stand", "back...");
                    spawn = true;
                    super.setDelay(4);
                    return false;
                }
                if (getDelay() == 4) {
                    setDelay(1);
                    player.getAudioManager().send(1910);
                    ForceMovement.run(player, player.getLocation().transform(0, secureRandom.nextInt(150) == 10 ? 1 : 2, 0)).setDirection(Direction.SOUTH);
                    return false;
                }
                player.removeExtension(LogoutTask.class);
                player.getInterfaceState().closeChatbox();
                NPC npc = new AnimatedArmour(player, object.getLocation(), set);
                player.setAttribute("animated_set", npc);
                npc.init();
                return true;
            }
        });
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        int[] ids = new int[ArmourSet.values().length * 3];
        int index = 0;
        for (ArmourSet set : ArmourSet.values()) {
            for (int id : set.getPieces()) {
                ids[index++] = id;
            }
        }
        UseWithHandler.addHandler(15621, UseWithHandler.OBJECT_TYPE, new UseWithHandler(ids) {

            @Override
            public boolean handle(NodeUsageEvent event) {
                Item item = event.getUsedItem();
                ArmourSet set = null;
                roar:
                {
                    for (ArmourSet s : ArmourSet.values()) {
                        for (int id : s.getPieces()) {
                            if (id == item.getId()) {
                                set = s;
                                break roar;
                            }
                        }
                    }
                }
                animateArmour(event.getPlayer(), (GameObject) event.getUsedWith(), set);
                return true;
            }

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

        });
        ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public void configure() {
        super.register(new ZoneBorders(2849, 3534, 2861, 3545));
    }

}
