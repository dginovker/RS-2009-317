package plugin.zone;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * The desert zone map.
 *
 * @author Emperor
 * @author 'Vexia
 * @version 2.0
 */
public final class DesertZone extends MapZone implements Plugin<Object> {

    /**
     * Represents the hydrateable waterskin items.
     */
    private static final Item[] WATER_SKINS = new Item[]{ new Item(1823), new Item(1825), new Item(1827), new Item(1829) };

    /**
     * Represents data of water vessils to dry up.
     */
    private static final int[][] VESSILS = new int[][]{ { 1937, 1935 }, { 1929, 1925 }, { 1921, 1923 }, { 227, 229 } };

    /**
     * Represents the animation of drinking water.
     */
    private static final Animation ANIMATION = new Animation(829);

    /**
     * The players list.
     */
    private static final List<Player> PLAYERS = new ArrayList<>();

    /**
     * The water draining pulse.
     */
    private static Pulse pulse = new Pulse(3) {

        @Override
        public boolean pulse() {
            for (Player player : PLAYERS) {
                if (player.getInterfaceState().isOpened() || player.getInterfaceState().hasChatbox() || player.getLocks().isMovementLocked()) {
                    continue;
                }
                if (player.getAttribute("desert-delay", -1) < World.getTicks()) {
                    effect(player);
                }
            }
            return PLAYERS.isEmpty();
        }
    };

    /**
     * Method used to handle the desert effect on the <code>Player</code>.
     *
     * @param player the <code>Player</code>.
     */
    private static void effect(Player player) {
        player.setAttribute("desert-delay", World.getTicks() + getDelay(player));
        evaporate(player);
        if (drink(player)) {
            return;
        }
        player.getImpactHandler().manualHit(player, RandomUtil.random(1, player.getLocation().getY() < 2990 ? 12 : 8), HitsplatType.NORMAL);
        player.getActionSender().sendMessage("You start dying of thirst while you're in the desert.", 1);
    }

    /**
     * Method used to evaporate the water vessils in the inventory.
     *
     * @param player the <code>Player</code>
     */
    public static void evaporate(Player player) {
        for (int i = 0; i < VESSILS.length; i++) {
            if (player.getInventory().contains(VESSILS[i][0], 1)) {
                if (player.getInventory().remove(new Item(VESSILS[i][0]))) {
                    player.getInventory().add(new Item(VESSILS[i][1]));
                    player.getActionSender().sendMessage("The water in your " + ItemDefinition.forId(VESSILS[i][0]).getName().toLowerCase().replace("of water", "").trim() + " evaporates in the desert heat.", 1);
                }
            }
        }
    }

    /**
     * Method used to drink a waterskin in the inventory.
     *
     * @param p the player.
     */
    public static boolean drink(Player p) {
        for (Item i : WATER_SKINS) {
            if (p.getInventory().containsItem(i) && p.getInventory().remove(i)) {
                p.getInventory().add(new Item(i.getId() + 2));
                p.animate(ANIMATION);
                p.getActionSender().sendMessage("You take a drink of water.", 1);
                return true;
            }
        }
        if (p.getInventory().contains(1831, 1)) {
            p.getActionSender().sendMessage("Perhaps you should fill up one of your empty waterskins.", 1);
        } else {
            p.getActionSender().sendMessage("You should get a waterskin for any travelling in the desert.", 1);
        }
        return false;
    }

    /**
     * Method used to calculate the delay to the next desert effect.
     *
     * @param player the player.
     * @return <code>Long</code> the amount of the delay.
     */
    private static int getDelay(Player player) {
        int delay = 116;
        if (player.getEquipment().contains(1833, 1)) {
            delay += 17;
        }
        if (player.getEquipment().contains(1835, 1)) {
            delay += 17;
        }
        if (player.getEquipment().contains(1837, 1)) {
            delay += 17;
        }
        return delay;
    }

    /**
     * Constructs a new {@code DesertZone} {@code Object}.
     */
    public DesertZone() {
        super("Desert Zone", true);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        //ZoneBuilder.configure(this);
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public void configure() {
        ZoneBorders borders = new ZoneBorders(3063, 2725, 3544, 3115);
        borders.addException(new ZoneBorders(3152, 2961, 3191, 2999));//Bandit camp
        borders.addException(new ZoneBorders(3147, 3019, 3185, 3059));//Bedabin
        borders.addException(new ZoneBorders(3217, 2881, 3248, 2914));//pyramid
        borders.addException(new ZoneBorders(3007, 4672, 3071, 4735));//agility-pyramid top(region12105)
        borders.addException(new ZoneBorders(3274, 3014, 3305, 3041));//Mining camp
        borders.addException(new ZoneBorders(3260, 9408, 3331, 9472));//Mining camp
        register(borders);
        pulse.stop();
    }

    @Override
    public boolean enter(Entity e) {
        if (e instanceof Player) {
            Player p = (Player) e;
            p.setAttribute("desert-delay", World.getTicks() + getDelay(p));
            PLAYERS.add(p);
            if (!pulse.isRunning()) {
                pulse.restart();
                pulse.start();
                World.submit(pulse);
            }
        }
        return true;
    }

    @Override
    public boolean leave(Entity e, boolean logout) {
        if (e instanceof Player) {
            PLAYERS.remove(e);
            e.removeAttribute("desert-delay");
        }
        return super.leave(e, logout);
    }

}
