package org.gielinor.game.system.command.impl;

import org.gielinor.cache.def.impl.ConfigFileDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Resets a player's Farming.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ResetFarmingCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "resetfarm", "resetfarming" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("resetfarm", "Resets ALL of your Farming patches.", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        for (PatchWrapper patchWrapper : player.getFarmingManager().getPatches()) {
            if (patchWrapper == null) {
                continue;
            }
            player.getConfigManager().set(patchWrapper.getConfigId(), 0, true);
            patchWrapper.getCycle().clear(player);
        }
        for (ObjectDefinition objectDefinition : ObjectDefinition.getDefinitions().values()) {
            if (objectDefinition.getName() == null || !objectDefinition.getName().contains("patch") || objectDefinition.hasAction("pick") || objectDefinition.hasAction("inspect")) {
                continue;
            }
            if (objectDefinition.getConfigFileId() < 1) {
                continue;
            }
            ConfigFileDefinition configFileDefinition = ConfigFileDefinition.forId(objectDefinition.getId());
            if (configFileDefinition == null) {
                continue;
            }
            player.getConfigManager().set(configFileDefinition.getConfigId(), 0, true);
        }
        for (PatchWrapper patchWrapper : player.getFarmingManager().getPatches()) {
            if (patchWrapper == null) {
                continue;
            }
            player.getConfigManager().set(patchWrapper.getConfigId(), 0, true);
            patchWrapper.getCycle().clear(player);
        }
        player.getActionSender().sendMessage("All of your Farming patches have been reset.");
    }
}
