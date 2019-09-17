package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.combat.ImpactHandler;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Kills NPCs in the given radius.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class KillNPCsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "killnpcs" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("killnpcs", "Kills all npcs in given radius", getRights(),
            "::killnpcs <lt>radius>"));
    }

    @Override
    public void execute(final Player player, final String[] args) {
        if (args.length == 2) {
            int radius = Integer.parseInt(args[1]);
            for (NPC npc : Repository.getNpcs()) {
                if (npc == null) {
                    continue;
                }
                if (!npc.getDefinition().hasAttackOption()) {
                    continue;
                }
                if (npc.getLocation().getDistance(player.getLocation()) < radius) {
                    npc.getImpactHandler().manualHit(player, npc.getSkills().getLifepoints(),
                        ImpactHandler.HitsplatType.NORMAL);
                    npc.playAnimation(npc.getProperties().getDefenceAnimation());
                }
            }
        } else {
            player.getActionSender().sendMessage("Use as ::killnpcs <lt>radius>");
        }
    }
}
