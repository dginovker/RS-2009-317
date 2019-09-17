package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for nulodion.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class NulodionDialogue extends DialoguePlugin {

    /**
     * Constructs a new {@code NulodionDialogue} {@code Object}.
     */
    public NulodionDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code NulodionDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public NulodionDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new NulodionDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello traveller, how's things?");
                stage = 1;
                break;
            case 1:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Not bad thanks, yourself?");
                stage = 2;
                break;
            case 2:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I'm good just working hard as usual...");
                stage = 3;
                break;
            case 3:
                interpreter.sendOptions("Select an Option", "I was hoping you might sell me a cannon?", "Well, take care of yourself the.", "I want to know more about the cannon.", "I've lost my cannon.");
                stage = 4;
                break;
            case 4:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I was hoping you might sell me a cannon.");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Can I look at the seperate parts please?");
                        stage = 200;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, that's too much for me.");
                        stage = 300;
                        break;
                    case 4:
                        interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course!");
                        stage = 201;
                        break;

                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hmmmmmm...");
                stage = 11;
                break;
            case 11:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "I shouldn't really, but as you helped us so much, well, I", "could sort something out. I'll warn you though, they don't", "come cheap!");
                stage = 12;
                break;
            case 12:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "How much?");
                stage = 13;
                break;
            case 13:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "For the full setup, 750,000 coins. Or I can sell you the", "seperate parts... but it'll cost extra!");
                stage = 14;
                break;
            case 14:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "That's not cheap!");
                stage = 15;
                break;
            case 15:
                interpreter.sendOptions("Select an Option", "Okay, I'll take a cannon please.", "Can I look at the seperate parts please?", "Sorry, that's too much for me.", "Have you any ammo or instructions to sell?");
                stage = 16;
                break;
            case 16:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Okay, I'll take a cannon please.");
                        stage = 100;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Well, take care of yourself then.");
                        stage = 2000;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I want to know more about the cannon.");
                        stage = 600;
                        break;
                    case 4:
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "I've lost my cannon.");
                        stage = 500;
                        break;

                }
                break;
            case 100:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Okay then, but keep it quiet... This thing's top secret!");
                stage = 101;
                break;
            case 101:
                if (player.getInventory().contains(Item.COINS, 750000) && player.getInventory().freeSlots() >= 6) {
                    player.getInventory().remove(new Item(Item.COINS, 750000));
                    player.getInventory().add(new Item(6, 1));
                    player.getInventory().add(new Item(8, 1));
                    player.getInventory().add(new Item(12, 1));
                    player.getInventory().add(new Item(10, 1));
                    player.getInventory().add(new Item(5, 1));
                    player.getInventory().add(new Item(4, 1));
                    interpreter.sendPlaneMessage("You give the Cannon engineer 750,000 coins...");
                    stage = 103;

                } else {
                    if (player.getInventory().freeSlots() >= 6) {
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I don't have enough coins for that.");
                        stage = 105;
                    }
                    if (player.getInventory().contains(Item.COINS, 750000)) {
                        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Sorry, I don't have enough inventory space.");
                        stage = 105;
                    }
                }
                break;
            case 105:
                end();
                break;
            case 103:

                interpreter.sendPlaneMessage("He gives you the four parts that make the cannon,", "plus an ammo mould, and an instruction manual.");
                stage = 104;

                break;
            case 104:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "There you go, you be careful with that thing.");
                stage = 106;
                break;
            case 106:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Will do, take care mate.");
                stage = 107;
                break;
            case 107:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Take care adventurer.");
                stage = 108;
                break;
            case 108:
                end();
                break;
            case 200:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Of course!");
                stage = 201;
                break;
            case 201:
                end();
                Shops.NULODIN_SHOP.open(player);
                break;
            case 300:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Fair enough, it's too much for most of us.");
                stage = 301;
                break;
            case 301:
                end();
                break;
            case 2000:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Indeed I will adventurer.");
                stage = 2001;
                break;
            case 2001:
                end();
                break;
            case 600:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "There's only so much I can tell you, adventurer. We've", "been working on this little beauty for some time now.");
                stage = 601;
                break;
            case 601:
                interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Is it effective?");
                stage = 602;
                break;
            case 602:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "In short bursts it's very effective, the most destructive", "weapon to date. The cannon automatically targets", "monsters close by. You just have to make the ammo and", "let rip.");
                stage = 603;
                break;
            case 603:
                end();
                break;
            case 500:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Oh dear, I'm only allowed to replace cannons that were", "stolen in action. I'm sorry, but you'll have to buy a new", "set.");
                stage = 501;
                break;
            case 501:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 209 };
    }
}
