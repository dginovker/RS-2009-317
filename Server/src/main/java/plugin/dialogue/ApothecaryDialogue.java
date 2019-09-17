package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.quest.impl.CurseOfTheUndead;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the dialogue plugin used for the apothecary npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ApothecaryDialogue extends DialoguePlugin {

    /**
     * Represents the potion requirements.
     */
    private static final Item[] POTION_ITEMS = new Item[]{ new Item(223), new Item(225), new Item(Item.COINS, 5) };

    /**
     * Represents the potion item.
     */
    private static final Item POTION = new Item(115);

    /**
     * Represents the unkown potion.
     */
    private static final Item UNKNOWN_POTION = new Item(195, 1);

    /**
     * Represents the cadava berries item.
     */
    private static final Item CADAVA_BERRIES = new Item(753);

    /**
     * Represents the cadava potion item.
     */
    private static final Item CADAVA_POTION = new Item(756);

    /**
     * Constructs a new {@code ApothecaryDialogue} {@code Object}.
     */
    public ApothecaryDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code ApothecaryDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public ApothecaryDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ApothecaryDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).getStage() == 2) {
            npc("Hm... Ah, very complicated.");
            stage = 1004;
            return true;
        }
        if (player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).getStage() == 3) {
            npc("Hello, " + TextUtils.formatDisplayName(player.getName()) + ", do you have the ashes yet?");
            stage = 1010;
            return true;
        }
        if (player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).getStage() == 4 && !player.getBank().contains(Item.STRANGE_POTION) &&
            !player.getInventory().contains(Item.STRANGE_POTION)) {
            player("Can I have another potion for the strange old man?");
            stage = 1015;
            return true;
        }
        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I am the Apothecary. I brew potions.", "Do you need anything specific?");
        stage = 1;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        int buttonId = -1;
        if (optionSelect != null) {
            buttonId = optionSelect.getButtonId();
        }
        switch (stage) {
            case 1:
                if (player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).getStage() == 1) {
                    interpreter.sendOptions("Select an Option", "Can you make a strength potion?", "Do you know a potion to mame hair fall out?", "Have you got any good potions to give away?", "Can you take a look at these notes?");
                    stage = 1000;
                    break;
                }
                interpreter.sendOptions("Select an Option", "Can you make a strength potion?", "Do you know a potion to mame hair fall out?", "Have you got any good potions to give away?");
                stage = 2;
                break;
            case 2:
                switch (buttonId) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you make a strength potion?");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Do you know a potion to make hair fall out?");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Have you got any good potions to give away?");
                        stage = 140;
                        break;
                }
                break;
            case 10:
                if (player.getInventory().containItems(223, 225)) {
                    npc("Certainly, just hand over the ingredients and 5 coins.");
                    stage = 50;
                    return true;
                }
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Yes, but the ingredients are a little hard to find. If you", "ever get them I will make it for you, for a fee.");
                stage = 11;
                break;
            case 50:
                if (!player.getInventory().contains(Item.COINS, 5)) {
                    end();
                    player.getActionSender().sendMessage("You need 5 gold coins to do that.");
                    return true;
                }
                interpreter.sendPlaneMessage("You hand over the ingredients and money.");
                stage = 51;
                break;
            case 51:
                if (player.getInventory().remove(POTION_ITEMS)) {
                    player.getInventory().add(POTION);
                    end();
                }
                break;
            case 11:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "So what are the ingredients?");
                stage = 12;
                break;
            case 12:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "You'll need to find the eggs of the deadly read spider and a", "limpwurt root.");
                stage = 13;
                break;
            case 13:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh and you'll have to pay me 5 coins.");
                stage = 14;
                break;
            case 14:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Ok, I'll look out for them.");
                stage = 15;
                break;
            case 15:
                end();
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I do indeed. I gave it to my mother. That's why I now live", "alone.");
                stage = 21;
                break;
            case 21:
                end();
                break;
            case 30:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Ok then. Try this potion.");
                player.getInventory().add(UNKNOWN_POTION);
                stage = 31;
                break;
            case 31:
                end();
                break;
            case 40:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "What a strange and morbid request! I can as it happens.", "The berry of the cadava bush, prepared properly, will", "induce a coma so deep that you will seem to be dead. It's", "very dangerous.");
                stage = 41;
                break;
            case 41:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I have the other ingredients, but I'll need you to bring me", "one bunch of cadava berries.");
                stage = 42;
                break;
            case 42:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I'll bear that in mind.");
                stage = 43;
                break;
            case 43:
                end();
                break;
            case 140:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Sorry, charity is not my strong point.");
                stage = 141;
                break;
            case 141:
                end();
                break;
            case 1000:
                switch (buttonId) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can you make a strength potion?");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Do you know a potion to make hair fall out?");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Have you got any good potions to give away?");
                        stage = 140;
                        break;
                    case 4:
                        player("Can you take a look at these notes?", "They were given to me by a strange old man", "who says he feels ill and isn't", "sure how long he has to live!");
                        stage = 1001;
                        break;
                }
                break;
            case 1001:
                if (player.getInventory().contains(291)) {
                    npc("Sure! Hand them here.");
                    stage = 1002;
                    break;
                }
                npc("What notes? Are you feeling okay " + TextUtils.formatDisplayName(player.getUsername()) + "?");
                stage = END;
                break;
            case 1002:
                player.getInventory().remove(new Item(291));
                player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).setStage(2);
                player.getQuestRepository().update(player);
                interpreter.sendItemMessage(291, "You hand the notes over to the apothecary.");
                stage = 1003;
                break;
            case 1003:
                npc("Hm... Ah, very complicated.");
                stage = 1004;
                break;
            case 1004:
                npc("I see the problem here...", "I can create a potion which will cure anyone", "with this disease.");
                stage = 1005;
                break;
            case 1005:
                npc("I am going to need you to get me a few things though.", "First of all, something undead is causing these", "outbreaks. So what I will need are the ashes", "of anything undead that they may");
                stage = 1006;
                break;
            case 1006:
                npc("have been in contact with recently.");
                stage = 1007;
                break;
            case 1007:
                player("Well, the strange old man said he felt it had something", "to do with the mounds he is always digging on.", "Under them lie the 6 Barrows brothers who are", "restless.");
                stage = 1008;
                break;
            case 1008:
                npc("Oh! Quite well then.", "I need you to defeat each brother, and bring", "me their ashes, so I can create a potion.");
                stage = 1009;
                player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).setStage(3);
                player.getQuestRepository().update(player);
                break;
            case 1009:
                player("Okay, I will do my best.");
                stage = END;
                break;
            case 1010:
                if (player.getInventory().contains(Item.AHRIM_ASHES, Item.DHAROK_ASHES,
                    Item.GUTHAN_ASHES, Item.KARIL_ASHES, Item.TORAG_ASHES,
                    Item.VERAC_ASHES)) {
                    player("Yes, I have them here.");
                    stage = 1012;
                    break;
                }
                player("No, I don't have them all yet.");
                stage = 1011;
                break;
            case 1011:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Do you need anything specific?");
                stage = 1;
                break;
            case 1012:
                npc("Okay, give them to me and I'll create the potion.");
                stage = 1013;
                break;
            case 1013:
                if (player.getInventory().remove(new Item(Item.AHRIM_ASHES), new Item(Item.DHAROK_ASHES),
                    new Item(Item.GUTHAN_ASHES), new Item(Item.KARIL_ASHES), new Item(Item.TORAG_ASHES),
                    new Item(Item.VERAC_ASHES))) {
                    player.getInventory().add(new Item(Item.STRANGE_POTION), true);
                    player.getQuestRepository().getQuest(CurseOfTheUndead.NAME).setStage(4);
                    player.getQuestRepository().update(player);
                    interpreter.sendItemMessage(Item.STRANGE_POTION, "The apothecary hands you a strange potion!");
                    stage = 1014;
                    break;
                }
                npc("I can't make a potion without the ashes!");
                stage = END;
                break;
            case 1014:
                npc("Take the potion to the strange old man, it should", "be the cure he needs.");
                stage = END;
                break;
            case 1015:
                npc("Sure, here you go, I created extra", "just in case!");
                stage = 1016;
                break;
            case 1016:
                player.getInventory().add(new Item(Item.STRANGE_POTION), true);
                interpreter.sendItemMessage(Item.STRANGE_POTION, "The apothecary hands you a strange potion!");
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
        return new int[]{ 638 };
    }
}
