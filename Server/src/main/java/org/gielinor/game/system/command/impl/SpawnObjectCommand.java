package org.gielinor.game.system.command.impl;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.node.object.ObjectBuilder;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;
import org.gielinor.game.world.map.Location;

/**
 * Spawns an object at the player's location.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class SpawnObjectCommand extends Command {

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
        return new String[]{ "obj", "object", "tobj" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("object", "Spawns an object", getRights(),
            "::object <lt>id> <lt>[ type]> <lt>[ rotation]>"));
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args[0].equalsIgnoreCase("tobj")) {
            if (args.length < 3) {
                p.getActionSender().sendMessage("Use as ::tobj <lt>object id> <lt>ticks>");
                return;
            }
            int x = p.getLocation().getX();
            int y = p.getLocation().getY();
            int z = p.getLocation().getZ();
            int type = 10;
            int rotation = 1;
            int ticks = Integer.parseInt(args[2]);
            ObjectBuilder.add(new GameObject(Integer.parseInt(args[1]), Location.create(x, y, z), type, rotation),
                ticks);
            return;
        }
        if (args.length < 2) {
            p.getActionSender().sendMessage("Use as ::object <lt>object id>");
            return;
        }
        int x = p.getLocation().getX();
        int y = p.getLocation().getY();
        int z = p.getLocation().getZ();
        int type = 10;
        int rotation = 1;
        if (args.length == 3) {
            type = Integer.parseInt(args[2]);
        }
        if (args.length == 4) {
            rotation = Integer.parseInt(args[3]);
        }
        GameObject gameObject = new GameObject(Integer.parseInt(args[1]), Location.create(x, y, z), type, rotation);
        ObjectBuilder.add(gameObject);
		/*p.setAttribute("SPAWN_OBJECT", gameObject);
		GameObject spawnedObject = p.getAttribute("SPAWN_OBJECT");
		if (((ObjectSpawnService) World.getWorld().getApplicationContext().getBean("objectSpawnService"))
				.insertSpawnedObject(spawnedObject)) {
			p.getDialogueInterpreter().sendPlaneMessage("Placed object successfully.");
		} else {
			p.getDialogueInterpreter().sendPlaneMessage("Could not place object!");
		}*/
    }
}
