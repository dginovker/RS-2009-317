package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Sets an attribute.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SetAttributeCommand extends Command {

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
        return new String[]{ "setattr", "setattribute" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("setattr", "Sets an attribute on your account", getRights(),
            "::setattr <lt>attribute_name> <lt>attribute_value><br>Example:<br>::setattr EASY_CLUE 0"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args.length != 3) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>attribute_name> <lt>attribute_value>");
            return;
        }
        String attributeName = args[1];
        String attributeValue = args[2];
        if (attributeValue.toLowerCase().equalsIgnoreCase("true")
            || attributeValue.toLowerCase().equalsIgnoreCase("false")) {
            player.setAttribute(attributeName, Boolean.valueOf(attributeValue.toLowerCase()));
        } else if (StringUtils.isNumeric(attributeValue)) {
            player.setAttribute(attributeName, Integer.parseInt(args[2]));
        } else {
            if (args[2].equalsIgnoreCase("0d")) {
                player.setAttribute(attributeName, 0D);
            } else if (args[2].equalsIgnoreCase("0l")) {
                player.setAttribute(attributeName, 0L);
            } else {
                player.setAttribute(attributeName, args[2]);
            }
        }
        player.getActionSender().sendMessage("Set attribute " + attributeName + " value to " + attributeValue + ".");
    }
}
