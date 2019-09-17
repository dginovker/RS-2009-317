package org.gielinor.game.system.command.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

/**
 * Teleports the player to the given location.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class TeleportCommand extends Command {

    /**
     * The {@link java.util.HashMap} of destinations.
     */
    private static HashMap<String, Location> destinations;

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public boolean isBeta() {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "mtele", "findcoords", "findcoord", "telecs", "tele", "teleport", "teler", "telers",
            "telere", "teleosrs" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("tele", "Teleports to given coordinates or name", getRights(),
            "::tele <lt>[ name]> or <lt>x> <lt>y> <lt>[ z]><br>Examples:<br>::tele varrock<br>::tele 3223 3223<br>::tele 3212 3461 1<br>Names can be found by typing ::findcoords"));
        CommandDescription.add(new CommandDescription("mtele", "Teleports to given coordinates", getRights(),
            "::tele <lt>[ name]> or <lt>x> <lt>y> <lt>[ z]><br>Examples:<br>::tele varrock<br>::tele 3223 3223<br>::tele 3212 3461 1<br>Names can be found by typing ::findcoords"));
        CommandDescription.add(new CommandDescription("telecs", "Teleports to regional coordinates", getRights(),
            "::telecs <lt>region_x> <lt>region_y>"));
        CommandDescription.add(new CommandDescription("teler", "Teleports to the center of a region", getRights(),
            "::teler <lt>region_id><br>Example:<br>::teler 10141"));
        CommandDescription.add(new CommandDescription("telers", "Teleports to the start of a region", getRights(),
            "::teler <lt>region_id><br>Example:<br>::teler 10141"));
        CommandDescription.add(new CommandDescription("telere", "Teleports to the end of a region", getRights(),
            "::teler <lt>region_id><br>Example:<br>::teler 10141"));
        CommandDescription.add(new CommandDescription("findcoords", "Finds coordinates by name", getRights(),
            "::findcoords <lt>name><br>Examples:<br>::findcoords varr<br>::findcoords lumbr<br>::findcoords god wars"));
    }

    @Override
    public void execute(final Player player, String[] args) {
        String lookFor;
        switch (args[0].toLowerCase()) {
            case "findcoords":
            case "findcoord":
                lookFor = toString(args, 1).toLowerCase().replaceAll(" ", "_");
                ArrayList<String> results = new ArrayList<>();
                for (Map.Entry<String, Location> entry : destinations.entrySet()) {
                    String destinationName = (String) entry.getKey();
                    Location location = (Location) entry.getValue();
                    if (destinationName.contains(lookFor) || destinationName.startsWith(lookFor)
                        || destinationName.endsWith(lookFor) || destinationName.equals(lookFor)) {
                        results.add(destinationName + " : " + location.toString());
                    }
                }
                if (results.size() == 0) {
                    player.getActionSender()
                        .sendConsoleMessage("Could not find any coordinates for : \"" + toString(args, 1) + "\"");
                    break;
                }
                player.getActionSender().sendConsoleMessage(results.size() + " results for \"" + toString(args, 1) + "\"");
                for (String result : results) {
                    result = result.replace(lookFor, "<col=FF0000>" + lookFor + "</col>");
                    player.getActionSender().sendConsoleMessage(result);
                }
                break;
            case "teler": // Teleports to the center of the region.
                int regionId = Integer.parseInt(args[1]);
                int x = 32;
                int y = 32;
                if (args.length > 3) {
                    x = Integer.parseInt(args[2]);
                    y = Integer.parseInt(args[3]);
                }
                player.getProperties()
                    .setTeleportLocation(Location.create(((regionId >> 8) << 6) + x, ((regionId & 0xFF) << 6) + y, 0));
                player.debug("Current location=" + player.getProperties().getTeleportLocation());
                return;
            case "telers": // Teleports to the start of the region.
                regionId = Integer.parseInt(args[1]);
                player.getProperties()
                    .setTeleportLocation(Location.create(((regionId >> 8) << 6), ((regionId & 0xFF) << 6), 0));
                player.debug("Current location=" + player.getProperties().getTeleportLocation());
                return;
            case "telere": // Teleports to the end of the region.
                regionId = Integer.parseInt(args[1]);
                player.getProperties().setTeleportLocation(
                    Location.create(((regionId >> 8) << 6) + 63, ((regionId & 0xFF) << 6) + 63, 0));
                player.debug("Current location=" + player.getProperties().getTeleportLocation());
                return;
            case "telecs":
                if (args.length <= 2) {
                    return;
                }
                player.setAttribute("TELE_CS", true);
                Location location = player.getPlayerFlags().getLastSceneGraph();
                x = ((location.getRegionX()) << 3) + Integer.parseInt(args[1]);
                y = ((location.getRegionY()) << 3) + Integer.parseInt(args[2]);
                player.getPulseManager().clear();
                player.setTeleportTarget(Location.create(x, y, player.getLocation().getZ()));
                return;
            case "mtele":
                if (!(args.length == 3 || args.length == 4)) {
                    return;
                }
                location = player.getPlayerFlags().getLastSceneGraph();
                x = Integer.parseInt(args[1]) * 64;
                y = Integer.parseInt(args[2]) * 64;
                player.getPulseManager().clear();
                player.setTeleportTarget(Location.create(x, y, 0));
                return;
            case "teleosrs":
            case "tele":
                if (args.length > 1 && !StringUtils.isNumeric(args[1])) {
                    Location destination = getDestination(toString(args, 1).toLowerCase().replaceAll(" ", "_"));
                    if (destination != null) {
                        player.setTeleportTarget(destination);
                    } else {
                        player.getActionSender().sendMessage("No teleport exists for " + toString(args, 1));
                    }
                } else if (args.length == 3 || args.length == 4) {
                    x = Short.parseShort(args[1]) + (Objects.equals(args[0].toLowerCase(), "teleosrs") ? 4224 : 0);
                    y = Short.parseShort(args[2]) + (Objects.equals(args[0].toLowerCase(), "teleosrs") ? 6656 : 0);
                    int z = player.getLocation().getZ();
                    if (args.length == 4) {
                        z = Byte.parseByte(args[3]);
                    }
                    player.setTeleportTarget(Location.create(x, y, z));
                }
                break;
        }
    }

    /**
     * Gets a destination by name.
     *
     * @param lookFor
     *            The name.
     * @return The location.
     */
    public static Location getDestination(String lookFor) {
        return destinations.get(lookFor);
    }

    /**
     * Sets the destinations mapping.
     *
     * @param destinations1
     *            The destinations map.
     */
    public static void setDestinations(HashMap<String, Location> destinations1) {
        destinations = destinations1;
    }
}
