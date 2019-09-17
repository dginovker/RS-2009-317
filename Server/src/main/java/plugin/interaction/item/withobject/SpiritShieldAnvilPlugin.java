package plugin.interaction.item.withobject;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents using a sigil on an anvil to create a Spirit shield.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SpiritShieldAnvilPlugin extends UseWithHandler {

    /**
     * Represents the Blessed spirit shield item.
     */
    private static final Item BLESSED_SPIRIT_SHIELD = new Item(Item.BLESSED_SPIRIT_SHIELD);
    /**
     * Represents the Smithing {@link org.gielinor.game.world.update.flag.context.Animation}.
     */
    private static final Animation ANIMATION = new Animation(898);

    public SpiritShieldAnvilPlugin() {
        super(Item.ELYSIAN_SIGIL, Item.SPECTRAL_SIGIL, Item.ARCANE_SIGIL);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        addHandler(2782, OBJECT_TYPE, this);
        addHandler(2783, OBJECT_TYPE, this);
        addHandler(4306, OBJECT_TYPE, this);
        addHandler(6150, OBJECT_TYPE, this);
        addHandler(22725, OBJECT_TYPE, this);
        addHandler(26817, OBJECT_TYPE, this);
        PluginManager.definePlugin(new SpiritShieldDialogue());
        return this;
    }

    @Override
    public boolean handle(NodeUsageEvent nodeUsageEvent) {
        final Player player = nodeUsageEvent.getPlayer();
        if (!player.getInventory().contains(BLESSED_SPIRIT_SHIELD)) {
            player.getDialogueInterpreter().sendPlaneMessage(false, "You need a blessed spirit shield to attach that to.");
            return true;
        }
        if (player.getSkills().getLevel(Skills.PRAYER) < 90) {
            player.getDialogueInterpreter().sendPlaneMessage(false, "You need a Prayer level of at least 90 to do that.");
            return true;
        }
        if (player.getSkills().getLevel(Skills.SMITHING) < 85) {
            player.getDialogueInterpreter().sendPlaneMessage(false, "You need a Smithing level of at least 85 to do that.");
            return true;
        }
        player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("spirit-shield-attach"),
            nodeUsageEvent.getUsedItem());
        return true;
    }

    /**
     * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for attaching a sigil to a Blessed spirit shield.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    public static class SpiritShieldDialogue extends DialoguePlugin {

        /**
         * The sigil item.
         */
        private Item item;

        /**
         * The final spirit shield.
         */
        private Item shield;

        public SpiritShieldDialogue() {
        }

        public SpiritShieldDialogue(Player player) {
            super(player);
        }

        @Override
        public DialoguePlugin newInstance(Player player) {
            return new SpiritShieldDialogue(player);
        }

        @Override
        public boolean open(Object... args) {
            item = (Item) args[0];
            switch (item.getId()) {
                case Item.ELYSIAN_SIGIL:
                    shield = new Item(Item.ELYSIAN_SPIRIT_SHIELD);
                    break;
                case Item.SPECTRAL_SIGIL:
                    shield = new Item(Item.SPECTRAL_SPIRIT_SHIELD);
                    break;
                case Item.ARCANE_SIGIL:
                    shield = new Item(Item.ARCANE_SPIRIT_SHIELD);
                    break;
            }
            if (shield == null) {
                end();
                return false;
            }
            interpreter.sendPlaneMessage(false, "Are you sure you want to attach the " + item.getName().toLowerCase() + " to the blessed spirit", "shield?");
            stage = 0;
            return true;
        }

        @Override
        public boolean handle(int interfaceId, OptionSelect optionSelect) {
            switch (stage) {
                case 0:
                    options("Yes.", "No.");
                    stage = 1;
                    break;
                case 1:
                    switch (optionSelect) {
                        case TWO_OPTION_ONE:
                            if (player.getSkills().getLevel(Skills.PRAYER) < 90) {
                                interpreter.sendPlaneMessage(false, "You need a Prayer level of at least 90 to do that.");
                                stage = END;
                                break;
                            }
                            if (player.getSkills().getLevel(Skills.SMITHING) < 85) {
                                interpreter.sendPlaneMessage(false, "You need a Smithing level of at least 85 to do that.");
                                stage = END;
                                break;
                            }
                            if (!player.getInventory().contains(item)) {
                                interpreter.sendPlaneMessage(false, "You need a" +
                                    (TextUtils.isPlusN(item.getName().toLowerCase()) ? "n " : " ")
                                    + item.getName().toLowerCase() + " to do that.");
                                stage = END;
                                break;
                            }
                            if (!player.getInventory().contains(BLESSED_SPIRIT_SHIELD)) {
                                interpreter.sendPlaneMessage(false, "You need a blessed spirit shield to do that.");
                                stage = END;
                                break;
                            }
                            close();
                            player.lock(3);
                            player.playAnimation(ANIMATION);
                            World.submit(new Pulse(3) {

                                @Override
                                public boolean pulse() {
                                    player.getInventory().remove(item);
                                    player.getInventory().remove(BLESSED_SPIRIT_SHIELD);
                                    player.getInventory().add(shield, true);
                                    interpreter.sendItemMessage(shield, "You successfully attach the " + item.getName().toLowerCase() +
                                        " to the blessed spirit, ", "shield");
                                    player.getSkills().addExperienceNoMod(Skills.SMITHING, 1800);
                                    return true;
                                }
                            });
                            stage = END;
                            break;

                        case TWO_OPTION_TWO:
                            end();
                            return true;
                    }
                    break;
                case END:
                    end();
                    break;
            }
            return true;
        }

        @Override
        public int[] getIds() {
            return new int[]{ DialogueInterpreter.getDialogueKey("spirit-shield-attach") };
        }
    }
}
