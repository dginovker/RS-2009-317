package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.NPCDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Gets an npc ID by npc name.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GetNpcIdCommand extends Command {

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
        return new String[]{ "getnpcid" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("getnpcid", "Gets the id of an NPC", getRights(),
            "::getnpcid <lt>npc_id><br>Example<br>::getnpcid duradel"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length >= 2) {
            try {
                int results = 0;
                String res = "";
                String cmdRes = "";
                String search = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        sb.append(" ");
                    }
                }
                if (sb.toString().isEmpty()) {
                    p.getActionSender().sendConsoleMessage("Use as ::getnpcid <lt>npc name>");
                    return;
                }
                search = sb.toString();
                if (search.contains("option:")) {

                }
                for (NPCDefinition npcDefinition : NPCDefinition.getDefinitions().values()) {
                    if (search.contains("option:")) {
                        String opt = search.replace("option:", "");
                        if (npcDefinition != null && npcDefinition.hasAction(opt)) {
                            String npcName = npcDefinition.getName();
                            res += ("<shad=1><col=FF9040>" + npcName + "(" + npcDefinition.getCombatLevel() + "): "
                                + npcDefinition.getId() + "</col>\n");
                            cmdRes += npcDefinition.getId() + ", ";
                            results += 1;
                        }
                        continue;
                    }
                    if (npcDefinition != null && npcDefinition.getName().toLowerCase().contains(search.toLowerCase())) {
                        String npcName = npcDefinition.getName();
                        res += ("<shad=1><col=FF9040>" + npcName + "(" + npcDefinition.getCombatLevel() + "): "
                            + npcDefinition.getId() + "</col>\n");
                        cmdRes += npcDefinition.getId() + ", ";
                        results += 1;
                    }
                }
                p.getActionSender()
                    .sendConsoleMessage(results + " npcs found for \"<col=306530>" + search + "</col>\"");
                if (results == 0) {
                    return;
                }
                String[] finalResults = res.split("\n");
                for (String message : finalResults) {
                    p.getActionSender().sendConsoleMessage(message);
                }
                System.out.println(cmdRes);
            } catch (Exception e) {
                p.getActionSender().sendConsoleMessage("Use as ::getnpcid <lt>npc name>");
            }
        } else {
            p.getActionSender().sendConsoleMessage("Use as ::getnpcid <lt>npc name>");
        }
    }

}
