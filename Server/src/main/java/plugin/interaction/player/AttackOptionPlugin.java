package plugin.interaction.player;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Task;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Ironman;
import org.gielinor.rs2.config.ServerVar;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the attack option plugin handler.
 *
 * @author Emperor
 * @version 1.0
 */
public final class AttackOptionPlugin extends OptionHandler {

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node instanceof NPC) {
            NPC npc = ((NPC) node);
            if (player.getAttribute("REMOVE_NPCS", 0) == 1) {
                npc.clear();
                return true;
            }
            Task task = npc.getTask();
            if (task != null && task.getLevel() > player.getSkills().getStaticLevel(Skills.SLAYER)) {
                player.getActionSender().sendMessage("You need a Slayer level of " + task.getLevel() + " to attack this creature.");
                return true;
            }
            if (npc.getDefinition().getName().toLowerCase().contains("nechry") && 80 > player.getSkills().getStaticLevel(Skills.SLAYER)) {
                player.getActionSender().sendMessage("You need a Slayer level of 80 to attack this creature.");
                return true;
            }
            npc.getWalkingQueue().reset();
        }
        if (ServerVar.fetch("pk_disabled", 0) == 1) {
            player.getDialogueInterpreter().sendPlaneMessage("Player killing has been disabled.");
            return true;
        }
        if ((node instanceof Player) && !Ironman.canAttack(player, (Player) node)) {
            return true;
        }
        player.attack(node);
        return true;
    }

    @Override
    public boolean isWalk() {
        return false;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Option._P_ATTACK.setHandler(this);
        NPCDefinition.setOptionHandler("attack", this);
        return this;
    }

    @Override
    public boolean isDelayed(Player player) {
        return false;
    }

}
