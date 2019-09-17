package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.entity.player.info.title.LoyaltyTitleManagement;
import org.gielinor.game.node.entity.player.info.title.TitleCategory;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.repository.Repository;

/**
 * Changes the player's title ID.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TitleCommand extends Command {

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
        return new String[]{ "title", "titles", "fixtitles" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("title", "Sets title to the given id", getRights(),
            "::title <lt>id><br>ID can be \"reset\"<br>IDs can be found at ::titles"));
        CommandDescription.add(new CommandDescription("titles", "Displays possible title ids", getRights(), null));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args[0].toLowerCase().equalsIgnoreCase("fixtitles")) {
            // TODO This is due to my mess up!
            String[][] titleFix = new String[][]{ { "allah:80" }, { "ashley:80,13" }, { "auschwitz:72,75,19,80" },
                { "clutch0603:80" }, { "coke:2" }, { "dylan:80" }, { "empire:80" },
                { "esseker:80,27,13,81,78,79,75,76,77,74,73,72,71,70" }, { "Gielinor:80,80,81,1" },
                { "greeds envy:80" }, { "grime kid:80" }, { "its walmart:80" },
                { "jared:71,70,72,73,74,75,76,77,78,79,26,80,22" }, { "leaf v2:80" }, { "leaf:80" },
                { "naked taco:80" }, { "shrek:80" }, { "victory:17,74,70,72,71,79,78,77,76,75,73,80" },
                { "yes:80" } };
            String playerName = toString(args, 1).toLowerCase();
            Player otherPlayer = Repository.getPlayerByName(playerName);
            if (otherPlayer == null) {
                player.getActionSender().sendMessage("Player " + playerName + " could not be found.");
                return;
            }
            for (String[] td : titleFix) {
                for (String t : td) {
                    String[] data = t.split(":");
                    if (data[0].equalsIgnoreCase(playerName.replaceAll("_", " "))) {
                        for (String id : data[1].split(",")) {
                            otherPlayer.getTitleManager().getUnlockedTitles()
                                .add(LoyaltyTitle.forId(Integer.parseInt(id)));
                        }
                    }
                }
            }
            player.getActionSender().sendMessage("Fixed titles for " + otherPlayer.getUsername());
            return;
        }
        if (args[0].toLowerCase().startsWith("titles")) {
            player.setAttribute("TITLE_CATEGORY", TitleCategory.REGULAR);
            LoyaltyTitleManagement.openPage(player, player.getAttribute("TITLES_PAGE", 0), true);
            return;
        }
        if (args.length != 2 || !StringUtils.isNumeric(args[1])) {
            if (args.length == 2 && args[1].toLowerCase().equalsIgnoreCase("reset")) {
                player.getTitleManager().setLoyaltyTitle(null);
                player.getAppearance().sync();
                return;
            }
            player.getActionSender().sendMessage("Use as ::title <lt>title id>");
            return;
        }
        LoyaltyTitle loyaltyTitle = null;
        for (LoyaltyTitle t : LoyaltyTitle.values()) {
            if (t.ordinal() == Integer.parseInt(args[1])) {
                loyaltyTitle = t;
                break;
            }
        }
        if (loyaltyTitle == null) {
            player.getActionSender().sendMessage("Invalid loyalty title id.");
            return;
        }
        player.getTitleManager().setLoyaltyTitle(loyaltyTitle);
        player.getAppearance().sync();
    }
}
