package plugin.skill.hunter;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.hunter.falconry.FalconCatch;
import org.gielinor.game.content.skill.member.hunter.falconry.FalconryCatchPulse;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.HintIconManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * Represents the activity used during falconry practice.
 *
 * @author Vexia
 */
public final class FalconryActivityPlugin extends ActivityPlugin {

    /**
     * Constructs a new {@code FalconryActivityPlugin} {@code Object}.
     */
    public FalconryActivityPlugin() {
        this(null);
    }

    /**
     * Constructs a new {@code FalconryActivityPlugin} {@code Object}.
     *
     * @param player the player.
     */
    public FalconryActivityPlugin(final Player player) {
        super("falconry", true, false, false);
        this.player = player;
    }

    @Override
    public ActivityPlugin newInstance(Player p) {
        return new FalconryActivityPlugin(p);
    }

    @Override
    public boolean start(final Player player, boolean login, Object... args) {
        player.saveAttribute("falconry", true);
        return super.start(player, login, args);
    }

    @Override
    public boolean leave(final Entity entity, boolean logout) {
        if (!logout && entity instanceof Player) {
            entity.removeAttribute("falconry");
            removeItems((Player) entity);
        }
        return super.leave(entity, logout);
    }

    /**
     * Method used to remove the items.
     */
    public static void removeItems(final Player player) {
        player.getInventory().remove(new Item(10023, player.getInventory().getCount(new Item(10023))));
        player.getInventory().remove(new Item(10024, player.getInventory().getCount(new Item(10024))));
        player.getEquipment().remove(new Item(10023));
        player.getEquipment().remove(new Item(10024));
    }

    @Override
    public boolean teleport(final Entity entity, int type, Node node) {
        removeItems((Player) entity);
        return true;
    }

    @Override
    public boolean interact(final Entity e, Node target, Option option) {
        return false;
    }

    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public void configure() {
        register(new ZoneBorders(2360, 3571, 2396, 3637));
    }

    @Override
    public void register() {
        PluginManager.definePlugin(new FalconryPlugin());
    }


    /**
     * Represents the falconry plugin.
     *
     * @author Vexia
     */
    public static final class FalconryPlugin extends OptionHandler {

        /**
         * Represents the bones.
         */
        private static final Item BONES = new Item(526);

        /**
         * Represents the falcon item.
         */
        private static final Item FALCON = new Item(10024);

        /**
         * Represents the falcon glove.
         */
        private static final Item GLOVE = new Item(10023);

        @Override
        public Plugin<Object> newInstance(Object arg) {
            NPCDefinition.forId(5093).getConfigurations().put("option:quick-falconry", this);
            NPCDefinition.forId(5094).getConfigurations().put("option:retrieve", this);
            for (FalconCatch falconCatch : FalconCatch.values()) {
                NPCDefinition.forId(falconCatch.getNpc()).getConfigurations().put("option:catch", this);
            }
            PluginManager.definePlugin(new Plugin<Object>() {

                @Override
                public Plugin<Object> newInstance(Object arg) {
                    ItemDefinition.forId(10024).getConfigurations().put("equipment", this);
                    ItemDefinition.forId(10023).getConfigurations().put("equipment", this);
                    return this;
                }

                @Override
                public Object fireEvent(String identifier, Object... args) {
                    final Player player = (Player) args[0];
                    switch (identifier) {
                        case "unequip":
                            if (player.getZoneMonitor().isInZone("falconry")) {
                                player.getDialogueInterpreter().sendDialogue("Leave the area in order to remove your falcon.");
                                return false;
                            }
                            break;
                    }
                    return true;
                }

            });
            return this;
        }

        @Override
        public boolean handle(Player player, Node node, String option) {
            switch (option) {
                case "quick-falconry":
                    player.getDialogueInterpreter().open(5093, null, true);
                    break;
                case "catch":
                    player.face(((NPC) node));
                    player.getPulseManager().run(new FalconryCatchPulse(player, ((NPC) node), FalconCatch.forNPC(((NPC) node))));
                    break;
                case "retrieve":
                    final NPC npc = ((NPC) node);
                    if (!npc.getAttribute("falcon:owner", "").equals(player.getUsername())) {
                        player.getActionSender().sendMessage("This isn't your falcon.");
                        return true;
                    }
                    if (player.getInventory().freeSlots() == 0) {
                        player.getActionSender().sendMessage("You don't have enough inventory space.");
                        return true;
                    }
                    npc.clear();
                    HintIconManager.removeHintIcon(player, 1);
                    final FalconCatch falconCatch = npc.getAttribute("falcon:catch");
                    player.getSkills().addExperience(Skills.HUNTER, falconCatch.getExperience());
                    player.getActionSender().sendMessage("You retrieve the falcon as well as the fur of the dead kebbit.", 1);
                    player.getInventory().add(falconCatch.getItem());
                    player.getInventory().add(BONES);
                    if (player.getEquipment().remove(GLOVE)) {
                        player.getEquipment().add(FALCON, true, false);
                    } else {
                        player.getInventory().remove(GLOVE);
                        player.getInventory().add(FALCON);
                    }
                    break;
            }
            return true;
        }

        @Override
        public boolean isWalk() {
            return false;
        }

        @Override
        public boolean isWalk(final Player player, Node node) {
            if (node instanceof NPC) {
                final NPC n = ((NPC) node);
                if (n.getId() == 5093 || n.getId() == 5094) {
                    return true;
                }
            }
            return false;
        }

    }

}
