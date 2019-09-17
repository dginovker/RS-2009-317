package plugin.dialogue;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.IronmanMode;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the dialogue for the Iron Man Adam and Paul.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class IronMenDialogue extends DialoguePlugin {

    /**
     * Represents the Iron Man helmet.
     */
    private static final Item IRON_MAN_HELMET = new Item(Item.IRONMAN_HELM);
    /**
     * Represents the Iron Man platebody.
     */
    private static final Item IRON_MAN_BODY = new Item(Item.IRONMAN_PLATEBODY);
    /**
     * Represents the Iron Man platelegs.
     */
    private static final Item IRON_MAN_LEGS = new Item(Item.IRONMAN_PLATELEGS);
    /**
     * Represents the ultimate Iron Man helmet.
     */
    private static final Item ULTIMATE_IRON_MAN_HELMET = new Item(Item.ULTIMATE_IRONMAN_HELM);
    /**
     * Represents the ultimate Iron Man platebody.
     */
    private static final Item ULTIMATE_IRON_MAN_BODY = new Item(Item.ULTIMATE_IRONMAN_PLATEBODY);
    /**
     * Represents the ultimate Iron Man platelegs.
     */
    private static final Item ULTIMATE_IRON_MAN_LEGS = new Item(Item.ULTIMATE_IRONMAN_PLATELEGS);

    private static final Item HARDCORE_IRON_MAN_HELMET = new Item(Item.HARDCORE_IRONMAN_HELM);
    private static final Item HARDCORE_IRON_MAN_BODY = new Item(Item.HARDCORE_IRONMAN_BODY);
    private static final Item HARDCORE_IRON_MAN_LEGS = new Item(Item.HARDCORE_IRONMAN_LEGS);

    /**
     * Constructs a new <code>AdamDialogue</code>.
     */
    public IronMenDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new <code>AdamDialogue</code>.
     *
     * @param player The player.
     */
    public IronMenDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new IronMenDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length == 2) {
            handleArmour();
            return true;
        }
        if (player.isIronman()) {
            npc("Hail, " + player.getSavedData().getGlobalData().getIronmanMode().getFormattedName() + "!", "What can we do for you?");
            stage = 0;
            return true;
        }
        npc("Hello, " + TextUtils.formatDisplayName(player.getName()) + ". We're the Iron Man tutors.", "What can we do for you?");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        final IronmanMode degradedMode = IronmanMode.getDegradedMode(player.getSavedData().getGlobalData().getIronmanMode());
        switch (stage) {
            case 0:
                if (player.isIronman()) {
                    options("Can I become a " + degradedMode.getFormattedName() + "?", "Have you any armour for me, please?", "I'm fine, thanks.");
                } else {
                    options("Tell me about Iron Men.", "Have you any armour for me, please?", "I'm fine, thanks.");
                }
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        if (player.isIronman()) {
                            player("Can I become a " + degradedMode.getFormattedName() + " instead?");
                            stage = 10;
                        } else {
                            player("Tell me about Iron Men.");
                            stage = 2;
                        }
                        break;
                    case THREE_OPTION_TWO:
                        player("Have you any armour for me, please?");
                        stage = 9;
                        break;
                    case THREE_OPTION_THREE:
                        player("I'm fine, thanks.");
                        stage = END;
                        break;
                }
                break;
            case 2:
                npc("When you play as an <col=7F0000>Iron Man<col=000000>, you do everything", "for yourself. You don't trade with other players, or take", "their items, or accept their help.");
                stage = 3;
                break;
            case 3:
                npc("As an <col=7F0000>Iron Man<col=000000>, you choose to have these restrictions", "imposed on you, so everyone knows you're doing it", "properly.");
                stage = 4;
                break;
            case 4:
                npc("For the ultimate challenge, you can choose to become", "an <col=00007F>Ultimate Iron Man</col>, a game mode inspired by the", "player <col=00007F>IronNoBank</col>.");
                stage = 5;
                break;
            case 5:
                npc("In addition to the standard restrictions, <col=00007F>Ultimate Iron", "<col=00007F>Men</col> are blocked from using the bank, and they drop all", "their items when they die.");
                stage = 6;
                break;
            case 6:
                npc("You can choose to become an <col=7F0000>Iron Man</col> while you're", "selecting your starter package. Although you can't do that", "on this account, you might like to bear it in mind", "for the future.");
                stage = 7;
                break;
            case 7:
                options("Have you any armour for me, please?", "I'm fine, thanks.");
                stage = 8;
                break;
            case 8:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Have you any armour for me, please?");
                        stage = 9;
                        break;
                    case TWO_OPTION_TWO:
                        player("I'm fine, thanks.");
                        stage = END;
                        break;
                }
                break;
            case 9:
                handleArmour();
                break;
            case 10:
                npc("Yeah, sure! But are you sure though?", "<col=ff0000>This cannot be undone!</col>");
                stage = 11;
                break;
            case 11:
                options("Nevermind.", "Yeah I'm sure!");
                stage = 12;
                break;
            case 12:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Nevermind.");
                        stage = END;
                        break;
                    case TWO_OPTION_TWO:
                        player("Yeah, I'm sure!");
                        stage = 13;
                        break;
                }
                break;
            case 13:
                npc("As you wish...");
                stage = 14;
                break;
            case 14:
                stage = END;
                player.getActionSender().sendMessage("You feel the shackles binding you become slightly less tight.");
                player.getActionSender().sendMessage("You're now a " + degradedMode.getFormattedName() + ".");
                player.getSavedData().getGlobalData().setStarterPackage(degradedMode.getStarterPackage());
            case END:
                end();
                break;
        }
        return true;
    }

    /**
     * Handles giving the player armour.
     */
    private void handleArmour() {
        if (!player.isIronman()) {
            npc("You're not an Iron Man.", "Our armour is only for them.");
            stage = END;
            return;
        }
        boolean hasHelmet = true;
        boolean hasBody = true;
        boolean hasLegs = true;
        int requiredInventory = 0;

        Item[] armourSet = getArmourSet(player.getSavedData().getGlobalData().getIronmanMode());

        if (armourSet == null) {
            end();
            return;
        }

        if (!player.hasItem(armourSet[0])) {
            hasHelmet = false;
            requiredInventory += 1;
        }
        if (!player.hasItem(armourSet[1])) {
            hasBody = false;
            requiredInventory += 1;
        }
        if (!player.hasItem(armourSet[2])) {
            hasLegs = false;
            requiredInventory += 1;
        }
        if (!hasHelmet || !hasBody || !hasLegs) {
            if (player.getInventory().freeSlots() < requiredInventory) {
                npc("It seems you don't have enough space for the armour.");
                stage = END;
                return;
            }
            if (!hasHelmet) {
                player.getInventory().add(armourSet[0], true);
            }
            if (!hasBody) {
                player.getInventory().add(armourSet[1], true);
            }
            if (!hasLegs) {
                player.getInventory().add(armourSet[2], true);
            }
            npc("There you go. Wear it with pride.");
            stage = END;
            return;
        }
        npc("I think you've already got the whole set.");
        stage = END;
        return;
    }

    private Item[] getArmourSet(IronmanMode mode) {
        switch (mode) {
            case IRONMAN:
                return new Item[]{ IRON_MAN_HELMET, IRON_MAN_BODY, IRON_MAN_LEGS };
            case ULTIMATE_IRONMAN:
                return new Item[]{ ULTIMATE_IRON_MAN_HELMET, ULTIMATE_IRON_MAN_BODY, ULTIMATE_IRON_MAN_LEGS };
            case HARDCORE_IRONMAN:
                return new Item[]{ HARDCORE_IRON_MAN_HELMET, HARDCORE_IRON_MAN_BODY, HARDCORE_IRON_MAN_LEGS };
            default:
                return null;
        }
    }

    @Override
    public int[] getIds() {
        return new int[]{ NPCDefinition.ADAM, NPCDefinition.PAUL, NPCDefinition.JUAN };
    }
}
