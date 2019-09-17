package plugin.skill.runecrafting.abyss;

import org.gielinor.game.content.skill.free.runecrafting.Pouch;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Handles an abyssal npc.
 *
 * @author Vexia
 */
public final class AbyssalNPC extends AbstractNPC {

    /**
     * Constructs a new {@code AbyssalNPC} {@code Object}.
     */
    public AbyssalNPC() {
        super(0, null, true);
        setAggressive(true);
    }

    /**
     * Constructs a new {@code AbyssalNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     */
    public AbyssalNPC(int id, Location location) {
        super(id, location, true);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new AbyssalNPC(id, location);
    }

    @Override
    public void finalizeDeath(Entity killer) {
        super.finalizeDeath(killer);
        if (killer instanceof Player) {
            Player p = killer.asPlayer();
            if (RandomUtil.random(750) < 12) {
                Item pouch = getPouch(p);
                if (pouch != null) {
                    getDefinition().getDropTables().drop(this, p);
                }
            }
        }
    }

    @Override
    public void init() {
        if (getLocation().getRegionId() == 12107) {
            setAggressive(true);
        }
        super.init();
    }

    /**
     * Gets the next pouch item.
     *
     * @param player the player.
     * @return the pouch.
     */
    private Item getPouch(Player player) {
        Item pouch = Pouch.SMALL.getPouch();
        if (player.hasItem(pouch)) {
            pouch = Pouch.MEDIUM.getPouch();
        }
        if (player.hasItem(Pouch.MEDIUM.getPouch())) {
            pouch = Pouch.LARGE.getPouch();
        }
        if (player.hasItem(Pouch.LARGE.getPouch())) {
            pouch = Pouch.GIANT.getPouch();
        }
        if (player.hasItem(Pouch.GIANT.getPouch())) {
            pouch = null;
        }
        return pouch;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 2263, 2264, 2265 };
    }

}
