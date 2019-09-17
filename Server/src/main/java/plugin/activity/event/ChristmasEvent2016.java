package plugin.activity.event;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.game.world.update.flag.player.FaceLocationFlag;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the 2016 Christmas event.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ChristmasEvent2016 implements Plugin<Object> {

    /**
     * Represents if this event is live.
     */
    private static final boolean LIVE = false;

    /**
     * The snowball item.
     */
    private static final Item SNOWBALL = new Item(11951);

    /**
     * The snowmen hats and npc ids.
     */
    private static final int[][] SNOWMEN = new int[][]{
        { 11955, 6742 },
        { 11956, 6743 },
        { 11957, 6744 },
        { 11958, 6745 },
        { 11959, 6746 }
    };

    /**
     * The hats for selection.
     */
    private final String[] HATS = new String[]{
        "Barbarian", "Dragon", "Dwarf", "Pirate", "Top"
    };

    /**
     * The rewards.
     */
    private static final Item[] REWARDS = new Item[]{
        new Item(1050),
        new Item(14595),
        new Item(14603),
        new Item(14602),
        new Item(14605)
    };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        if (LIVE) {
            PluginManager.definePlugin(new SnowImpDialogue());
            PluginManager.definePlugin(new SnowCollectPlugin());
            PluginManager.definePlugin(new SnowballBuildPlugin());
            PluginManager.definePlugin(new SnowmanPlugin());
            PluginManager.definePlugin(new SnowmanHatPlugin());
            // TODO Snowman dialogues
            World.submit(new SnowPulse());
        }
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Represents the snow imp {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class SnowImpDialogue extends DialoguePlugin {

        /**
         * The selected hat.
         */
        private int selectedHat;

        /**
         * Constructs a new <code>SnowImpDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
         */
        public SnowImpDialogue() {
        }

        /**
         * Constructs a new <code>SnowImpDialogue</code> {@link org.gielinor.game.content.dialogue.DialoguePlugin}.
         *
         * @param player The player.
         */
        public SnowImpDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new SnowImpDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            npc = (NPC) args[0];
            if (player.getAttribute("CHRISTMAS_2016", 0) == 100) {
                player("Hello! Merry Christmas!");
                stage = 23;
                return true;
            }
            if (player.getAttribute("CHRISTMAS_2016", 0) == 3) {
                player("It's done! I've finished building a snowman!");
                stage = 20;
                return true;
            }
            if (player.getAttribute("CHRISTMAS_2016", 0) == 1 || player.getAttribute("CHRISTMAS_2016", 0) == 2) {
                if (hasHat()) {
                    npc("Hello again, " + player.getName() + ". Have you finished building the snowman?");
                    stage = 17;
                    return true;
                } else {
                    player("I'm afraid I've lost the snowman hat.");
                    stage = 19;
                    return true;
                }
            }
            player("Hello! Merry Christmas!");
            stage = 1;
            return true;
        }

        /**
         * Checks if the player has a snowman hat.
         *
         * @return <code>True</code> if so.
         */
        public boolean hasHat() {
            for (int hatId = 11955; hatId < (11955 + HATS.length); hatId++) {
                if (player.getInventory().contains(hatId) || player.getBank().contains(hatId)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 1:
                    npc("Hello, " + player.getUsername() + "!", "Ah, yes, Christmas... However, it doesn't seem so merry!");
                    stage = 2;
                    break;

                case 2:
                    player("What do you mean?");
                    stage = 3;
                    break;

                case 3:
                    npc("Look around " + player.getUsername() + "!", "No snowmen, no holiday cheer...", "No Santa Claus!");
                    stage = 4;
                    break;

                case 4:
                    player("Why, you're right! Where is Santa Claus?");
                    stage = 5;
                    break;

                case 5:
                    npc("We don't know, he left to gather decorations for his", "snowmen, and hasn't returned since!");
                    stage = 6;
                    break;

                case 6:
                    player("Well, is there anything I can do to help?");
                    stage = 7;
                    break;

                case 7:
                    npc("There just may be!", "Would you be able to build a snowman with a hat that", "I have?");
                    stage = 8;
                    break;

                case 8:
                    options("Yes, I can try!", "I'm too busy right now.");
                    stage = 9;
                    break;

                case 9:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            player("Yes, I can try!");
                            stage = 10;
                            break;
                        case TWO_OPTION_TWO:
                            player("I'm too busy right now... Sorry.");
                            stage = END;
                            break;
                    }
                    break;

                case 10:
                    npc("Wonderful! I have a few hats here, pick one to your liking!");
                    stage = 11;
                    break;

                case 11:
                    interpreter.sendOptions("Select a hat", HATS);
                    stage = 12;
                    break;

                case 12:
                    selectedHat = optionSelect.getIndex();
                    Item hat = new Item(11955 + selectedHat);
                    player.saveAttribute("CHRISTMAS_2016", 1);
                    if (!player.getInventory().hasRoomFor(hat)) {
                        npc("Oh, I'm afraid you don't have any inventory spaces.", "Make some room then come talk to me again!");
                        stage = END;
                        break;
                    }
                    npc("Great! Here's the " + HATS[selectedHat] + " hat to use.");
                    player.getInventory().add(hat);
                    stage = 13;
                    break;

                case 13:
                    npc("Now, " + player.getUsername() + ", to build a snowman, you simply have to click", "on some snow to collect snowballs.");
                    stage = 14;
                    break;

                case 14:
                    npc("When you have snowballs in your inventory, you can click the", "build option on them to create the base of the snowman.", "From here you can click \"add-to\" on the snowman, to add", "more snow from your inventory onto it!");
                    stage = 15;
                    break;

                case 15:
                    npc("Once the snowman is fully built you will be given a message.", "After that you can add the hat you've chosen to him!");
                    stage = 16;
                    break;

                case 16:
                    npc("Come back to me when you're finished building the snowman.");
                    player.saveAttribute("CHRISTMAS_2016", 2);
                    stage = END;
                    break;

                case 17:
                    player("Not yet, I'm afraid!");
                    stage = 18;
                    break;

                case 18:
                    npc("Oh, well let me know when you do!", "Remember, to build a snowman you simply have to click on", "some snow to collect snowballs.");
                    stage = 14;
                    break;

                case 19:
                    npc("No worries, I have some extra ones!");
                    stage = 11;
                    break;

                case 20:
                    npc("Great! It's beginning to look more like Christmas already!");
                    stage = 21;
                    break;

                case 21:
                    npc("Thank you for all of your help, " + player.getUsername() + "!", "I don't have much, but I can give you this, it doesn't", "fit Santa anymore!");
                    stage = 22;
                    break;

                case 22:
                    player.saveAttribute("CHRISTMAS_2016", 100);
                    if (player.getInventory().freeSlots() < 5 || player.getBank().freeSlots() < 5) {
                        for (Item item : REWARDS) {
                            GroundItem groundItem = new GroundItem(item, player.getLocation(), 5000, player);
                            GroundItemManager.create(groundItem);
                        }
                        player.getActionSender().sendMessage("Your rewards have fallen to the ground.");
                    } else {
                        if (player.getInventory().freeSlots() >= 5) {
                            for (Item item : REWARDS) {
                                player.getInventory().add(item);
                            }
                        } else {
                            for (Item item : REWARDS) {
                                player.getBank().add(item);
                            }
                            player.getActionSender().sendMessage("Your rewards have been placed in your bank.");
                        }
                    }

                    if (player.getRights().isAdministrator() || RandomUtil.random(0, 15) < 5) {
                        player.getDialogueInterpreter().sendPlaneMessage("A christmas cracker mysteriously falls out of the sky...");
                        GroundItemManager.create(new Item(962), player);
                        stage = END;
                        return true;
                    }
                    end();
                    break;

                case 23:
                    npc("Merry Christmas, " + player.getUsername() + "!");
                    stage = END;
                    break;

                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ 6732 };
        }
    }

    /**
     * Represents the {@link org.gielinor.game.interaction.OptionHandler} for piles of snow.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class SnowCollectPlugin extends OptionHandler {

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (player.getAttribute("CHRISTMAS_2016", 0) >= 2) {
                if (!player.getInventory().hasRoomFor(SNOWBALL)) {
                    player.getDialogueInterpreter().sendPlaneMessage("You can't carry anymore snowballs!");
                    return true;
                }
                if (player.getInventory().contains(6570) || player.getInventory().contains(Item.FIRE_MAX_CAPE) || player.getInventory().contains(Item.FIRE_MAX_HOOD)) {
                    player.getDialogueInterpreter().sendPlaneMessage("Your inventory seems too hot to collect snowballs...");
                    return true;
                }
                player.getPulseManager().run(new Pulse(3) {

                    @Override
                    public boolean pulse() {
                        player.faceLocation(node.getLocation());
                        player.lock(2);
                        player.animate(new Animation(7529));
                        player.getInventory().add(SNOWBALL);
                        if (!player.getInventory().hasRoomFor(SNOWBALL)) {
                            player.getDialogueInterpreter().sendPlaneMessage("You cannot carry anymore snowballs.");
                            return true;
                        }
                        return false;
                    }
                });
                return true;
            }
            player.getDialogueInterpreter().sendPlaneMessage("You have no reason to collect snow at the moment!");
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            ObjectDefinition.forId(28296).getConfigurations().put("option:collect", this);
            return this;
        }

    }

    /**
     * Represents the {@link org.gielinor.game.interaction.OptionHandler} for snowmen building.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class SnowmanPlugin extends OptionHandler {

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (player.getAttribute("CHRISTMAS_2016", 0) >= 2) {
                final GameObject[] snowman = { node.asObject() };
                if (snowman[0].getIndex() != player.getAttribute("snowman-index", 0)) {
                    player.getDialogueInterpreter().sendPlaneMessage("This isn't your snowman.");
                    return true;
                }
                if (snowman[0].getId() == 28295) {
                    player.getDialogueInterpreter().sendPlaneMessage("It looks like the snowman has enough snow.", "Perhaps you should use a hat on it?");
                    return true;
                }
                if (!player.getInventory().contains(SNOWBALL)) {
                    player.getDialogueInterpreter().sendPlaneMessage("You have nothing to add to the snowman.");
                    return true;
                }
                player.getActionSender().sendMessage("You add some snow to the snowman.");
                player.getPulseManager().run(new Pulse(3) {

                    @Override
                    public boolean pulse() {
                        player.faceLocation(node.getLocation());
                        player.animate(new Animation(7532));
                        player.getInventory().remove(SNOWBALL);
                        GameObject gameObject = snowman[0].transform(snowman[0].getId() + 1);
                        ObjectBuilder.remove(snowman[0]);
                        ObjectBuilder.add(gameObject, 500);
                        snowman[0] = gameObject;
                        player.saveAttribute("snowman-build", World.getTicks() + 200);
                        if (!player.getInventory().contains(SNOWBALL) || snowman[0].getId() == 28295) {
                            return true;
                        }
                        return false;
                    }
                });
                return true;
            }
            player.getDialogueInterpreter().sendPlaneMessage("You can't do that at the moment.");
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            for (int objectId = 28266; objectId <= 28295; objectId++) {
                ObjectDefinition.forId(objectId).getConfigurations().put("option:add-to", this);
            }
            return this;
        }

    }

    /**
     * Represents the {@link org.gielinor.game.interaction.OptionHandler} for snowballs.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class SnowballBuildPlugin extends OptionHandler {

        @Override
        public boolean handle(Player player, Node node, String option) {
            if (player.getAttribute("CHRISTMAS_2016", 0) == 2) {
                if (player.getAttribute("snowman-build", 0) > World.getTicks() && !player.getRights().isAdministrator()) {
                    player.getDialogueInterpreter().sendPlaneMessage("You should probably wait a little while before", "beginning a new snowman!");
                    return true;
                }
                if (RegionManager.getObject(player.getLocation()) != null ||
                    player.getZoneMonitor().isInZone("bank") || player.getLocation().getRegionId() != 12850
                    || player.getLocation().getZ() != 0) {
                    player.getDialogueInterpreter().sendPlaneMessage("You can't build a snowman here.");
                    return true;
                }
                GameObject snowman = new GameObject(28266, player.getLocation());
                player.animate(new Animation(9726));
                ObjectBuilder.add(snowman, 500);
                player.faceLocation(FaceLocationFlag.getFaceLocation(player, snowman));
                player.moveStep();
                player.saveAttribute("snowman-index", snowman.getIndex());
                player.getDialogueInterpreter().sendPlaneMessage("You build the base of the snowman.", "You should probably add some snow to it before it", "melts away!");
                player.saveAttribute("snowman-build", World.getTicks() + 200);
                return true;
            }
            player.getDialogueInterpreter().sendPlaneMessage("What could you build at a time like this with a snowball?");
            return true;
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            ItemDefinition.forId(11951).getConfigurations().put("option:build", this);
            return this;
        }

    }

    /**
     * Represents the {@link org.gielinor.game.interaction.UseWithHandler} for using a snowman hat on a snowman.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class SnowmanHatPlugin extends UseWithHandler {

        /**
         * Constructs a new <code>SnowmanHatPlugin</code>
         */
        public SnowmanHatPlugin() {
            super(11955, 11956, 11957, 11958, 11959);
        }

        @Override
        public Plugin<Object> newInstance(Object arg) {
            addHandler(28295, OBJECT_TYPE, this);
            return this;
        }

        @Override
        public boolean handle(NodeUsageEvent nodeUsageEvent) {
            final Player player = nodeUsageEvent.getPlayer();
            if (player.getAttribute("CHRISTMAS_2016", 0) >= 2) {
                final GameObject[] snowman = { nodeUsageEvent.getUsedWith().asObject() };
                if (snowman[0].getIndex() != player.getAttribute("snowman-index", 0)) {
                    player.getDialogueInterpreter().sendPlaneMessage("This isn't your snowman.");
                    return true;
                }
                player.lock(20);
                player.faceLocation(snowman[0].getLocation());
                int snowmanId = -1;
                for (int[] snowmen : SNOWMEN) {
                    if (snowmen[0] == nodeUsageEvent.getUsed().asItem().getId()) {
                        snowmanId = snowmen[1];
                        break;
                    }
                }
                if (snowmanId == -1) {
                    return false;
                }
                final int finalSnowmanId = snowmanId;
                World.submit(new Pulse(1) {

                    int ticks = 0;

                    @Override
                    public boolean pulse() {
                        switch (ticks) {
                            case 0:
                                player.getInventory().remove(nodeUsageEvent.getUsed().asItem());
                                player.getDialogueInterpreter().sendPlaneMessage(true, "You place the hat on the snowman...");
                                break;
                            case 1:
                                player.animate(new Animation(7533));
                                break;
                            case 3:
                                ObjectBuilder.remove(snowman[0]);
                                NPC npc = NPC.create(finalSnowmanId, snowman[0].getLocation(), snowman[0].getDirection());
                                npc.init();
                                World.submit(new Pulse(200, npc) {

                                    @Override
                                    public boolean pulse() {
                                        npc.clear();
                                        return true;
                                    }
                                });
                                player.getDialogueInterpreter().sendPlaneMessage(true, "The snowman comes to life!", "You should go tell the snow imp about your snowman!");
                                break;
                            case 6:
                                player.unlock();
                                player.getDialogueInterpreter().sendPlaneMessage(false, "The snowman comes to life!", "You should go tell the snow imp about your snowman!");
                                player.saveAttribute("CHRISTMAS_2016", 3);
                                return true;
                        }
                        ticks++;
                        return false;
                    }
                });
            }
            return true;
        }

    }


    /**
     * Handles the pulse for spawning snow.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public final class SnowPulse extends Pulse {

        /**
         * The borders in which we'll be spawning snow.
         */
        private final ZoneBorders LUMBRIDGE = ZoneBorders.forRegion(12850);

        /**
         * Constructs a new <code>SnowPulse</code>.
         */
        public SnowPulse() {
            super(20);
        }

        @Override
        public boolean pulse() {
            if (Repository.getPlayers().size() > 0) {
                int counter = RandomUtil.random(5, 10);
                while (counter > 0) {
                    Location location = Location.getRandomLocation(LUMBRIDGE);
                    if (RegionManager.forId(12850).isActive()) {
                        Graphics.send(Graphics.create(1284), location);
                    }
                    World.submit(new Pulse(5) {

                        @Override
                        public boolean pulse() {
                            ObjectBuilder.add(new GameObject(28296, location), 50);
                            return true;
                        }

                    });
                    counter--;
                }
            }
            return false;
        }
    }

}
