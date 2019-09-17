package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the experience lamp interface.
 *
 * @author 'Vexia
 * @version 1.0
 *          TODO 317
 */
public final class ExperienceLampInterface extends ComponentPlugin {

    /**
     * Represents the sound to send.
     */
    private static final Audio AUDIO = new Audio(1270, 12, 1);

    /**
     * Represents the lamp item.
     */
    private static final Item LAMP = new Item(2528);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(2808, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        if (component.getId() != 2808) {
            return false;
        }
        OptionSelect optionSelect = OptionSelect.forInterfaceId(button);
        if (optionSelect == null) {
            player.getActionSender().sendMessage("Something went wrong... Please report this on the forums (1): " + button);
            return true;
        }
        int level = player.getAttribute("ANTIQUE_LAMP_LEVEL", 5);
        int lampId = player.getAttribute("ANTIQUE_LAMP_ID", 2528);
        Lamp lamp = null;
        if (optionSelect != OptionSelect.EXPERIENCE_LAMP_CONFIRM) {
            for (Lamp l : Lamp.values()) {
                if (l.optionSelect == optionSelect) {
                    lamp = l;
                    break;
                }
            }
            player.setAttribute("lamp:experience", lamp);
        } else {
            if (!player.getAttributes().containsKey("lamp:experience")) {
                player.getActionSender().sendMessage("You need to pick the skill you wish to gain the experience in first.");
                return true;
            }
            lamp = player.getAttribute("lamp:experience");
        }
        if (lamp == null) {
            player.getActionSender().sendMessage("Something went wrong... Please report this on the forums (2): " + button);
            return true;
        }
        player.getConfigManager().set(261, lamp.ordinal() + 1, false);
        if (optionSelect == OptionSelect.EXPERIENCE_LAMP_CONFIRM) {
            if (!player.getAttributes().containsKey("lamp:experience")) {
                player.getActionSender().sendMessage("You need to pick the skill you wish to gain the experience in first.");
                return true;
            }
            if (player.getSkills().getStaticLevel(lamp.skill) < level) {
                player.getConfigManager().set(261, 0, false);
                player.getActionSender().sendMessage("You need to have a level of at least " + level + " to gain experience in that skill.");
                return true;
            }
            if (!player.getInventory().contains(lampId, 1)) {
                player.removeAttribute("ANTIQUE_LAMP_LEVEL");
                player.removeAttribute("ANTIQUE_LAMP_EXP");
                player.removeAttribute("ANTIQUE_LAMP_ID");
                return true;
            }
            player.getActionSender().sendSound(AUDIO);
            player.getInventory().remove(new Item(lampId, 1));
            player.getInterfaceState().close();
            int x = player.getSkills().getLevel(lamp.skill);
            int experience = x * 10;
            if (player.getAttribute("ANTIQUE_LAMP_EXP", -1) != -1) {
                experience = player.getAttribute("ANTIQUE_LAMP_EXP");
            }
            player.removeAttribute("ANTIQUE_LAMP_LEVEL");
            player.removeAttribute("ANTIQUE_LAMP_EXP");
            player.removeAttribute("ANTIQUE_LAMP_ID");
            player.getSkills().addExperienceNoMod(lamp.skill, experience);
            player.getDialogueInterpreter().open(70099, "The lamp gives you " + experience + " " + Skills.SKILL_NAME[lamp.skill] + " experience.");
            return true;
        }
        return false;
    }

    /**
     * Representa a skill to gain experience.
     *
     * @author 'Vexia
     */
    public enum Lamp {
        ATTACK(OptionSelect.EXPERIENCE_LAMP_ATTACK, Skills.ATTACK, 1),
        STRENGTH(OptionSelect.EXPERIENCE_LAMP_STRENGTH, Skills.STRENGTH, 2),
        RANGED(OptionSelect.EXPERIENCE_LAMP_RANGED, Skills.RANGE, 3),
        MAGIC(OptionSelect.EXPERIENCE_LAMP_MAGIC, Skills.MAGIC, 4),
        DEFENCE(OptionSelect.EXPERIENCE_LAMP_DEFENCE, Skills.DEFENCE, 5),
        HITPOINTS(OptionSelect.EXPERIENCE_LAMP_HITPOINTS, Skills.HITPOINTS, 6),
        PRAYER(OptionSelect.EXPERIENCE_LAMP_PRAYER, Skills.PRAYER, 7),
        AGILITY(OptionSelect.EXPERIENCE_LAMP_AGILITY, Skills.AGILITY, 8),
        HERBLORE(OptionSelect.EXPERIENCE_LAMP_HERBLORE, Skills.HERBLORE, 9),
        THIEVING(OptionSelect.EXPERIENCE_LAMP_THIEVING, Skills.THIEVING, 10),
        CRAFTING(OptionSelect.EXPERIENCE_LAMP_CRAFTING, Skills.CRAFTING, 11),
        RUNECRAFT(OptionSelect.EXPERIENCE_LAMP_RUNECRAFT, Skills.RUNECRAFTING, 12),
        MINING(OptionSelect.EXPERIENCE_LAMP_MINING, Skills.MINING, 13),
        SMITHING(OptionSelect.EXPERIENCE_LAMP_SMITHING, Skills.SMITHING, 14),
        FISHING(OptionSelect.EXPERIENCE_LAMP_FISHING, Skills.FISHING, 15),
        COOKING(OptionSelect.EXPERIENCE_LAMP_COOKING, Skills.COOKING, 16),
        FIREMAKING(OptionSelect.EXPERIENCE_LAMP_FIREMAKING, Skills.FIREMAKING, 17),
        WOODCUTTING(OptionSelect.EXPERIENCE_LAMP_WOODCUTTING, Skills.WOODCUTTING, 18),
        FLETCHING(OptionSelect.EXPERIENCE_LAMP_FLETCHING, Skills.FLETCHING, 19),
        SLAYER(OptionSelect.EXPERIENCE_LAMP_SLAYER, Skills.SLAYER, 20),
        FARMING(OptionSelect.EXPERIENCE_LAMP_FARMING, Skills.FARMING, 21),;


        /**
         * Constructs a new {@code ExperienceLampInterface} {@code Object}.
         *
         * @param optionSelect The {@link org.gielinor.game.content.dialogue.OptionSelect}.
         * @param skill        the skill.
         */
        Lamp(OptionSelect optionSelect,
            int skill, int configId
        )

        {
            this.optionSelect = optionSelect;
            this.skill = skill;
        }

        /**
         * The {@link org.gielinor.game.content.dialogue.OptionSelect}.
         */
        private final OptionSelect optionSelect;

        /**
         * The <b>Skill</b> id.
         */
        private final int skill;

        /**
         * The config id.
         */
        private int configId;
    }
}
