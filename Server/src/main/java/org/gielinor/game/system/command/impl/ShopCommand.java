package org.gielinor.game.system.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.content.global.shop.Shops;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Opens a shop by the given ordinal.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ShopCommand extends Command {

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
        return new String[]{ "shop" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("shop", "Opens a shop by id", getRights(), "::shop <lt>id>"));
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.getActionSender().sendMessage("Use as ::shop <lt>shop id> or <lt>shop name>");
            return;
        }
        if (!StringUtils.isNumeric(args[1])) {
            String shopName = toString(args, 1).replaceAll(" ", "_").toUpperCase();
            for (Shops shop : Shops.values()) {
                if (shop.name().equals(shopName)) {
                    player.getInterfaceState().close();
                    shop.open(player);
                    return;
                }
            }
            player.getActionSender().sendMessage("Shop does not exist.");
            return;
        }
        int shopId = Integer.parseInt(args[1]);
        for (Shops shop : Shops.values()) {
            if (shop.ordinal() == shopId) {
                player.getInterfaceState().close();
                shop.open(player);
                return;
            }
        }
        player.getActionSender().sendMessage("Shop does not exist.");
    }
}
