package org.gielinor.game.system.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Lists NPCs in the area.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class NpcsInAreaCommand extends Command {

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
        return new String[]{ "npcsinarea" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("npcsinarea", "Gets a count of npcs in the radius given",
            getRights(), "::npcsinarea <lt>radius>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!(args.length >= 2)) {
            player.getActionSender().sendMessage("Use as ::npcsinarea <lt>radius>");
            return;
        }
        int radius = Integer.parseInt(args[1]);
        String npcName = args.length == 3 ? args[2] : "ALL_NPCS";
        List<NPC> npcsInArea = new ArrayList<>();
        for (NPC npc : Repository.getNpcs()) {
            if (player.getLocation().getZ() != npc.getLocation().getZ()) {
                continue;
            }
            if (player.getLocation().getDistance(npc.getLocation()) > radius) {
                continue;
            }
            if (!npcName.equals("ALL_NPCS")) {
                if (npc.getName() != null && !npc.getName().contains(npcName)) {
                    continue;
                }
            }
            npcsInArea.add(npc);
        }
        if (npcsInArea.isEmpty()) {
            player.getActionSender().sendMessage("No NPCs in the area.");
            return;
        }
        player.getActionSender().sendMessage(npcsInArea.size() + " npcs are in the area.");
        for (NPC npc : npcsInArea) {
            player.getActionSender()
                .sendConsoleMessage(npc.getId() + " : " + npc.getName() + ": " + npc.getLocation().toString());
        }
    }
}
