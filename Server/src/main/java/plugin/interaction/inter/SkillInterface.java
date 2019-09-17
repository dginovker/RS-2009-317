package plugin.interaction.inter;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.SkillMenu;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the skill menu interface plugin.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class SkillInterface extends ComponentPlugin {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ComponentDefinition.put(50000, this);
        return this;
    }

    @Override
    public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
        SkillMenu skillMenu = player.getAttribute("SKILL_MENU");
        if (skillMenu == null) {
            SkillMenu.forId(0).open(player, 0, true);
            return true;
        }
        skillMenu.open(player, button - 50005, false);
        return true;
    }
}