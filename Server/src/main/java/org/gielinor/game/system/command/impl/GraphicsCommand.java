package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.out.UpdateAreaPosition;

/**
 * Displays graphics around the player.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class GraphicsCommand extends Command {

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
        return new String[]{ "gfx", "ogfx", "graphic", "graphics", "ogfxloc", "gfxloc" };
    }

    @Override
    public void init() {
        CommandDescription.add(
            new CommandDescription("gfx", "Plays a graphic", getRights(), "::gfx <lt>graphics id> <lt>[ height]>"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args[0].equalsIgnoreCase("gfxloc") || args[0].equalsIgnoreCase("ogfxloc")) {
            int id = Integer.parseInt(args[1]);
            if (args[0].startsWith("ogfxloc")) {
                id += 2045;
            }
            Location l = player.getLocation().transform(2, 2, 0);
            Graphics g = new Graphics(id);
            PacketBuilder packetBuilder = UpdateAreaPosition.getBuffer(player, l);
            packetBuilder.put(4);
            packetBuilder.put(0);
            packetBuilder.putShort(g.getId());
            packetBuilder.put(g.getHeight());
            packetBuilder.putShort(g.getDelay());
            player.getSession().write(packetBuilder);
            return;
        }
        if (args.length < 2) {
            player.getActionSender().sendMessage("Use as ::" + args[0] + " <lt>graphics id> <lt>[ height]>");
            return;
        }
        if (Integer.parseInt(args[1]) > 800 && !player.specialDetails()) {
            args[1] = "0";
        }
        int id = Integer.parseInt(args[1]);
        if (args[0].startsWith("ogfx")) {
            id += 2045;
        }
        player.getActionSender().sendConsoleMessage("Playing graphics " + id + ".");
        player.playGraphics(
            Graphics.create(Integer.parseInt(args[1]), (args.length == 3 ? Integer.parseInt(args[2]) : 0)));
    }
}
