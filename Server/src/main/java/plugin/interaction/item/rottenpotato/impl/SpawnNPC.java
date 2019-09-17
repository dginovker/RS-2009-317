package plugin.interaction.item.rottenpotato.impl;

import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RunScript;

import plugin.npc.AutoSpawnNPC;

/**
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SpawnNPC extends ActionClass {

    @Override
    public String getVariableName() {
        return "spawn_npc";
    }

    @Override
    public boolean execute(Player player) {
        player.getDialogueInterpreter().getDialogue().end();
        player.getDialogueInterpreter().sendInput(false, "Enter NPC ID:");
        player.setAttribute("runscript", new RunScript() {

            @Override
            public boolean handle() {
                int npcId = (int) getValue();
                NPC autoSpawnNPC = new AutoSpawnNPC(npcId, player.getLocation(), 6);
                autoSpawnNPC.setTeleportTarget(autoSpawnNPC.getLocation());
                autoSpawnNPC.setAttribute("RESPAWN_NPC", false);
                autoSpawnNPC.setRespawn(false);
                autoSpawnNPC.setWalks(false);
                autoSpawnNPC.setWalkRadius(0);
                autoSpawnNPC.init();
                return true;
            }
        });
        return true;
    }
}
