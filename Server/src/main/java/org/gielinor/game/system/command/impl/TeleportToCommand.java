package org.gielinor.game.system.command.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

public class TeleportToCommand extends Command {

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
        return new String[]{ "teleto", "teletonpc" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("teleto", "Teleports to the given player name", getRights(),
            "::teleto <lt>player_name>"));
        CommandDescription.add(new CommandDescription("teletonpc", "Teleports to the given NPC id", getRights(),
            "::teletonpc <lt>npc_id> <lt>[ index]><br>Index optional, the index of the spawned NPC to teleport to"));

    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.getActionSender()
                .sendMessage("Use as ::" + args[0] + " <lt>"
                    + (args[0].equalsIgnoreCase("teleto") ? "player name" : "npc id") + ">"
                    + (args[0].equalsIgnoreCase("teleto") ? "" : " <lt>[ index]>"));
            return;
        }
        switch (args[0].toLowerCase()) {
            case "teleto":
                String other = toString(args, 1);
                for (Player otherPlayer : Repository.getPlayers()) {
                    if (otherPlayer.getUsername().equalsIgnoreCase(other)) {
                        player.setTeleportTarget(otherPlayer.getLocation());
                        break;
                    }
                }
                break;
            case "teletonpc":
                if (!StringUtils.isNumeric(args[1])) {
                    player.getActionSender().sendMessage("The npc id must be numeric.");
                    break;
                }
                if (args.length > 2 && !StringUtils.isNumeric(args[2])) {
                    player.getActionSender().sendMessage("The index for the npc spawn must be numeric.");
                    break;
                }
                int index = args.length > 2 ? Integer.parseInt(args[2]) : 0;
                List<NPC> npcList = Repository.findNPCs(Integer.parseInt(args[1]));
                if (npcList.size() == 0) {
                    player.getActionSender().sendMessage("No NPCs could be found with the id of " + args[1] + ".");
                    break;
                }
                if (npcList.size() < index) {
                    player.getActionSender().sendMessage("NPC index too high, max = " + npcList.size() + ".");
                    break;
                }
                int currentIndex = 0;
                for (NPC npc : npcList) {
                    if (currentIndex == index) {
                        player.setTeleportTarget(npc.getLocation());
                        break;
                    }
                    currentIndex++;
                }
                break;
        }
    }
}
