package plugin.interaction.item;

import java.security.SecureRandom;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.interaction.NodeUsageEvent;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.interaction.UseWithHandler;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the plugin for the ectophial.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class EctophialPlugin extends OptionHandler {

    /**
     * The secure random.
     */
    private final SecureRandom random = new SecureRandom();
    /**
     * The ectophial item.
     */
    private final Item ECTOPHILE = new Item(4251);
    /**
     * The empty ectophial item.
     */
    private final Item EMPTY_ECTOPHILE = new Item(4252);
    /**
     * The location to teleport to upon emptying the ectophial.
     */
    private final Location location = Location.create(3659, 3517, 0);
    /**
     * The teleporting animation.
     */
    private final Animation ANIMATION = Animation.create(1652);
    /**
     * The teleporting graphics.
     * <br />
     * TODO find the right graphic
     */
    private final Graphics GRAPHIC = Graphics.create(1688);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ItemDefinition.forId(4251).getConfigurations().put("option:empty", this);
        return this;
    }

    @Override
    public boolean handle(final Player player, Node node, String option) {
        if (!player.getInventory().remove(ECTOPHILE)) {
            return false;
        }
        player.getActionSender().sendMessage("You empty the ectoplasm onto the ground around your feet...");
        player.animate(ANIMATION);
        //player.playGraphics(GRAPHIC);
        player.getInventory().add(EMPTY_ECTOPHILE);
        World.submit(new Pulse(3, player) {

            @Override
            public boolean pulse() {
                player.setTeleportTarget(location.transform((random.nextBoolean() ? random.nextInt(5) : -random.nextInt(5)), (random.nextBoolean() ? random.nextInt(5) : -random.nextInt(5)), 0));
                GameObject ECTOFUNTUS = getEctofuntus(player);
                player.getActionSender().sendMessage("...and the world changes around you.");
                if (ECTOFUNTUS == null) {
                    return true;
                }
                player.faceLocation(ECTOFUNTUS.getLocation());
                return true;
            }
        });
        World.submit(new Pulse(5, player) {

            @Override
            public boolean pulse() {
                GameObject ECTOFUNTUS = getEctofuntus(player);
                Item ectophile = player.getInventory().getById(EMPTY_ECTOPHILE.getId());
                if (ECTOFUNTUS == null || ectophile == null) {
                    return true;
                }
                player.faceLocation(ECTOFUNTUS.getLocation());
                UseWithHandler.run(new NodeUsageEvent(player, 0, ectophile, ECTOFUNTUS));
                return true;
            }
        });

        return true;
    }

    /**
     * Gets the ectofuntus.
     *
     * @param player The player.
     * @return The ectofuntus.
     */
    public GameObject getEctofuntus(Player player) {
        GameObject ECTOFUNTUS = null;
        List<GameObject> objectList = RegionManager.getSurroundingObjects(player, "ectofuntus", 20);
        if (objectList != null && objectList.size() > 0) {
            for (GameObject ecto : objectList) {
                if (ecto == null) {
                    continue;
                }
                if (ECTOFUNTUS == null) {
                    ECTOFUNTUS = objectList.get(0);
                }
                if (!ECTOFUNTUS.getDefinition().hasAction("worship")) {
                    objectList.remove(ECTOFUNTUS);
                    continue;
                }
                if (location.getDistance(ecto.getLocation()) < location.getDistance(ECTOFUNTUS.getLocation())) {
                    ECTOFUNTUS = ecto;
                }
            }
        }
        return ECTOFUNTUS;
    }
}
