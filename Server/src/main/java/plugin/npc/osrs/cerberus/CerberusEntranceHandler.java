package plugin.npc.osrs.cerberus;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.component.Component;
import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.slayer.Tasks;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Region;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles Cerberus Lair entrances.
 *
 * @author Vexia
 */
public final class CerberusEntranceHandler extends OptionHandler {

    /**
     * Constructs a new {@code CerberusEntranceHandler Object}.
     */
    public CerberusEntranceHandler() {
        super();
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ObjectDefinition.forId(38484).getConfigurations().put("option:exit", this);
        ObjectDefinition.forId(38501).getConfigurations().put("option:turn", this);
        ObjectDefinition.forId(38501).getConfigurations().put("option:peek", this);
        ObjectDefinition.forId(38509).getConfigurations().put("option:crawl", this);
        ObjectDefinition.forId(38510).getConfigurations().put("option:crawl", this);
        ObjectDefinition.forId(38511).getConfigurations().put("option:crawl", this);
        ObjectDefinition.forId(40460).getConfigurations().put("option:crawl", this);
        ObjectDefinition.forId(40461).getConfigurations().put("option:crawl", this);
        ObjectDefinition.forId(40462).getConfigurations().put("option:crawl", this);
        ObjectDefinition.forId(40443).getConfigurations().put("option:pass", this);
        ObjectDefinition.forId(40443).getConfigurations().put("option:quick pass", this);
        //NPC.create(8714, Location.create(3614, 3747, 0)).init(); //Key Master
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        GameObject object = (GameObject) node;
        switch (object.getId()) {
            case 40443:
                //			if (option.equals("quick pass")) {
                passFlames(player, object);
                //			}
                //			else {
                //				player.getDialogueInterpreter().st
                //			}
                return true;
            case 40460:
            case 40461:
            case 40462:
                if (player.getSkills().getStaticLevel(Skills.SLAYER) < 91) {
                    player.getActionSender().sendMessage("You need a Slayer level of 91 to enter Cerberus' Lair.");
                    return true;
                }
                if (player.getSlayer().getTask() != Tasks.HELLHOUNDS.getTask() && player.getRights() != Rights.GIELINOR_MODERATOR && player.getRights() != Rights.DEVELOPER) {
                    player.getActionSender().sendMessage("You have to be hunting Hellhounds as a slayer task to enter this.");
                    return true;
                }
                player.getProperties().setTeleportLocation(Location.create(1310, 1237, 0));
                return true;
            case 38509:
            case 38510:
            case 38511:
                player.getProperties().setTeleportLocation(Location.create(2873, 9847, 0));
                return true;
            case 38484:
                int id = getRegion(object).getId();
                if (id == 4883) {
                    operateWinch(player, object, Location.create(1290, 1252, 0));
                } else if (id == 5140) {
                    operateWinch(player, object, Location.create(1310, 1273, 0));
                } else {
                    operateWinch(player, object, Location.create(1332, 1252, 0));
                }
                return true;
            case 38501:
                if (option.equals("turn")) {
                    player.animate(new Animation(896));
                    operateWinch(player, object, getRegion(object).getBaseLocation().transform(24, 10, 0));
                } else {
                    player.getActionSender().sendMessage("There are " + getRegion(object).getPlanes()[0].getPlayers().size() + " players in this room.");
                }
                return true;
        }
        return false;
    }

    /**
     * Passes the flames.
     *
     * @param player The player.
     * @param object The object.
     */
    private void passFlames(Player player, GameObject object) {
        Location l = object.getLocation();
        player.lock(2);
        player.getWalkingQueue().reset();
        if (player.getLocation().getY() > l.getY()) {
            player.getWalkingQueue().addPath(l.getX(), l.getY() - 1, true);
        } else {
            player.getWalkingQueue().addPath(l.getX(), l.getY() + 1, true);
        }
        player.getImpactHandler().manualHit(player, 5, HitsplatType.NORMAL);
        player.getActionSender().sendMessage("You're burned by the heath of the flames.");
    }

    /**
     * Operates the winch.
     *
     * @param player   The player.
     * @param object   The winch.
     * @param entrance The entrance location.
     */
    private void operateWinch(final Player player, final GameObject object, final Location entrance) {
        player.lock(5);
        World.submit(new Pulse(1, player) {

            int stage;

            @Override
            public boolean pulse() {
                if (++stage == 1) {
                    player.getInterfaceState().openOverlay(new Component(115));
                    setDelay(3);
                    return false;
                } else if (stage == 2) {
                    setDelay(1);
                    player.getProperties().setTeleportLocation(entrance);
                    return false;
                }
                player.getInterfaceState().closeOverlay();
                return true;
            }
        });
    }

    /**
     * Gets the cerberus lair region for the given node.
     *
     * @param node The node.
     * @return The region.
     */
    private Region getRegion(Node node) {
        int x = node.getLocation().getX();
        if (x < 1294) {
            return RegionManager.forId(4883);
        }
        if (x < 1324) {
            return RegionManager.forId(5140);
        }
        return RegionManager.forId(5395);
    }
}
