package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.rs2.model.container.Container;
import plugin.activity.barrow.BarrowsLootGenerator;

public class BarrowsRewardChestCommand extends Command {

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
        return new String[]{ "brc" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("brc", "Gives a reward from the reward chest", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        boolean[] barrowsKilled = { true, true, true, true, true, true };
        BarrowsLootGenerator lootGenerator = new BarrowsLootGenerator(player);
        Container loot = lootGenerator.generateLoot(barrowsKilled, 1000, true);
        player.getInventory().clear();
        player.getInventory().add(Item.ROTTEN_POTATO);

        for (int i = 0; i < loot.itemCount(); i++) {
            Item item = loot.get(i);
            if (item == null) continue;
            player.getInventory().add(item, player);
        }
    }

}
