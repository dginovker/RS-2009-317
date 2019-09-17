package plugin.interaction.object;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.free.crafting.spinning.Spinnable;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the spinning wheel interface.
 *
 * @author <a href="http://Gielinor.org/">Gielinor</a>
 */
public class SpinSpinningWheelPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        player.getInterfaceState().open(new Component(22653));
        for (Spinnable spinnable : Spinnable.values()) {
            if (spinnable.getTextIds() != null) {
                for (int textId : spinnable.getTextIds()) {
                    player.getActionSender().sendInterfaceColour(textId,
                        (player.getSkills().getLevel(Skills.CRAFTING) >= spinnable.getLevel()) ? 0x00CC00 :
                            0xFF981F, true);
                }
            }
        }
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(2644).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(4309).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(8748).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(14889).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(20365).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(21304).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(25824).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(26143).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(34497).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(36970).getConfigurations().put("option:spin", this);
        ObjectDefinition.forId(37476).getConfigurations().put("option:spin", this);
        return this;
    }

    @Override
    public Location getDestination(Node node, Node n) {
        return null;
    }

}
