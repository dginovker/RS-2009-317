package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.SkillMenu;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the plugin used for the Skill tab interface.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SkillTabInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(3917, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int buttonId, int slot, int itemId) {
        final SkillConfig config = SkillConfig.forId(buttonId);
        if (config == null) {
            return false;
        }
        int skillId = -1;
        int index = 0;
        for (String skillName : Skills.SKILL_NAME) {
            if (skillName.toLowerCase().equals(config.name().toLowerCase())) {
                skillId = index;
                break;
            }
            index++;
        }
        if (skillId == -1) {
            player.getActionSender().sendMessage("Something went wrong, please report on forums : " + opcode + "," + buttonId + "," + slot);
            return true;
        }
        SkillMenu skillMenu = SkillMenu.forId(skillId);
        if (skillMenu == null) {
            player.getActionSender().sendMessage("Coming soon.");
            return true;
        }
        return skillMenu.open(player, 0, true);
    }

    public enum SkillConfig {
        ATTACK(24610),
        DEFENCE(24616),
        STRENGTH(24613),
        HITPOINTS(24611),
        RANGED(24619),
        PRAYER(24622),
        MAGIC(24625),
        COOKING(24621),
        WOODCUTTING(24627),
        FLETCHING(24626),
        FISHING(24618),
        FIREMAKING(24624),
        CRAFTING(24623),
        SMITHING(24615),
        MINING(24612),
        HERBLORE(24617),
        AGILITY(24614),
        THIEVING(24620),
        SLAYER(24629),
        FARMING(24630),
        RUNECRAFTING(24628),
        CONSTRUCTION(24631),
        HUNTER(24632),
        SUMMONING(24633);

        /**
         * The id of the button.
         */
        private final int buttonId;

        /**
         * Constructs a new {@code SkillConfig}.
         *
         * @param buttonId The id of the button.
         */
        SkillConfig(int buttonId) {
            this.buttonId = buttonId;
        }

        /**
         * Gets a {@link plugin.interaction.inter.SkillTabInterface.SkillConfig} by the id of the button.
         *
         * @param buttonId The id of the button clicked.
         * @return The {@code SkillConfig}.
         */
        public static SkillConfig forId(int buttonId) {
            for (SkillConfig skillConfig : SkillConfig.values()) {
                if (skillConfig.buttonId == buttonId) {
                    return skillConfig;
                }
            }
            return null;
        }

        /**
         * Gets the id of the button.
         *
         * @return The id.
         */
        public int getButtonId() {
            return buttonId;
        }
    }
}