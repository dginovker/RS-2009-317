package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the gam fred npc.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class GamFredDialoue extends DialoguePlugin {

    /**
     * Constructs a new {@code GamFredDialoue} {@code Object}.
     */
    public GamFredDialoue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code GamFredDialoue} {@code Object}.
     *
     * @param player the player.
     */
    public GamFredDialoue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new GamFredDialoue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        if (args.length > 1) {
            interpreter.sendDialogues(player, null, "May I have a shield please?");
            stage = 13;
            return true;
        }
        interpreter.sendDialogues(npc, null, "Ello there. I'm Gamfred, the engineer in this here guild.", "Have you seen my catapult?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendOptions("Select an Option", "That's not a catapult, it's a large crossbow.", "Yes, beautiful piece of enigneering.", "No, where is it?", "May I claim my tokens please?", "Bye!");
                stage = 1;
                break;
            case 1:
                if (optionSelect.getButtonId() == 1) {
                    interpreter.sendDialogues(player, null, "That's not a catapult, it's a large crossbow.");
                    stage = 2;
                } else if (optionSelect.getButtonId() == 2) {
                    interpreter.sendDialogues(player, null, "Yes, beautiful piece of engineering.");
                    stage = 4;
                } else if (optionSelect.getButtonId() == 3) {
                    interpreter.sendDialogues(player, null, "No, where is it?");
                    stage = 15;
                } else if (optionSelect.getButtonId() == 4) {
                    end();
                    player.getDialogueInterpreter().open("wg:claim-tokens", npc.getId());
                } else if (optionSelect.getButtonId() == 5) {
                    interpreter.sendDialogues(player, null, "Bye!");
                    stage = 16;
                }
                break;
            case 2:
                interpreter.sendDialogues(npc, null, "WHAT!? I'll have you know that is the finest piece of", "dwarven engineering for miles around! How DARE you", "insult my work!");
                stage = 3;
                break;
            case 3:
                end();
                break;
            case 4:
                interpreter.sendDialogues(npc, null, "Nice to meet someone who appreicates fine work, have", "you tried it out yet?");
                stage = 5;
                break;
            case 5:
                interpreter.sendOptions("Select an Option", "Yes", "No, how do I do that?");
                stage = 6;
                break;
            case 6:
                if (optionSelect.getButtonId() == 1) {
                    interpreter.sendDialogues(player, null, "Yes.");
                    stage = 7;
                } else if (optionSelect.getButtonId() == 2) {
                    interpreter.sendDialogues(player, null, "No, how do I do that?");
                    stage = 17;
                }
                break;
            case 7:
                interpreter.sendDialogues(npc, null, "What did you think?");
                stage = 8;
                break;
            case 8:
                interpreter.sendOptions("Select an Option", "It was ok I guess.", "It was fun!", "I didn't like it.", "May I have a shield please?");
                stage = 9;
                break;
            case 9:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, null, "It was ok I guess.");
                        stage = 10;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, null, "It was fun!");
                        stage = 11;
                        break;
                    case 3:
                        interpreter.sendDialogues(player, null, "I didn't like it.");
                        stage = 12;
                        break;
                    case 4:
                        interpreter.sendDialogues(player, null, "May I have a shield please?");
                        stage = 13;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, null, "Well I guess not everyone will like it.");
                stage = 3;
                break;
            case 11:
                interpreter.sendDialogues(npc, null, "Glad to hear it. Try it again sometime. We have more", "tests to run.");
                stage = 3;
                break;
            case 12:
                interpreter.sendDialogues(npc, null, "Well I guess not everyone will like it. But give it another", " chance before you go.");
                stage = 3;
                break;
            case 13:
                if (player.getBank().contains(8856, 1) || player.getInventory().contains(8856, 1) || player.getEquipment().contains(8856, 1)) {
                    interpreter.sendDialogues(npc, null, "Silly muffin, you have one already!");
                    stage = 3;
                } else {
                    interpreter.sendDialogues(npc, null, "Of course!");
                    stage = 14;
                }
                break;
            case 14:
                if (player.getInventory().hasRoomFor(new Item(8856))) {
                    player.getInventory().add(new Item(8856));
                    interpreter.sendItemMessage(8856, "The dwarf hands you a large shield.");
                    stage = 3;
                } else {
                    interpreter.sendDialogues(npc, null, "Muffin make some room in your inventory first!");
                    stage = 3;
                }
                break;
            case 15:
                interpreter.sendDialogues(npc, null, "Are ye blind lad? Tis over there in the next room with me", "assistant working it!");
                stage = 3;
                break;
            case 16:
                interpreter.sendDialogues(npc, null, "Come back soon! My catapult needs more test subjects.");
                stage = 3;
                break;
            case 17:
                interpreter.sendDialogues(npc, null, "Well ye take the big defence shield in both hands and", "watch the catapult. My assitant will fire different things", "at you and you need to defend against them. To see", "what might be comming your way and wich defensive");
                stage = 18;
                break;
            case 18:
                interpreter.sendDialogues(npc, null, "mode to choose, see the poster on the wall.");
                stage = 19;
                break;
            case 19:
                interpreter.sendOptions("Select an Option", "May I have a shield please?", "Sounds boring.");
                stage = 20;
                break;
            case 20:
                switch (optionSelect.getButtonId()) {
                    case 1:
                        interpreter.sendDialogues(player, null, "May I have a shield please?");
                        stage = 13;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, null, "Sounds boring.");
                        stage = 21;
                        break;
                }
                break;
            case 21:
                interpreter.sendDialogues(npc, null, "Your loss...");
                stage = 3;
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 4287 };
    }
}
