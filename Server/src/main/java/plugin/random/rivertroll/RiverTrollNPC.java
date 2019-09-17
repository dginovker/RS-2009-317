package plugin.random.rivertroll;

import java.security.SecureRandom;

import org.gielinor.game.content.anticheat.AntiMacroEvent;
import org.gielinor.game.content.anticheat.AntiMacroNPC;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles the river troll npc.
 *
 * @author Vexia
 */
public final class RiverTrollNPC extends AntiMacroNPC {

    /**
     * The river troll npc.
     */
    private static final int[] IDS = new int[]{ 391, 392, 393, 394, 395, 396 };

    /**
     * Constructs a new {@code RiverTrollNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     * @param event    the event.
     * @param player   the player.
     */
    public RiverTrollNPC(int id, Location location, AntiMacroEvent event, Player player) {
        super(id, location, event, player);
    }

    public RiverTrollNPC() {
        super(0, null, null, null);
    }

    @Override
    public void init() {
        super.init();
        setRespawn(false);
        getProperties().getCombatPulse().attack(player);
        sendChat("Fishies be mine! Leave dem fishies!");
    }

    @Override
    public void handleTickActions() {
        super.handleTickActions();
        if (!getProperties().getCombatPulse().isAttacking()) {
            getProperties().getCombatPulse().attack(player);
        }
        if (getProperties().getCombatPulse().isAttacking()) {
            if (RandomUtil.random(40) < 3) {
                sendChat("Fishies be mine! Leave dem fishies!");
            }
        }
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player player = ((Player) killer);
            SecureRandom secureRandom = new SecureRandom();
            int[] RANDOM_FISH = new int[]{ 359, 331, 335, 345, 349, 327, 317, 321, 371, 383 };
            Item[] OTHER_DROPS = new Item[]{ new Item(532), new Item(526), new Item(151), new Item(313, 50), new Item(314, 20), new Item(407), new Item(405), new Item(1969) };
            GroundItemManager.create(new Item(RANDOM_FISH[secureRandom.nextInt(RANDOM_FISH.length)], 1), getLocation(), player);
            GroundItemManager.create(new Item(RANDOM_FISH[secureRandom.nextInt(RANDOM_FISH.length)], 1), getLocation(), player);
            if (secureRandom.nextBoolean()) {
                GroundItemManager.create(new Item(RANDOM_FISH[secureRandom.nextInt(RANDOM_FISH.length)], 1), getLocation(), player);
            }
            GroundItemManager.create(OTHER_DROPS[secureRandom.nextInt(OTHER_DROPS.length)], getLocation(), player);
        }
    }

    @Override
    public boolean isIgnoreMultiBoundaries(Entity victim) {
        return victim == player;
    }

    @Override
    public int[] getIds() {
        return IDS;
    }

}
