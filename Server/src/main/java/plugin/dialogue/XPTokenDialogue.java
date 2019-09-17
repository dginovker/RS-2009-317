package plugin.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.utilities.string.TextUtils;

/**
 * The dialogue plugin for claiming experience from an XP Token.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class XPTokenDialogue extends DialoguePlugin {

    /**
     * The token.
     */
    private Item token;

    /**
     * The selected {@link plugin.dialogue.XPTokenDialogue.RewardSkill}.
     */
    private RewardSkill rewardSkill;

    /**
     * The amount of experience to give.
     */
    private int experience;

    /**
     * The page skill index we're on.
     */
    private int skillIndex;

    /**
     * The max options for the skill page we're on.
     */
    private int maxOptions;

    /**
     * The {@link plugin.dialogue.XPTokenDialogue.RewardSkill}s.
     */
    private RewardSkill[] rewardSkills;

    /**
     * Constructs a new {@link XPTokenDialogue} {@link DialoguePlugin}.
     */
    public XPTokenDialogue() {
    }

    /**
     * Constructs a new {@link XPTokenDialogue} {@link DialoguePlugin}.
     *
     * @param player The player.
     */
    public XPTokenDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new XPTokenDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        token = (Item) args[0];
        if (!player.getInventory().contains(token) || token.getCharge() == 0) {
            end();
            return false;
        }
        if (!token.isCharged()) {
            end();
            player.getActionSender().sendMessage("Your XP token does not have any experience left.");
            return true;
        }
        stage = 0;
        return handleSelection(null);
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        if (!player.getInventory().contains(token)) {
            end();
            return false;
        }
        if (!token.isCharged()) {
            end();
            return false;
        }
        if (stage == 0 && handleSelection(optionSelect)) {
            return true;
        }
        switch (stage) {
            case 0:
                int selected = optionSelect.getIndex() + skillIndex;
                if (selected < 0) {
                    end();
                    return true;
                }
                rewardSkill = rewardSkills[optionSelect.getIndex() + skillIndex];
                interpreter.sendOptions("Select Experience Amount", "50,000", "100,000", "250,000", "500,000", "Different Skill");
                stage = 1;
                break;
            case 1:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        experience = 50000;
                        break;
                    case FIVE_OPTION_TWO:
                        experience = 100000;
                        break;
                    case FIVE_OPTION_THREE:
                        experience = 250000;
                        break;
                    case FIVE_OPTION_FOUR:
                        experience = 500000;
                        break;
                    case FIVE_OPTION_FIVE:
                        end();
                        player.getDialogueInterpreter().open("XPTokenDialogue", token);
                        return true;
                }
                int left = (token.getCharge() - (experience / 1000)) * 1000;
                left = left < 0 ? 0 : left;
                interpreter.sendPlaneMessage(false, "You are about to use " +
                    TextUtils.getFormattedNumber(experience) + " experience in the " +
                    TextUtils.formatDisplayName(rewardSkill.name()) +
                    " skill.", "You will have " + TextUtils.getFormattedNumber(left) +
                    " experience left to use.", "Are you sure?");
                stage = 2;
                break;
            case 2:
                options("Use " + TextUtils.getFormattedNumber(experience) + " experience in " +
                    TextUtils.formatDisplayName(rewardSkill.name()), "No");
                stage = 3;
                break;
            case 3:
                switch (optionSelect) {
                    case TWO_OPTION_ONE:
                        end();
                        if (!player.getInventory().contains(token) || token.getCharge() == 0) {
                            return false;
                        }
                        long amountLeft = token.getCharge() * 1000;
                        if (amountLeft < experience) {
                            player.getActionSender().sendMessage("You do not have enough experience in this token for that.");
                            end();
                            return true;
                        }
                        token.setCharge(token.getCharge() - (experience / 1000));
                        int exp = token.getCharge() * 1000;
                        player.getSkills().addExperienceNoMod(rewardSkill.getSkillId(), experience);
                        player.getActionSender().sendMessage("You have used " + TextUtils.getFormattedNumber((int) experience) + " in the " + TextUtils.formatDisplayName(rewardSkill.name()) + " skill.");
                        player.getActionSender().sendMessage("You have " + TextUtils.getFormattedNumber(exp) + " experience left to use.");
                        break;
                    case TWO_OPTION_TWO:
                        end();
                        player.getDialogueInterpreter().open("XPTokenDialogue", token);
                        break;
                }
                break;
            case END:
                end();
                break;
        }
        return true;
    }

    /**
     * Represents skills that can be used with a token.
     *
     * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
     */
    enum RewardSkill {
        ATTACK(Skills.ATTACK, 10942, 10943),
        DEFENCE(Skills.DEFENCE, 10942, 10943),
        STRENGTH(Skills.STRENGTH, 10942, 10943),
        HITPOINTS(Skills.HITPOINTS, 10942, 10943),
        RANGE(Skills.RANGE, 10942, 10943),
        PRAYER(Skills.PRAYER, 10942, 10943),
        MAGIC(Skills.MAGIC, 10942, 10943),
        COOKING(Skills.COOKING, 10942, 10944),
        WOODCUTTING(Skills.WOODCUTTING, 10942, 10944),
        FLETCHING(Skills.FLETCHING, 10942, 10944),
        FISHING(Skills.FISHING, 10942, 10944),
        FIREMAKING(Skills.FIREMAKING, 10942, 10944),
        CRAFTING(Skills.CRAFTING, 10942, 10944),
        SMITHING(Skills.SMITHING, 10942, 10944),
        MINING(Skills.MINING, 10942, 10944),
        HERBLORE(Skills.HERBLORE, 10942, 10944),
        AGILITY(Skills.AGILITY, 10942, 10944),
        THIEVING(Skills.THIEVING, 10942, 10944),
        SLAYER(Skills.SLAYER, 10942),
        FARMING(Skills.FARMING, 10942, 10944),
        RUNECRAFTING(Skills.RUNECRAFTING, 10942, 10944),
        HUNTER(Skills.HUNTER, 10942, 10944),;
        //CONSTRUCTION(Skills.CONSTRUCTION, 10942, 10944),
        //SUMMONING(Skills.SUMMONING, 10942, 10944),
        /**
         * The id of the skill.
         */
        private final int skillId;
        /**
         * The item ids.
         */
        private final int[] ids;

        /**
         * Constructs a new <code>RewardSkill</code>.
         *
         * @param skillId The id of the skill.
         * @param ids     The item ids.
         */
        RewardSkill(int skillId, int... ids) {
            this.skillId = skillId;
            this.ids = ids;
        }

        /**
         * Gets an array of possible skills for the given item id.
         *
         * @param itemId The id of the item.
         * @return The array.
         */
        public static RewardSkill[] getRewardSkills(int itemId) {
            List<RewardSkill> rewardSkills = new ArrayList<>();
            for (RewardSkill rewardSkill : values()) {
                for (int id : rewardSkill.getIds()) {
                    if (id == itemId) {
                        rewardSkills.add(rewardSkill);
                    }
                }
            }
            return rewardSkills.toArray(new RewardSkill[rewardSkills.size()]);
        }

        /**
         * Gets the id of the skill.
         *
         * @return The id.
         */
        public int getSkillId() {
            return skillId;
        }

        /**
         * Gets the item ids.
         *
         * @return The ids.
         */
        public int[] getIds() {
            return ids;
        }
    }

    /**
     * Handles page / skill selection.
     *
     * @param optionSelect The {@link org.gielinor.game.content.dialogue.OptionSelect}.
     * @return <code>True</code> if handled.
     */
    public boolean handleSelection(OptionSelect optionSelect) {
        if (optionSelect == null) {
            rewardSkills = RewardSkill.getRewardSkills(token.getId());
            List<String> options = new ArrayList<>();
            maxOptions = rewardSkills.length > 4 ? 4 : rewardSkills.length;
            int currentOptions = 0;
            for (int index = 0; index < 5; index++) {
                if ((index + skillIndex) >= rewardSkills.length) {
                    maxOptions = index + skillIndex;
                    break;
                }
                options.add(TextUtils.formatDisplayName(rewardSkills[index + skillIndex].name()));
                currentOptions++;
            }
            String[] sendOptions;
            if (maxOptions == 4) {
                sendOptions = options.toArray(new String[5]);
                sendOptions[4] = "More...";
            } else {
                sendOptions = options.toArray(new String[currentOptions + 1]);
                sendOptions[currentOptions] = "More...";
                skillIndex = -1;
                maxOptions = currentOptions;
            }
            options(sendOptions);
            return true;
        }
        if (optionSelect.getIndex() == maxOptions) {
            if (skillIndex != rewardSkills.length - maxOptions) {
                skillIndex += maxOptions;
            } else {
                skillIndex = 0;
            }
            List<String> options = new ArrayList<>();
            maxOptions = rewardSkills.length > 4 ? 4 : rewardSkills.length;
            int currentOptions = 0;
            for (int index = 0; index < 5; index++) {
                if ((index + skillIndex) >= rewardSkills.length) {
                    maxOptions = index + skillIndex;
                    break;
                }
                options.add(TextUtils.formatDisplayName(rewardSkills[index + skillIndex].name()));
                currentOptions++;
            }
            String[] sendOptions;
            if (maxOptions == 4) {
                sendOptions = options.toArray(new String[5]);
                sendOptions[4] = "More...";
            } else {
                sendOptions = options.toArray(new String[currentOptions + 1]);
                sendOptions[currentOptions] = "More...";
                skillIndex = rewardSkills.length - currentOptions;
                maxOptions = currentOptions;
            }
            options(sendOptions);
            stage = 0;
            return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ DialogueInterpreter.getDialogueKey("XPTokenDialogue") };
    }
}
