package plugin.dialogue;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

import plugin.interaction.item.MaxCapePlugin;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents the dialogue for the Mac NPC.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class MacDialogue extends DialoguePlugin {

    /**
     * The cost of the Max cape.
     */
    private final Item COST = new Item(Item.COINS, 2178000);
    /**
     * The Max cape and hood.
     */
    private final Item[] MAX_ITEMS = new Item[]{ new Item(Item.MAX_HOOD, 1), new Item(Item.MAX_CAPE, 1) };

    /**
     * Constructs a new {@code MacDialogue} {@code Object}.
     */
    public MacDialogue() {
    }

    /**
     * Constructs a new {@code MacDialogue} {@code Object}.
     *
     * @param player The player.
     */
    public MacDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new MacDialogue(player);
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
                npc("Hello there, " + TextUtils.formatDisplayName(player.getName()) + ".");
                stage = 1;
                break;
            case 1:
                options("Who are you?","What is my progress?", "Nothing.");
                stage = 2;
                break;
            case 2:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        player("Who are you?");
                        stage = 3;
                        break;
                    case THREE_OPTION_TWO:

                        final int
                            masteryInSkillsLeft = player.getSkills().getTotalSkills() - player.getSkills().getMasteredSkills(),
                            questsLeft = Math.toIntExact(player.getQuestRepository().getQuests().values().stream().filter(quest -> !player.getQuestRepository().isComplete(quest)).count());

                        final boolean learnedSlayer = player.getSavedData().getActivityData().hasLearnedSlayerOption(2);

                        final String[]       //todo: add diaries.
                            miscLines = new String[]{
                            "To receive the cape I need to:",
                            (masteryInSkillsLeft == 0 ? "<str>" : "") + "<col=08088A> Achieve Mastery in "+masteryInSkillsLeft+" more Skills.",
                            (questsLeft == 0 ? "<str>" : "") + "<col=08088A> Complete "+questsLeft+" more Quests.",
                            (learnedSlayer ? "<str>" : "") + "<col=08088A> Learn how to craft a slayer helmet.",
                            ""
                        }, skillsRequirement = IntStream.rangeClosed(0, 23)
                            .boxed()
                            .sorted(Comparator.comparingInt(player.getSkills()::getLevelsTillMastery).reversed())
                            .map(id ->
                                (player.getSkills().getLevelsTillMastery(id) == 0
                                    ? "<str> Mastered "+Skills.SKILL_NAME[id]
                                    : "Need to level up " +  (99 - player.getSkills().getStaticLevel(id)) +" more "+Skills.SKILL_NAME[id]+" levels.")
                            ).toArray(String[]::new);

                        player.getQuestMenuManager()
                            .setTitle("Max Cape Requirements")
                            .setLines(
                                Stream.of(miscLines, skillsRequirement)
                                .flatMap(Stream::of)
                                .toArray(String[]::new))
                            .send();

                        return true;
                    case THREE_OPTION_THREE:
                        end();
                        return true;
                }
                break;
            case 3:
                npc("My name is Mac, I've traveled these lands many times,", "questing, skilling, slaying, you name it!", "For those who have done the same, I reward them", "with a Max cape, which can be combined with other");
                stage = 4;
                break;
            case 4:
                npc("various capes to adjust to the style of the player.");
                stage = 5;
                break;
            case 5:
                options("How can I receive a Max cape?", "Nothing.");
                stage = 6;
                break;
            case 6:
                if (optionSelect == OptionSelect.TWO_OPTION_TWO) {
                    end();
                    return true;
                }
                player("How do I receive a Max cape?");
                stage = 7;
                break;
            case 7:
                if (MaxCapePlugin.hasRequirements(player, true)) {
                    npc("Well, it looks as if you've accomplished as much as", "I have!");
                    stage = 8;
                    break;
                }
                npc(
                    "To receive a Max cape, you need to have level 99",
                    "in all of your skills, have completed all quests and",
                    "achievement diaries, and must have unlocked all of",
                    "the abilities through Slayer by talking to a Slayer master.");
                stage = 13;
                break;
            case 8:
                npc("I will sell you a Max cape, for " + TextUtils.getFormattedNumber(COST.getCount()) + " coins.", "You will receive the Max cape and hood, the cape will", "allow you to teleport to various locations.");
                stage = 9;
                break;
            case 9:
                npc("Would you like to buy one for " + TextUtils.getFormattedNumber(COST.getCount()) + " coins?");
                stage = 10;
                break;
            case 10:
                options("Yes, pay " + TextUtils.getFormattedNumber(COST.getCount()) + " coins.", "No thank you.");
                stage = 11;
                break;
            case 11:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        player("Yes, I will pay " + TextUtils.getFormattedNumber(COST.getCount()) +
                            " coins for a Max cape.");
                        stage = 12;
                        break;
                    case TWO_OPTION_TWO:
                        player("No thank you.");
                        stage = END;
                        break;
                }
                break;
            case 12:
                if (!MaxCapePlugin.hasRequirements(player, true)) {
                    npc("I'm afraid you do not have the requirements for a", "Max cape.");
                    stage = END;
                    break;
                }
                if (!player.getInventory().contains(COST)) {
                    npc("I'm afraid you do not have enough coins for a", "Max cape.");
                    stage = END;
                    break;
                }
                if (player.getInventory().freeSlots() < 2) {
                    npc("You need at least 2 free inventory spaces for the", "Max cape and hood.");
                    stage = END;
                    break;
                }
                if (player.getInventory().remove(COST)) {
                    player.getInventory().add(MAX_ITEMS[0], true);
                    player.getInventory().add(MAX_ITEMS[1], true);
                    if (player.getAttribute("max_cape") == null) {
                        player.saveAttribute("max_cape", 1);
                        player.sendGlobalNewsMessage("A03939", "has just been awarded the Max cape!", 6);
                    }
                    interpreter.sendItemMessage(MAX_ITEMS[0], "Mac sells you a Max cape and hood for " + TextUtils.getFormattedNumber(COST.getCount()) + " coins!");
                    stage = END;
                    break;
                }
                break;
            case 13:
                npc("Once you have done that, I will sell you a Max cape", "for " + TextUtils.getFormattedNumber(COST.getCount()) + " coins.");
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
        return new int[]{ NPCDefinition.MAC };
    }


}
