package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

import plugin.npc.AutoSpawnNPC;

/**
 * Spawns a "squad" of NPCs.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class NPCSquadCommand extends Command {

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
        return new String[]{ "npcsquad" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("npcsquad", "Spawns a 'squad' of npcs in a 4x4 tile", getRights(),
            "::npcsquad <lt>npc_id> <lt>[ sizeX]> <lt>[ sizeY]>"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::npcsquad <lt>npc_id> <lt>[ sizeX]> <lt>[ sizeY]>");
            return;
        }
        int sizeX = 3;
        int sizeY = 3;
        int spawned = 0;
        if (args.length > 2) {
            sizeX = Integer.parseInt(args[2]);
            sizeY = args.length > 3 ? Integer.parseInt(args[3]) : sizeX;
        }
        boolean aggressive = args.length > 4;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                spawned++;
                Location sp = player.getLocation().transform(1 + x, 1 + y, 0);
                int newX = sp.getX();
                int newY = sp.getY();
                int newZ = sp.getZ();
                AutoSpawnNPC autoSpawnNPC = new AutoSpawnNPC(Integer.parseInt(args[1]),
                    Location.create(newX, newY, newZ), 0);
                autoSpawnNPC.init();
                if (aggressive) {
                    autoSpawnNPC.setWalkRadius(3);
                }
                autoSpawnNPC.setWalks(aggressive);
            }
        }
        player.getActionSender().sendMessage("Spawned " + spawned + " NPCs.");
    }
}
