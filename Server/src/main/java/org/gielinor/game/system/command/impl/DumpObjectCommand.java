package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;

/**
 * Dumps specific objects following a pattern.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DumpObjectCommand extends Command {

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
        return new String[]{ "dumpobjects" };
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length >= 2 && args.length < 4) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder results = new StringBuilder();
                boolean contain = false;
                for (int i = 1; i < args.length; i++) {
                    stringBuilder.append(args[i]);
                    if (i != args.length - 1) {
                        stringBuilder.append(" ");
                    }
                }
                String[] arguments = stringBuilder.toString().split("-");
                String name = arguments[0];
                if (name.equalsIgnoreCase("no_value")) {
                    name = null;
                }
                String action = null;
                if (arguments.length == 2) {
                    action = arguments[1].toLowerCase();
                    if (action.endsWith("_contains")) {
                        action = action.replace("_contains", "");
                        contain = true;
                    }
                }
                if (name == null) {
                    player.getActionSender()
                        .sendConsoleMessage("Dumping objects with action <col=8A0808>" + action + "</col>");
                } else {
                    player.getActionSender().sendConsoleMessage("Dumping objects by name of <col=8A0808>" + name
                        + "</col>" + (action == null ? "" : " with action <col=8A0808>" + action + "</col>") + ".");
                }
                for (ObjectDefinition objectDefinition : ObjectDefinition.getDefinitions().values()) {
                    boolean skip = true;
                    if (name != null) {
                        if (!objectDefinition.getName().toLowerCase().contains(name.toLowerCase())) {
                            continue;
                        }
                    }
                    if (action != null && !objectDefinition.hasActions()) {
                        continue;
                    }
                    if (action != null) {
                        for (String a : objectDefinition.getOptions()) {
                            if (a == null) {
                                continue;
                            }
                            a = a.toLowerCase();
                            if ((contain ? (a.contains(action)) : (a.equalsIgnoreCase(action)))) {
                                skip = false;
                                break;
                            }
                        }
                        if (skip) {
                            continue;
                        }
                    }
                    results.append(objectDefinition.getId()).append(", ");
                }
                System.out.println(results.toString());
            } catch (Exception e) {
                e.printStackTrace();
                player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>object name>-<lt> [action]>");
            }
        }
    }
}
