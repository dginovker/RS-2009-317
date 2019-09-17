package plugin.interaction.object;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.combat.equipment.ArmourSet;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the dummy attack plugin.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class DummyAttackPlugin extends OptionHandler {

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(823).getConfigurations().put("option:attack", this);
        ObjectDefinition.forId(2038).getConfigurations().put("option:hit", this);
        NPCDefinition.forId(NPCDefinition.MAX_HIT_DUMMY).getConfigurations().put("option:hit", this); // todo
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        if (node instanceof NPC && option.equals("hit")) { // Max hit dummy
            if (player.sendDonorNotification("to use this dummy.")) {
                return true;
            }
            player.lock(3);
            NPC victim = (NPC) node;
            victim.getSkills().setLifepoints(65000);
            CombatSwingHandler combatSwingHandler = player.getSwingHandler(true);
            final BattleState battleState = new BattleState(player, victim);
            ArmourSet set = combatSwingHandler.getArmourSet(player);
            player.getProperties().setArmourSet(set);
            int delay = combatSwingHandler.swing(player, victim, battleState);
            if (delay < 0) {
                return false;
            }
            battleState.setEstimatedHit(battleState.getMaximumHit());
            combatSwingHandler.adjustBattleState(player, victim, battleState);
            combatSwingHandler.visualize(player, victim, battleState);
            if (delay - 1 < 1) {
                combatSwingHandler.visualizeImpact(player, victim, battleState);
            }
            combatSwingHandler.visualizeAudio(player, victim, battleState);
            if (set != null && set.effect(player, victim, battleState)) {
                set.visualize(player, victim);
            }
            World.submit(new Pulse(delay - 1, player, victim) {

                @Override
                public boolean pulse() {
                    victim.getImpactHandler().manualHit(player,
                        battleState.getMaximumHit(),
                        ImpactHandler.HitsplatType.NORMAL);
                    victim.getSkills().setLifepoints(65000);
                    return true;
                }
            });
            return true;
        }
        player.animate(player.getProperties().getAttackAnimation());
        if (player.getProperties().getCurrentCombatLevel() < 8) {
            final Player p = player;
            double experience = 5;
            switch (p.getProperties().getAttackStyle().getStyle()) {
                case WeaponInterface.STYLE_ACCURATE:
                    p.getSkills().addExperience(Skills.ATTACK, experience);
                    break;
                case WeaponInterface.STYLE_AGGRESSIVE://strength.
                    p.getSkills().addExperience(Skills.STRENGTH, experience);
                    break;
                case WeaponInterface.STYLE_DEFENSIVE://defence.
                    p.getSkills().addExperience(Skills.DEFENCE, experience);
                    break;
                case WeaponInterface.STYLE_CONTROLLED://shared.
                    experience /= 3;
                    p.getSkills().addExperience(Skills.ATTACK, experience);
                    p.getSkills().addExperience(Skills.STRENGTH, experience);
                    p.getSkills().addExperience(Skills.DEFENCE, experience);
                    break;
            }
        } else {
            player.getActionSender().sendMessage("You swing at the dummy...");
            player.getActionSender().sendMessage("There is nothing more you can learn from hitting a dummy.");
        }
        return true;
    }

}
