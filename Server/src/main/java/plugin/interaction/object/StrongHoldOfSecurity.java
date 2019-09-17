package plugin.interaction.object;

import java.awt.Point;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.action.DoorActionHandler;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the option plugin used for the strong hold of security.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class StrongHoldOfSecurity extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(29061).getConfigurations().put("option:climb-down", this); // main entrance
        ObjectDefinition.forId(23922).getConfigurations().put("option:use", this); // fourth level portal
        ObjectDefinition.forId(20656).getConfigurations().put("option:open", this); // gift of peace
        ObjectDefinition.forId(19001).getConfigurations().put("option:climb-up", this); // rope in level 2
        ObjectDefinition.forId(20785).getConfigurations().put("option:climb-down", this); // ladder to second level
        ObjectDefinition.forId(19003).getConfigurations().put("option:climb-up", this); // ladder from second to first level
        ObjectDefinition.forId(20784).getConfigurations().put("option:climb-up", this); // first floor to surface ladder
        ObjectDefinition.forId(19000).getConfigurations().put("option:search", this); // grain of plenty
        ObjectDefinition.forId(19004).getConfigurations().put("option:climb-down", this); // second ladder to third floor
        ObjectDefinition.forId(19005).getConfigurations().put("option:use", this); // second level portal
        ObjectDefinition.forId(23705).getConfigurations().put("option:climb-up", this); // ladder from third to second level
        ObjectDefinition.forId(23707).getConfigurations().put("option:use", this); // third level portal
        ObjectDefinition.forId(23703).getConfigurations().put("option:climb-up", this); // vine in third level
        ObjectDefinition.forId(23709).getConfigurations().put("option:open", this); // box of health
        ObjectDefinition.forId(23706).getConfigurations().put("option:climb-down", this); // third ladder going down
        ObjectDefinition.forId(20786).getConfigurations().put("option:use", this); // first level portal
        ObjectDefinition.forId(23921).getConfigurations().put("option:climb-up", this); // bone ladder up to third floor
        ObjectDefinition.forId(23732).getConfigurations().put("option:climb-up", this); // bone chain
        ObjectDefinition.forId(23731).getConfigurations().put("option:search", this); // cradle of life
        ObjectDefinition.forId(20788).getConfigurations().put("option:search", this); // dead explorer

        ObjectDefinition.forId(19206).getConfigurations().put("option:open", this); // first level door
        ObjectDefinition.forId(19207).getConfigurations().put("option:open", this); // first level door

        ObjectDefinition.forId(17100).getConfigurations().put("option:open", this); // second level door
        ObjectDefinition.forId(17009).getConfigurations().put("option:open", this); // second level door

        ObjectDefinition.forId(23653).getConfigurations().put("option:open", this); // third level door
        ObjectDefinition.forId(23654).getConfigurations().put("option:open", this); // third level door

        ObjectDefinition.forId(23727).getConfigurations().put("option:open", this); // fourth level door
        ObjectDefinition.forId(23728).getConfigurations().put("option:open", this); // fourth level door

        new ExplorerDialogue().init();
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        int id = ((GameObject) node).getId();
        switch (option) {
            case "climb-up":
                switch (id) {
                    case 23732:// bone chain.
                        player.getActionSender().sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
                        ladder(player, Location.create(3081, 3421, 0));
                        player.getActionSender().sendMessage("You climb the ladder which seems to twist and wind in all directions.");
                        break;
                    case 23921:// boney ladder to get to third floor.
                        ladder(player, Location.create(2123, 5252, 0));
                        player.getActionSender().sendMessage("You climb up the ladder to the level above.");
                        break;
                    case 23703: // vine in third level
                        player.getActionSender().sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
                        ladder(player, Location.create(2123, 5252, 0));
                        player.getActionSender().sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
                        break;
                    case 23705:// third ladder.
                        ladder(player, Location.create(2042, 5245, 0));
                        player.getActionSender().sendMessage("You climb up the ladder to the level above.");
                        break;
                    case 19003: // second ladder to first level
                        ladder(player, Location.create(1859, 5243, 0));
                        player.getActionSender().sendMessage("You climb up the ladder to the level above.");
                        break;
                    case 19001: // rope in level 2
                        player.getActionSender().sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
                        ladder(player, Location.create(2042, 5245, 0));
                        player.getActionSender().sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
                        break;
                    case 20784: // first level to surface
                        ladder(player, Location.create(3081, 3421, 0));
                        player.getActionSender().sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
                        break;
                }
                break;
            case "climb-down":
                switch (id) {
                    case 29061:
                        player.removeAttribute("sos:answered");
                        player.lock(2);
                        World.submit(new Pulse(1) {

                            boolean animated;

                            @Override
                            public boolean pulse() {
                                if (!animated) {
                                    player.animate(new Animation(828));
                                    animated = true;
                                    return false;
                                }
                                player.getProperties().setTeleportLocation(Location.create(1859, 5243, 0));
                                player.getDialogueInterpreter().open(70099, "You squeeze through the hole and find a ladder a few feet down", "leading into the Stronghold of Security.");
                                return true;
                            }
                        });
                        break;
                    case 23706:// third ladder going down.
                        openComponent(player, Location.create(2358, 5215, 0));
                        break;
                    case 19004:// second ladder going down.//2358 5215
                        openComponent(player, Location.create(2123, 5252, 0));
                        break;
                    case 20785: // first ladder going down
                        openComponent(player, Location.create(2042, 5245, 0));
                        break;
                }
                break;
            case "open":
                if (player.getAttribute("sos:answered", false)) {
                    doorTeleport(player, (GameObject) node);
                    return true;
                }
                if (((GameObject) node).getId() == 20656) { // gift of peace
                    if (player.getSavedData().getGlobalData().hasStrongholdReward(1)) {
                        player.getDialogueInterpreter().open(70099, "You have already claimed your reward from this level.");
                    } else {
                        player.getDialogueInterpreter().open(54678);
                    }
                    break;
                }
                if (id == 23709) { // box of health
                    if (player.getSavedData().getGlobalData().hasStrongholdReward(3)) {
                        player.getDialogueInterpreter().open(70099, "You have already claimed your reward from this level.");
                    } else {
                        player.getDialogueInterpreter().open(96878);
                    }
                    break;
                }
                player.getDialogueInterpreter().open(12459, (GameObject) node);
                break;
            case "use":
                if (id == 20786) {
                    if (!player.getSavedData().getGlobalData().hasStrongholdReward(1)) {
                        player.getActionSender().sendMessage("You are not of sufficient experience to take the shortcut through this level.");
                    } else {
                        player.getProperties().setTeleportLocation(Location.create(1914, 5222, 0));
                        player.getActionSender().sendMessage("You enter the portal to be whisked through to the treasure room.");
                    }
                }
                if (id == 19005) {
                    if (!player.getSavedData().getGlobalData().hasStrongholdReward(2)) {
                        player.getActionSender().sendMessage("You are not of sufficient experience to take the shortcut through this level.");
                    } else {
                        player.getProperties().setTeleportLocation(Location.create(2021, 5223, 0));
                        player.getActionSender().sendMessage("You enter the portal to be whisked through to the treasure room.");
                    }
                }
                if (id == 23707) {
                    if (!player.getSavedData().getGlobalData().hasStrongholdReward(3)) {
                        player.getActionSender().sendMessage("You are not of sufficient experience to take the shortcut through this level.");
                    } else {
                        player.getProperties().setTeleportLocation(Location.create(2146, 5287, 0));
                        player.getActionSender().sendMessage("You enter the portal to be whisked through to the treasure room.");
                    }
                }
                if (id == 23922) {
                    if (!player.getSavedData().getGlobalData().hasStrongholdReward(4)) {
                        player.getActionSender().sendMessage("You are not of sufficient experience to take the shortcut through this level.");
                    } else {
                        player.getProperties().setTeleportLocation(Location.create(2341, 5219, 0));
                        player.getActionSender().sendMessage("You enter the portal to be whisked through to the treasure room.");
                    }
                }
                break;
            case "search":
                if (id == 20788) { // dead explorer
                    player.getDialogueInterpreter().open(16152);
                    return true;
                }
                if (id == 19000) { // grain of plenty
                    if (player.getSavedData().getGlobalData().hasStrongholdReward(2)) {
                        player.getDialogueInterpreter().open(70099, "You have already claimed your reward from this level.");
                    } else {
                        player.getDialogueInterpreter().open(56875);
                    }
                }
                if (id == 23731) { // cradle of life
                    player.getDialogueInterpreter().open(96873);
                }
                break;
        }
        return true;
    }

    /**
     * Opens the component.
     *
     * @param player   the player.
     * @param location the location.
     */
    public void openComponent(Player player, Location location) {
        final Component component = new Component(259);
        player.getActionSender().sendString(265, "Are you sure you want to climb down?");
        player.getActionSender().sendString(266, "Yes - I know that it may be<br>  dangerous down there");
        player.getActionSender().sendString(267, "No thanks - I don't want to die");
        component.setPlugin(new StrongholdComponentPlugin(location));
        player.getInterfaceState().open(component);
    }

    /**
     * Method used to teleport through the door.
     *
     * @param player the player.
     */
    public void doorTeleport(final Player player, final GameObject door) {
        player.lock();
        player.animate(new Animation(4282));
        World.submit(new Pulse(1, player) {

            int count = 0;

            @Override
            public boolean pulse() {
                if (count == 1) {
                    player.removeAttribute("sos:answered");
                    Point p = DoorActionHandler.getRotationPoint(door.getRotation());
                    int[] rotation = DoorActionHandler.getRotation(door, DoorActionHandler.getSecondDoor(door), p);
                    final GameObject opened = door.transform(door.getId(), rotation[0]);
                    opened.setLocation(door.getLocation().transform((int) p.getX(), (int) p.getY(), 0));
                    Location destination = door.getLocation();
                    if (player.getLocation().equals(destination)) {
                        destination = destination.transform((int) p.getX(), (int) p.getY(), 0);
                    }
                    player.getProperties().setTeleportLocation(destination);
                }
                if (count == 2) {
                    return true;
                }
                count++;
                return false;
            }

            @Override
            public void stop() {
                player.animate(new Animation(4283));
                player.unlock();
            }
        });
    }

    public void ladder(final Player player, final Location location) {
        player.lock(2);
        World.submit(new Pulse(1) {

            boolean animated;

            @Override
            public boolean pulse() {
                if (!animated) {
                    player.animate(new Animation(828));
                    animated = true;
                    return false;
                }
                player.getProperties().setTeleportLocation(location);
                return true;
            }
        });
    }

    /**
     * Represents the explorer dialogue.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public static final class ExplorerDialogue extends DialoguePlugin {

        /**
         * Represents the stronghold notes item.
         */
        private static final Item STRONGHOLD_NOTES = new Item(9004);

        /**
         * Represents the animation specific to pickpocketing.
         */
        private static final Animation ANIMATION = new Animation(881);

        /**
         * Constructs a new {@code ExplorerDialogue} {@code Object}.
         *
         * @param player the player.
         */
        public ExplorerDialogue(final Player player) {
            super(player);
        }

        /**
         * Constructs a new {@code ExplorerDialogue} {@code Object}.
         */
        public ExplorerDialogue() {
            /**
             * empty.
             */
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new ExplorerDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            player.animate(ANIMATION);
            if (player.getInventory().containsItem(STRONGHOLD_NOTES) || player.getBank().containsItem(STRONGHOLD_NOTES)) {
                player.getActionSender().sendMessage("You don't find anything.");
                end();
                return true;
            }
            interpreter.sendPlaneMessage("You rummage around in the dead explorer's bag.....");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    interpreter.sendPlaneMessage("You find a book of hand written notes.");
                    stage = 1;
                    break;
                case 1:
                    player.getInventory().add(STRONGHOLD_NOTES, player);
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 16152 };
        }

    }

    /**
     * Represents the stronghold component plugin.
     *
     * @author 'Vexia
     * @version 1.0
     */
    public final class StrongholdComponentPlugin extends ComponentPlugin {

        /**
         * Represents the destination.
         */
        private final Location destination;

        /**
         * Constructs a new {@code StrongholdComponentPlugin} {@code Object}.
         *
         * @param destination the destination.
         */
        public StrongholdComponentPlugin(final Location destination) {
            this.destination = destination;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            return this;
        }

        @Override
        public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
            switch (button) {
                case 267:
                    if (player.getInterfaceState().close()) {
                        player.getDialogueInterpreter().open(8000, "No thanks, I don't want to die!");
                    }
                    return true;
                case 266:
                    player.getInterfaceState().close();
                    ladder(player, destination);
                    player.getActionSender().sendMessage("You climb down the ladder to the next level.");
                    return true;
            }
            return true;
        }

    }
}
