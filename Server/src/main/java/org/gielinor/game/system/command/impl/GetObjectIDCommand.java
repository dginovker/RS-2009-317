package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gets an object ID by the object name.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GetObjectIDCommand extends Command {

    private static final Logger log = LoggerFactory.getLogger(GetObjectIDCommand.class);

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
        return new String[]{ "getobjectid", "getobjid" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("getobjectid", "Gets the id of an object by name", getRights(),
            "::getobjectid <lt>object_name><br>Example:<br>::getobjectid ancient altar"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length >= 2) {
            try {
                int resultCount = 0;
                String search = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                if (search.isEmpty()) {
                    player.getActionSender().sendMessage("Use as ::getobjectid <lt>object name>");
                    return;
                }
                boolean dumpCmd = search.contains("_dump");
                if (dumpCmd) {
                    search = search.replace("_dump", "");
                }
                String res = "";
                List<Integer> results = new ArrayList<>();
                for (ObjectDefinition objectDefinition : ObjectDefinition.getDefinitions().values()) {
                    if (objectDefinition != null
                        && objectDefinition.getName().toLowerCase().contains(search.toLowerCase())) {
                        String objectName = objectDefinition.getName();
                        res += ("<shad=1><col=FF9040>" + objectName + ": " + objectDefinition.getId() + "</col>\n");
                        if (dumpCmd) {
                            results.add(objectDefinition.getId());
                        }
                        resultCount += 1;
                    }
                }
                if (resultCount > 30) {
                    player.getActionSender().sendMessage("Too many results, please refine search.");
                }
                player.getActionSender().sendMessage(resultCount + " objects found for \"<col=306530>" + search + "</col>\"");
                if (resultCount == 0) {
                    return;
                }
                String[] finalResults = res.split("\n");
                for (String message : finalResults) {
                    player.getActionSender().sendMessage(message);
                }
                if (dumpCmd) {
                    log.info("[{}] requested object [{}] - found: {}.",
                        player.getName(), toString(args, 1), results);
                }
            } catch (Exception ex) {
                player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>object name>");
            }
        }
    }

}
