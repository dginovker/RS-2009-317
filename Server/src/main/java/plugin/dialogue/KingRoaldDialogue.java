package plugin.dialogue;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.appearance.Gender;
import org.gielinor.game.world.repository.Repository;

import plugin.interaction.object.KingGjukiChestPlugin;

/**
 * Represents the {@link org.gielinor.game.content.dialogue.DialoguePlugin} for King Roald.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KingRoaldDialogue extends DialoguePlugin {

    /**
     * The {@link org.gielinor.game.content.global.quest.impl.TheLostKingdom} quest.
     */
    private Quest quest;
    /**
     * The Aeonisig Raispher NPC.
     */
    private NPC aeon;

    /**
     * Constructs a new {@code KingRoaldDialogue} {@code Object}.
     */
    public KingRoaldDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code KingRoaldDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public KingRoaldDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new KingRoaldDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        aeon = Repository.findNPC(4710);
        quest = player.getQuestRepository().getQuest(TheLostKingdom.NAME);
        if (quest.getStage() == 10) {
            npc("No more shall it go on. I need you to talk to him and",
                "attempt to figure out what he has planned for an attack",
                "on my Kingdom.");
            stage = 14;
            return true;
        }
        if (quest.getStage() == 15) {
            npc("Have you found anything out to work in our advantage?");
            stage = 16;
            return true;
        }
        if (quest.getStage() == 20) {
            npc("Have you found anything out to work in our advantage?");
            stage = 17;
            return true;
        }
        if (quest.getStage() == 21) {
            npc("Have you found anything out to work in our advantage?");
            stage = 16;
            return true;
        }
        if (quest.getStage() == 25) {
            player("Hello, King Roald. I have news!");
            stage = 19;
            return true;
        }
        if (quest.getStage() == 30) {
            if (player.getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                npc("Do you have the deed, " + player.getUsername() + "?");
                stage = 30;
                return true;
            }
            npc("Oh dear! I always suspected there was something about him!",
                "I have such poor taste when it comes to advisors!");
            stage = 27;
            return true;
        }
        interpreter.sendDialogues(player, FacialExpression.NO_EXPRESSION, "Hello.");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (stage) {
            case 0:
                interpreter.sendDialogues(npc, FacialExpression.NO_EXPRESSION, "Hello, " + player.getUsername() + ".", "What can I help you with?");
                stage = 1;
                break;
            case 1:
                interpreter.sendOptions("Select an Option", "Do you have any quests for me?", "Nothing.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        if (quest.getState() == QuestState.COMPLETED) {
                            npc("I'm afraid I do not have any quests for you.");
                            stage = END;
                            break;
                        }
                        if (quest.getState() == QuestState.NOT_STARTED) {
                            npc("Well... There is something extremely important that", "I think you could provide great assistance", "with.");
                            stage = 3;
                            break;
                        }
                        break;
                    case TWO_OPTION_TWO:
                        player("Nothing.");
                        stage = END;
                        break;
                }
                break;
            case 3:
                player.faceLocation(aeon.getLocation());
                aeon.faceLocation(npc.getLocation());
                npc.faceLocation(aeon.getLocation());
                interpreter.sendDialogues(aeon, FacialExpression.WORRIED, "Sir... As your advisor, I recommend that you", "do not tell this adventurer anything!");
                stage = 4;
                break;
            case 4:
                npc.faceLocation(aeon.getLocation());
                player.faceLocation(npc.getLocation());
                npc("Nonsense... They could be our best chance", "on claiming victory once again!");
                stage = 5;
                break;
            case 5:
                player.faceLocation(aeon.getLocation());
                interpreter.sendDialogues(aeon, FacialExpression.WORRIED, "I understand, but there is no guarantee we can", "trust " + (player.getAppearance().getGender() == Gender.MALE ? "him" : "her") + " to do what must be achieved.");
                stage = 6;
                break;
            case 6:
                player.faceLocation(npc.getLocation());
                interpreter.sendDialogues(npc, FacialExpression.ANNOYED, "We don't have much time left before the end of this", "Kingdom. I will not sit around doing absolutely", "nothing in the meantime!");
                stage = 7;
                break;
            case 7:
                npc.faceLocation(player.getLocation());
                player("I don't mean to interrupt, but do you need my", "help or not?");
                stage = 8;
                break;
            case 8:
                npc("Yes, " + player.getUsername() + ", I sincerely do.", "We haven't much time, so we must hurry.", "Will you help us?");
                stage = 9;
                break;
            case 9:
                interpreter.sendPlaneMessage(false, "Aeonisig Raispher lets out a sigh.");
                stage = 10;
                break;
            case 10:
                options("Yes, I will.", "No, I don't have time for this.");
                stage = 11;
                break;
            case 11:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, I will help you.");
                        stage = 12;
                        break;
                    case TWO_OPTION_TWO:
                        player("No, I don't have time for this.");
                        stage = END;
                        break;
                }
                break;
            case 12:
                npc("My Kingdom has fallen under attack of a",
                    "pesky King by the name of Gjuki Sorvott.",
                    "He is attempting to take over what",
                    "I have spent years building."
                );
                quest.start();
                stage = 13;
                break;
            case 13:
                npc("No more shall it go on. I need you to talk to him and",
                    "attempt to figure out what he has planned for an attack",
                    "on my Kingdom.");
                stage = 14;
                break;
            case 14:
                npc("You can find him in Jatizso.", "Just try to coerce him into telling you", "anything to our advantage!");
                stage = 15;
                break;
            case 15:
                quest.setStage(15);
                player("Okay, I will do my best.");
                stage = END;
                break;
            case 16:
                player("Not yet, I'm still working on it.");
                stage = END;
                break;
            case 17:
                player("I tried to talk to the king,", "he did not want to talk to me!");
                stage = 18;
                break;
            case 18:
                quest.setStage(21);
                npc("Hm... Maybe you should try wearing a disguise...",
                    "Go over to my chest and take out the Silly jester outfit.");
                stage = END;
                break;
            case 19:
                interpreter.sendDialogues(npc, FacialExpression.WORRIED,
                    "It will have to wait, " + player.getUsername() + ".",
                    "Somehow, King Gjuki has figured out our plot to", "find out his plan of attack.");
                stage = 20;
                break;
            case 20:
                interpreter.sendDialogues(npc, FacialExpression.ANGRY, "Not only are we in the dark again, but after he figured",
                    "out that you were spying on him, he caught us with our",
                    "guards down and somehow took the deed to MY kingdom!");
                stage = 21;
                break;
            case 21:
                player("What can we do?");
                stage = 22;
                break;
            case 22:
                npc("Well, after an attempt to retrieve the deed,", "we have lost a great warrior who has fallen", "to a guard of Gjuki.");
                stage = 23;
                break;
            case 23:
                npc("Before his death, he located the deed inside of a chest behind",
                    "King Gjuki's throne. We have no time to waste. I'm certain",
                    "he will be looking out for anyone");
                stage = 24;
                break;
            case 24:
                npc("attempting to retrieve my deed. If you wish to take a",
                    "chance, I recommend taking heavy combat gear to Jatizso,",
                    "in case you have to battle anyone who tries to stop you",
                    "from taking the deed.");
                stage = 25;
                break;
            case 25:
                npc.faceLocation(aeon.getLocation());
                player.faceLocation(aeon.getLocation());
                interpreter.sendDialogues(aeon, FacialExpression.LAUGHING, "You fools! I took the deed!", "You will never see your precious deed again!", "Why attempt suicide, " + player.getUsername() + "?! Just leave it be!");
                stage = 26;
                break;
            case 26:
                npc.faceLocation(player.getLocation());
                player.faceLocation(npc.getLocation());
                quest.setStage(30);
                npc("Oh dear! I always suspected there was something about him!",
                    "I have such poor taste when it comes to advisors!");
                stage = 27;
                break;
            case 27:
                npc.faceLocation(player.getLocation());
                player.faceLocation(npc.getLocation());
                npc("This is terrible, " + player.getUsername() + ", please! You must", "travel back to Jatizso, before they have", "time to plot any attacks against my Kingdom!");
                stage = 28;
                break;
            case 28:
                npc("With the knowledge Aeonisig has, it could corrupt", "my Kingdom forever!");
                stage = 29;
                break;
            case 29:
                player("I will get your deed back, King Roald!", "Don't worry!");
                stage = END;
                break;
            case 30:
                player("Yes! I have it right here!");
                stage = 31;
                break;
            case 31:
                if (!player.getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                    end();
                    break;
                }
                npc("Oh, thank goodness!", "How could I ever repay you?!");
                stage = 32;
                break;
            case 32:
                if (!player.getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                    end();
                    break;
                }
                npc("Wait! I've an idea! I have extra pairs of armoured gloves,",
                    "that are just taking up space. You can access my",
                    "personal chest here to purchase the gloves! All",
                    "I ask of you is to kill King Gjuki's guards to");
                stage = 33;
                break;
            case 33:
                if (!player.getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                    end();
                    break;
                }
                npc("show him that I mean business!");
                stage = 34;
                break;
            case 34:
                if (!player.getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                    end();
                    break;
                }
                npc("Thank you again, " + player.getUsername() + "! You have saved", "my Kingdom!");
                stage = 35;
                break;
            case 35:
                end();
                if (!player.getInventory().contains(KingGjukiChestPlugin.VARROCK_PALACE_DEED)) {
                    break;
                }
                player.getInventory().remove(KingGjukiChestPlugin.VARROCK_PALACE_DEED);
                quest.finish();
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 648 };
    }
}
