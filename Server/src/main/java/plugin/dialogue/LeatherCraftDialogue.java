package plugin.dialogue;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.dialogue.impl.Dialogue;
import org.gielinor.game.content.skill.free.crafting.armour.DragonCraftPulse;
import org.gielinor.game.content.skill.free.crafting.armour.HardCraftPulse;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting;
import org.gielinor.game.content.skill.free.crafting.armour.LeatherCrafting.DragonHide;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.context.InterfaceContext;
import org.gielinor.utilities.misc.RunScript;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the dialogue plugin used for leather crafting.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LeatherCraftDialogue extends DialoguePlugin {

    /**
     * Represents the type.
     */
    private String type = "";

    /**
     * Represents the leather.
     */
    private int leather;

    /**
     * Constructs a new {@code LeatherCraftDialogue} {@code Object}.
     */
    public LeatherCraftDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code LeatherCraftDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public LeatherCraftDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new LeatherCraftDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        Dialogue dialogue = Dialogue.ITEM_SELECT_3;
        type = (String) args[0];

        if (type.equals("hard")) {
            dialogue = Dialogue.ITEM_SELECT_1;
            player.getInterfaceState().openChatbox(dialogue.getInterfaceId());
            player.getActionSender().sendItemZoomOnInterface(1131, 150, dialogue.getModelIds()[0]);
            player.getActionSender().sendString("\\n\\n\\n\\n" + "Heardleather body", dialogue.getTextIds()[0]);
        } else {
            /** dragon */
            leather = (int) args[1];
            int index[] = new int[3];
            if (leather == LeatherCrafting.SNAKESKIN) {
                dialogue = Dialogue.ITEM_SELECT_5;
                Component component = new Component(dialogue.getInterfaceId());
                component.getDefinition().setContext(new InterfaceContext(null, dialogue.getInterfaceId(), false));
                player.getInterfaceState().openChatbox(component);
                index = new int[]{
                    DragonHide.SNAKESKIN_BODY.getProduct(), DragonHide.SNAKESKIN_CHAPS.getProduct(),
                    DragonHide.SNAKESKIN_VAMBRACES.getProduct(), DragonHide.SNAKESKIN_BANDANA.getProduct(),
                    DragonHide.SNAKESKIN_BOOTS.getProduct()
                };
                for (int i = 0; i < index.length; i++) {
                    player.getActionSender().sendItemZoomOnInterface(index[i],
                        (i == 0 || i == 3) ? 120 : 170,
                        dialogue.getModelIds()[i]);
                    player.getActionSender().sendString("\\n\\n\\n\\n" +
                            TextUtils.uppercaseFirst(ItemDefinition.forId(index[i]).getName().replaceAll("Snakeskin ", "").replaceAll("v\\'brace", "Vambraces")),
                        dialogue.getTextIds()[i]);
                }
                return true;
            }
            Component component = new Component(dialogue.getInterfaceId());
            component.getDefinition().setContext(new InterfaceContext(null, dialogue.getInterfaceId(), false));
            player.getInterfaceState().openChatbox(component);
            if (leather == LeatherCrafting.GREEN_LEATHER) {
                index[0] = DragonHide.GREEN_D_HIDE_BODY.getProduct();
                index[1] = DragonHide.GREEN_D_HIDE_VAMBS.getProduct();
                index[2] = DragonHide.GREEN_D_HIDE_CHAPS.getProduct();
            }
            if (leather == LeatherCrafting.BLUE_LEATHER) {
                index[0] = DragonHide.BLUE_D_HIDE_BODY.getProduct();
                index[1] = DragonHide.BLUE_D_HIDE_VAMBS.getProduct();
                index[2] = DragonHide.BLUE_D_HIDE_CHAPS.getProduct();
            }
            if (leather == LeatherCrafting.RED_LEATHER) {
                index[0] = DragonHide.RED_D_HIDE_BODY.getProduct();
                index[1] = DragonHide.RED_D_HIDE_VAMBS.getProduct();
                index[2] = DragonHide.RED_D_HIDE_CHAPS.getProduct();
            }
            if (leather == LeatherCrafting.BLACK_LEATHER) {
                index[0] = DragonHide.BLACK_D_HIDE_BODY.getProduct();
                index[1] = DragonHide.BLACK_D_HIDE_VAMBS.getProduct();
                index[2] = DragonHide.BLACK_D_HIDE_CHAPS.getProduct();
            }
            player.getActionSender().sendItemZoomOnInterface(index[0], 180, dialogue.getModelIds()[0]);
            player.getActionSender().sendItemZoomOnInterface(index[1], 180, dialogue.getModelIds()[1]);
            player.getActionSender().sendItemZoomOnInterface(index[2], 180, dialogue.getModelIds()[2]);
            player.getActionSender().sendString("\\n\\n\\n\\n" + ItemDefinition.forId(index[0]).getName(), dialogue.getTextIds()[0]);
            player.getActionSender().sendString("\\n\\n\\n\\n" + ItemDefinition.forId(index[1]).getName(), dialogue.getTextIds()[1]);
            player.getActionSender().sendString("\\n\\n\\n\\n" + ItemDefinition.forId(index[2]).getName(), dialogue.getTextIds()[2]);
        }
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        player.getInterfaceState().closeChatbox();
        int buttonId = optionSelect == null ? -1 : optionSelect.getChildId();
        int amt = 0;
        switch (type) {
            case "hard":
                switch (buttonId) {
                    case 2799:
                        amt = 1;
                        break;
                    case 2798:
                        amt = 5;
                        break;
                    case 1748:
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                int amount = (int) value;
                                player.getPulseManager().run(new HardCraftPulse(player, null, amount));
                                return false;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(false, "Enter the amount");
                        return true;
                    case 1747:
                        amt = player.getInventory().getCount(new Item(LeatherCrafting.HARD_LEATHER));
                        break;
                }
                player.getPulseManager().run(new HardCraftPulse(player, null, amt));
                break;
            case "snakeskin":
                //OptionSelect optionSelect = OptionSelect.forId(buttonId);
                assert optionSelect != null;
                int hideIndex = optionSelect.getIndex(buttonId);
                DragonHide snakeskin = null;
                switch (hideIndex) {
                    case 0:
                        snakeskin = DragonHide.SNAKESKIN_BODY;
                        break;
                    case 1:
                        snakeskin = DragonHide.SNAKESKIN_CHAPS;
                        break;
                    case 2:
                        snakeskin = DragonHide.SNAKESKIN_VAMBRACES;
                        break;
                    case 3:
                        snakeskin = DragonHide.SNAKESKIN_BANDANA;
                        break;
                    case 4:
                        snakeskin = DragonHide.SNAKESKIN_BOOTS;
                        break;
                }
                if (snakeskin == null) {
                    end();
                    return true;
                }
                int amount = optionSelect.getAmount(buttonId);
                if (amount == 0) {
                    end();
                    return true;
                }
                if (amount == -1) {
                    final DragonHide finalSnakeskin = snakeskin;
                    player.setAttribute("runscript", new RunScript() {

                        @Override
                        public boolean handle() {
                            player.getPulseManager().run(new DragonCraftPulse(player, null, finalSnakeskin, (int) getValue()));
                            return true;
                        }
                    });
                    player.getDialogueInterpreter().sendInput(false, "Enter amount:");
                    return true;
                }
                player.getPulseManager().run(new DragonCraftPulse(player, null, snakeskin, amount));
                return true;
            case "dragon":
                int index = 0;
                if (buttonId >= 8886 && buttonId <= 8889) {
                    index = 1;
                }
                if (buttonId >= 8890 && buttonId <= 8893) {
                    index = 2;
                }
                if (buttonId >= 8894 && buttonId <= 8897) {
                    index = 3;
                }
                DragonHide hide = null;
                if (index == 1) {
                    switch (leather) {
                        case LeatherCrafting.GREEN_LEATHER:
                            hide = DragonHide.GREEN_D_HIDE_BODY;
                            break;
                        case LeatherCrafting.BLUE_LEATHER:
                            hide = DragonHide.BLUE_D_HIDE_BODY;
                            break;
                        case LeatherCrafting.RED_LEATHER:
                            hide = DragonHide.RED_D_HIDE_BODY;
                            break;
                        case LeatherCrafting.BLACK_LEATHER:
                            hide = DragonHide.BLACK_D_HIDE_BODY;
                            break;
                    }
                }
                if (index == 2) {
                    switch (leather) {
                        case LeatherCrafting.GREEN_LEATHER:
                            hide = DragonHide.GREEN_D_HIDE_VAMBS;
                            break;
                        case LeatherCrafting.BLUE_LEATHER:
                            hide = DragonHide.BLUE_D_HIDE_VAMBS;
                            break;
                        case LeatherCrafting.RED_LEATHER:
                            hide = DragonHide.RED_D_HIDE_VAMBS;
                            break;
                        case LeatherCrafting.BLACK_LEATHER:
                            hide = DragonHide.BLACK_D_HIDE_VAMBS;
                            break;
                    }
                }
                if (index == 3) {
                    switch (leather) {
                        case LeatherCrafting.GREEN_LEATHER:
                            hide = DragonHide.GREEN_D_HIDE_CHAPS;
                            break;
                        case LeatherCrafting.BLUE_LEATHER:
                            hide = DragonHide.BLUE_D_HIDE_CHAPS;
                            break;
                        case LeatherCrafting.RED_LEATHER:
                            hide = DragonHide.RED_D_HIDE_CHAPS;
                            break;
                        case LeatherCrafting.BLACK_LEATHER:
                            hide = DragonHide.BLACK_D_HIDE_CHAPS;
                            break;
                    }
                }
                switch (buttonId) {
                    case 8889:
                    case 8893:
                    case 8897:
                        amt = 1;
                        break;
                    case 8888:
                    case 8892:
                    case 8896:
                        amt = 5;
                        break;
                    case 8887:
                    case 8891:
                    case 8895:
                        amt = 10;
                        break;
                    case 8886:
                    case 8890:
                    case 8894:
                        final DragonHide hidee = hide;
                        player.setAttribute("runscript", new RunScript() {

                            @Override
                            public boolean handle() {
                                player.getPulseManager().run(new DragonCraftPulse(player, null, hidee, (int) getValue()));
                                return true;
                            }
                        });
                        player.getDialogueInterpreter().sendInput(false, "Enter amount:");
                        return true;
                }
                player.getPulseManager().run(new DragonCraftPulse(player, null, hide, amt));
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 48923 };
    }
}
