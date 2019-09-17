package plugin.interaction.item.withobject;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.dialogue.SkillDialogueHandler;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.smithing.SmithingConstants;
import org.gielinor.game.content.skill.free.smithing.SteelStudsPulse;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;

/**
 * Represents the option handler used for Smithing.
 *
 * @author 'Vexia
 */
public final class SmithingPlugin extends UseWithHandler {

    /**
     * Constructs a new {@code SmithingPlugin} {@code Object}.
     */
    public SmithingPlugin() {
        super(2349, 2351, 2353, 2359, 2361, 2363, 2366, 2368, 9467, 11286, 1540, 11710, 11712, 11714, 11686, 11688, 11692);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(2031, OBJECT_TYPE, this);
        addHandler(2097, OBJECT_TYPE, this);
        addHandler(2782, OBJECT_TYPE, this);
        addHandler(2783, OBJECT_TYPE, this);
        addHandler(4306, OBJECT_TYPE, this);
        addHandler(6150, OBJECT_TYPE, this);
        addHandler(22725, OBJECT_TYPE, this);
        addHandler(25349, OBJECT_TYPE, this);
        addHandler(26817, OBJECT_TYPE, this);
        addHandler(37622, OBJECT_TYPE, this);
        PluginManager.definePlugin(new AnvilOptionPlugin());
        PluginManager.definePlugin(new SteelStudsDialogue());
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent event) {
        final Player player = event.getPlayer();
        return show(player, event, true);
    }

    public static boolean show(Player player, NodeUsageEvent nodeUsageEvent, boolean steel) {
        if (!player.getInventory().contains(Item.HAMMER)) {
            player.getDialogueInterpreter().sendPlaneMessage("You need a hammer to work the metal with.");
            return true;
        } else {
            if (nodeUsageEvent.getUsedItem().getId() == 2366 || nodeUsageEvent.getUsedItem().getId() == 2368) {
                if (player.getSkills().getLevel(Skills.SMITHING) < 80) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need a smithing level of 80 to smith this.");
                    return true;
                }
                if (!player.getInventory().contains(2366, 1) && !player.getInventory().contains(2368, 1)) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need a shield left half and a shield right half.");
                    return true;
                }
                player.getDialogueInterpreter().open(82127843, 1, nodeUsageEvent.getUsedItem().getId());
                return true;
            }
            if (nodeUsageEvent.getUsedItem().getId() == 11286 || nodeUsageEvent.getUsedItem().getId() == 1540) {
                if (player.getSkills().getLevel(Skills.SMITHING) < 90) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need a smithing level of 90 to smith this.");
                    return true;
                }
                if (!player.getInventory().contains(1540, 1) || !player.getInventory().contains(11286, 1)) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need a draconic visage and an anti-dragon shield to do this.");
                    return true;
                }
                player.getDialogueInterpreter().open(82127843, 2, nodeUsageEvent.getUsedItem().getId());
                return true;
            }
            if (nodeUsageEvent.getUsedItem().getId() == 11710 || nodeUsageEvent.getUsedItem().getId() == 11712 || nodeUsageEvent.getUsedItem().getId() == 11714 || nodeUsageEvent.getUsedItem().getId() == 11686 || nodeUsageEvent.getUsedItem().getId() == 11688 || nodeUsageEvent.getUsedItem().getId() == 11692) {
                if (player.getSkills().getLevel(Skills.SMITHING) < 80) {
                    player.getDialogueInterpreter().sendPlaneMessage("You need a smithing level of 80 to smith this.");
                    return true;
                }
                player.getDialogueInterpreter().open(62362, nodeUsageEvent.getUsedItem().getId());
                return true;
            }
            if (nodeUsageEvent.getUsedItem().getId() == 2353 && steel) {
                player.getDialogueInterpreter().open("steel-stud-smith", nodeUsageEvent);
                return true;
            }
            SmithingConstants.buildInterface(player, nodeUsageEvent.getUsedItem());
        }
        return true;
    }

    /**
     * Represents the plugin used for anvils.
     *
     * @author <a href="http://Gielinor.org/">Gielinor</a>
     */
    public static class AnvilOptionPlugin extends OptionHandler {

        @Override
        public Plugin<Object> newInstance(Object arg) throws Throwable {
            ObjectDefinition.setOptionHandler("smith", this);
            return this;
        }

        @Override
        public boolean handle(final Player player, Node node, String option) {
            player.getDialogueInterpreter().sendDialogue("You should select an item from your inventory and use it on the", "anvil.");
            return true;
        }
    }

    /**
     * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} steel studs.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class SteelStudsDialogue extends DialoguePlugin {

        /**
         * The {@link org.gielinor.game.interaction.NodeUsageEvent}.
         */
        private NodeUsageEvent nodeUsageEvent;

        public SteelStudsDialogue() {
        }

        public SteelStudsDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new SteelStudsDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            nodeUsageEvent = (NodeUsageEvent) args[0];
            interpreter.sendOptions("Select an Option", "Steel studs", "Smithing");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            SkillDialogueHandler skillDialogueHandler = new SkillDialogueHandler(player, SkillDialogueHandler.SkillDialogue.ONE_OPTION, SteelStudsPulse.STEEL_STUDS) {

                                @Override
                                public void create(final int amount, int index) {
                                    player.getPulseManager().run(new SteelStudsPulse(player, nodeUsageEvent, amount));
                                }

                                @Override
                                public int getAll(int index) {
                                    return player.getInventory().getCount(nodeUsageEvent.getUsedItem());
                                }

                            };
                            if (player.getInventory().getCount(nodeUsageEvent.getUsedItem()) == 1) {
                                skillDialogueHandler.create(0, 1);
                            } else {
                                skillDialogueHandler.open();
                            }
                            stage = 1;
                            break;
                        case TWO_OPTION_TWO:
                            end();
                            SmithingPlugin.show(player, nodeUsageEvent, false);
                            break;
                    }
                    break;
                case 1:

                    break;
                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("steel-stud-smith") };
        }
    }

}
