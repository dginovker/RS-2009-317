package plugin.skill.thieving;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.thieving.Pickpocket;
import org.gielinor.game.content.skill.member.thieving.PickpocketPulse;
import org.gielinor.game.content.skill.member.thieving.Stall;
import org.gielinor.game.content.skill.member.thieving.StallThiefPulse;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Represents the plugin used to handle thieving options.
 *
 * @author 'Vexia
 * @date 22/10/2013
 */
public class ThievingOptionPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        NPCDefinition.setOptionHandler("pick-pocket", this);
        NPCDefinition.setOptionHandler("pickpocket", this);
        ObjectDefinition.setOptionHandler("steal-from", this);
        ObjectDefinition.setOptionHandler("steal from", this);
        ObjectDefinition.setOptionHandler("pick-lock", this);
        NPCDefinition.forId(2082).getConfigurations().put("option:pickpocket", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        switch (option) {
            case "pick-pocket":
            case "pickpocket":
                if (player.getLocation().getDistance(node.getLocation()) <= 2 && ((NPC) node).getPulseManager().getCurrent() instanceof MovementPulse) {
                    ((NPC) node).getPulseManager().getCurrent().stop();
                }
                player.getPulseManager().run(new PickpocketPulse(player, (NPC) node, Pickpocket.forNPC((NPC) node)));
                break;
            case "steal-from":
            case "steal from":
                player.getPulseManager().run(new StallThiefPulse(player, (GameObject) node, Stall.forObject((GameObject) node)));
                break;
            case "pick-lock":
                break;
        }
        return true;
    }

}
