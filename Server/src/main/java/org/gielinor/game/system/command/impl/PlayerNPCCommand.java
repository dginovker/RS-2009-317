package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Transforms the player into an {@link NPC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PlayerNPCCommand extends Command {

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
        return new String[]{ "pnpc", "unpc", "unnpc", "pitem", "unitem", "pobj", "pobject", "unobj", "unobject" };
    }

    @Override
    public void init() {
        CommandDescription
            .add(new CommandDescription("pnpc", "Transform into npc by id", getRights(), "::pnpc <lt>npc_id>"));
        CommandDescription.add(new CommandDescription("unpc", "Transform back into player after npc transformation",
            getRights(), null));

    }

    @Override
    public void execute(final Player player, String[] args) {
        switch (args[0]) {
            case "pnpc":
                player.getAppearance().transformNPC(Integer.parseInt(args[1]));
                break;
            case "pitem":
                player.getAppearance().transformItem(Integer.parseInt(args[1]));
                break;
            case "pobj":
            case "pobject":
                player.getAppearance().transformObject(Integer.parseInt(args[1]));
                break;
            default:
                player.getAppearance().transformNPC(-1);
                break;
        }
    }
}
