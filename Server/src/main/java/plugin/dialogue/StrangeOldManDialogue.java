package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.global.quest.impl.CurseOfTheUndead;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the dialogue for the Strange old man.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class StrangeOldManDialogue extends DialoguePlugin {

    /**
     * Represents the {@link org.gielinor.game.content.global.quest.impl.CurseOfTheUndead} {@link org.gielinor.game.content.global.quest.Quest}.
     */
    private Quest quest;

    public StrangeOldManDialogue() {
    }

    public StrangeOldManDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new StrangeOldManDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        quest = player.getQuestRepository().getQuest(CurseOfTheUndead.NAME);
        if (!quest.hasRequirements() || player.getQuestRepository().isComplete(quest)) {
            npc("Dig, dig, dig.");
            stage = END;
            return true;
        }
        switch (quest.getStage()) {
            case 0:
                npc("Dig, dig, dig.");
                stage = 0;
                return true;
            case 5:
                interpreter.sendPlaneMessage("You feel weird...", "Something's not right...");
                stage = 1006;
                return true;
            case 10:
                npc("Wow! I feel so much better!", "Whatever Surok has done, seems to be reversed.", "I am forever in your debt, " + TextUtils.formatDisplayName(player.getUsername()) + "!");
                stage = 1007;
                return true;
        }
        npc("How goes the quest " + TextUtils.formatDisplayName(player.getName()) + "?");
        stage = 1000;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        int buttonId = -1;
        if (optionSelect != null) {
            buttonId = optionSelect.getButtonId();
        }
        switch (stage) {
            case 0:
                options("Who are you?", "Do you have any quests for me?", "Nothing.");
                stage = 1;
                break;
            case 1:
                switch (buttonId) {
                    case 1:
                        player("Who are you?");
                        stage = 2;
                        break;
                    case 2:
                        player("Do you have any quests for me?");
                        stage = 5;
                        break;
                    case 3:
                        end();
                        break;
                }
                break;
            case 2:
                npc("Dig... Oh... Why, I'm the caretaker of these", "here mounds. Under which lie the 6 undead Barrows", "brothers.");
                stage = 3;
                break;
            case 3:
                player("That doesn't sound very appealing!");
                stage = 4;
                break;
            case 4:
                npc("I need to just keep digging!");
                stage = 0;
                break;
            case 5:
                if (player.getQuestRepository().isComplete(quest)) {
                    npc("No... I do have one for myself however, I must dig!");
                    stage = END;
                    break;
                }
                npc("In fact, I do!", "In my travels, I have noticed those who have", "visited these here mounds, have been dying of", "a disease unknown to anyone, I wasn't sure it");
                stage = 6;
                break;
            case 6:
                npc("was from here until I started to feel ill myself.", "I believe it has something to do with", "the undead brothers lying beneath these mounds.");
                stage = 7;
                break;
            case 7:
                npc("If its a quest you want, its a quest you've got!", "I need you to take these notes to the", "apothecary in Varrock.");
                stage = 8;
                break;
            case 8:
                player.getInventory().add(new Item(291), true);
                quest.setState(QuestState.STARTED);
                quest.setStage(1);
                player.getQuestRepository().update(player);
                interpreter.sendItemMessage(291, "The Strange old man gives you some notes.");
                stage = 9;
                break;
            case 9:
                player("Okay, I will take them to him now!");
                stage = END;
                break;

            case 1000:
                switch (quest.getStage()) {
                    case 1:
                        if (!player.getBank().contains(291) || !player.getInventory().contains(291)) {
                            player("I've lost the notes you have given me, can I have a copy?");
                            stage = 1001;
                            break;
                        }
                        player("I still need to take these notes to the apothecary.");
                        stage = 1002;
                        break;
                    case 2:
                    case 3:
                        player("I still need to obtain ashes for the apothecary", "so he can create a cure potion.");
                        stage = END;
                        break;
                    case 4:
                        if (player.getInventory().contains(Item.STRANGE_POTION)) {
                            player("I have a potion from the apothecary,", "he says it should work on healing you!");
                            stage = 1003;
                            break;
                        }
                        player("I have a potion from the apothecary, but I lost it.");
                        stage = 1004;
                        break;
                }
                break;
            case 1001:
                player.getInventory().add(new Item(291), true);
                quest.setState(QuestState.STARTED);
                quest.setStage(1);
                player.getQuestRepository().update(player);
                interpreter.sendItemMessage(291, "The Strange old man gives you some notes.");
                stage = END;
                break;
            case 1002:
                npc("Well, hurry " + TextUtils.formatDisplayName(player.getUsername()) + "! We may not have much time", "left!");
                stage = END;
                break;
            case 1003:
                if (player.getInventory().remove(new Item(Item.STRANGE_POTION))) {
                    npc("Well, give me it, there's hope of living after all!");
                    stage = 1005;
                    quest.setState(QuestState.STARTED);
                    quest.setStage(5);
                    player.getQuestRepository().update(player);
                    break;
                }
                npc("You lie!");
                stage = END;
                break;
            case 1004:
                npc("Well, find it! Dig if you have to!");
                stage = END;
                break;
            case 1005:
                interpreter.sendPlaneMessage("You feel weird...", "Something's not right...");
                stage = 1006;
                break;
            case 1006:
                end();
                player.getDialogueInterpreter().open(5835, Repository.findNPC(5835), true);
                break;
            case 1007:
                npc("Please, accept these rewards for your help!");
                stage = 1008;
                return true;
            case 1008:
                end();
                quest.finish();
                break;
            case END:
                end();
                return true;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2024 };
    }
}
