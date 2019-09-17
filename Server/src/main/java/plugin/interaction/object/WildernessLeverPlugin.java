package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.travel.Teleport;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The plugin for wilderness levers.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class WildernessLeverPlugin extends OptionHandler {

    /**
     * The animation used when pulling a lever.
     */
    private static final Animation PULL_ANIMATION = new Animation(2140);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (LeverSet leverSet : LeverSet.values()) {
            for (int id : leverSet.getIds()) {
                ObjectDefinition.forId(id).getConfigurations().put("option:pull", this);
            }
        }
        PluginManager.definePlugin(new LeverDialogue());
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        final GameObject object = (GameObject) node;
        final LeverSet leverSet = LeverSet.forId(object.getId());
        player.faceLocation(object.getLocation());
        leverSet.pull(player, leverSet.getIndex(object.getId()));
        ObjectBuilder.replace(object, object.transform(1817), 2);
        return true;
    }

    /**
     * Method used to pull a lever.
     *
     * @param player   The player.
     * @param leverSet The lever.
     */
    private static void pullLever(final Player player, final LeverSet leverSet, final int index) {
        player.lock(4);
        player.playAnimation(PULL_ANIMATION);
        player.getActionSender().sendSound(new Audio(2400));
        World.submit(new Pulse(2, player) {

            @Override
            public boolean pulse() {
                leverSet.message(player, index);
                player.getTeleporter().send(leverSet.getLocation(index), Teleport.TeleportType.NORMAL, Teleport.WILDY_TELEPORT);
                return true;
            }
        });
    }

    /**
     * Represents a lever set.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public enum LeverSet {

        ARDOUGNE(1814, 1815, new String[]{ "You pull the lever...", "...and teleport into the wilderness.", "...and teleport out of the wilderness." }, Location.create(2561, 3311, 0), Location.create(3154, 3923, 0)),
        MAGE_BANK(5959, 5960, new String[]{ "You pull the lever...", "...and teleport into the mage's cave.", "...and teleport out of the mage's cave." }, Location.create(3090, 3956, 0), Location.create(2539, 4712, 0));

        /**
         * The ids of the levers (back and forth).
         */
        private final int ids[];
        /**
         * The locations to teleport to (back and forth).
         */
        private final Location[] locations;
        /**
         * The messages to send (if any).
         */
        private String[] messages;

        /**
         * Creates a new lever set with more than two levers.
         *
         * @param ids       The ids.
         * @param locations The location.
         */
        private LeverSet(int[] ids, Location... locations) {
            this.ids = ids;
            this.locations = locations;
        }

        /**
         * Creates a new lever set with more than two levers.
         *
         * @param ids       The ids.
         * @param locations The location.
         */
        private LeverSet(int[] ids, String[] messages, Location... locations) {
            this.ids = ids;
            this.locations = locations;
            this.messages = messages;
        }

        /**
         * Creates a new lever set with two levers.
         *
         * @param first     The first id.
         * @param second    The second id.
         * @param locations The locations.
         */
        private LeverSet(int first, int second, Location... locations) {
            this(new int[]{ first, second }, locations);
        }

        /**
         * Creates a new lever set with two levers.
         *
         * @param first     The first id.
         * @param second    The second id.
         * @param locations The locations.
         */
        private LeverSet(int first, int second, String[] messages, Location... locations) {
            this(new int[]{ first, second }, locations);
        }

        /**
         * Pulls a lever.
         *
         * @param player The player.
         * @param index  The index of the lever.
         */
        public void pull(final Player player, final int index, boolean force) {
            if (index == 0 && !force) {
                player.getDialogueInterpreter().open("wilderness-lever", this, getId(index));
            } else {
                pullLever(player, this, index);
            }
        }

        /**
         * A wrapper method for pulling a lever.
         *
         * @param player The player.
         * @param index  The index of the lever.
         */
        public void pull(final Player player, final int index) {
            pull(player, index, false);
        }

        /**
         * Sends the lever messages on teleporting.
         *
         * @param player The player.
         * @param index  The index.
         */
        public void message(final Player player, final int index) {
            if (messages == null || messages.length < 3) {
                return;
            }
            player.getActionSender().sendMessages(messages[0], messages[index + 1]);
        }

        /**
         * Gets the index by the id.
         *
         * @param id the id.
         * @return the index.
         */
        public int getIndex(int id) {
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] == id) {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Gets a lever set by the id.
         *
         * @param id the id.
         * @return the set.
         */
        public static LeverSet forId(int id) {
            for (LeverSet set : values()) {
                for (int i : set.getIds()) {
                    if (i == id) {
                        return set;
                    }
                }
            }
            return null;
        }

        /**
         * Gets the location.
         *
         * @param index the index.
         * @return the location.
         */
        public Location getLocation(int index) {
            return locations[index == 0 ? 1 : 0];
        }

        /**
         * Gets the id.
         *
         * @param index the index.
         * @return the id.
         */
        public int getId(int index) {
            return ids[index];
        }

        /**
         * Gets the ids.
         *
         * @return The ids.
         */
        public int[] getIds() {
            return ids;
        }

        /**
         * Gets the location.
         *
         * @return The location.
         */
        public Location[] getLocations() {
            return locations;
        }

        /**
         * Gets the messages (if any).
         *
         * @return The messages.
         */
        public String[] getMessages() {
            return messages;
        }
    }

    /**
     * Handles the wilderness lever dialogues.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class LeverDialogue extends DialoguePlugin {

        /**
         * The lever set.
         */
        private LeverSet lever;

        /**
         * The object id.
         */
        private int id;

        /**
         * Constructs a new {@code LeverDialogue} {@code Object}.
         */
        public LeverDialogue() {
            /**
             * empty.
             */
        }

        /**
         * Constructs a new {@code LeverDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public LeverDialogue(final Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new LeverDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            lever = (LeverSet) args[0];
            id = (int) args[1];
            if (id == 5959) {
                lever.pull(player, lever.getIndex(id), true);
                return true;
            }
            interpreter.sendPlaneMessage("Warning! Pulling the lever will teleport you deep into the wilderness.");
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Yes, I'm brave.", "No thank you.");
                    stage++;
                    break;
                case 1:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            lever.pull(player, lever.getIndex(id), true);
                            end();
                            break;
                        case TWO_OPTION_TWO:
                            end();
                            break;
                    }
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("wilderness-lever") };
        }

    }
}
