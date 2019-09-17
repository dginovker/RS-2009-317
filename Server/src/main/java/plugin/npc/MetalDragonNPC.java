package plugin.npc;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.combat.equipment.SwitchAttack;
import org.gielinor.game.node.entity.combat.handlers.DragonfireSwingHandler;
import org.gielinor.game.node.entity.combat.handlers.MultiSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Handles a metal dragon (bronze, iron, steel).
 *
 * @author Emperor
 */
public final class MetalDragonNPC extends AbstractNPC {

    /**
     * The dragonfire attack.
     */
    private static final SwitchAttack DRAGONFIRE = DragonfireSwingHandler.get(true, 52, new Animation(81, Priority.HIGH), null, null, Projectile.create((Entity) null, null, 54, 40, 36, 41, 46, 20, 255));

    /**
     * Handles the combat.
     */
    private final CombatSwingHandler combatAction = new MultiSwingHandler(true, new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(80, Priority.HIGH)), new SwitchAttack(CombatStyle.MELEE.getSwingHandler(), new Animation(91, Priority.HIGH)), DRAGONFIRE);

    /**
     * Constructs a new {@code MetalDragonNPC} {@code Object}.
     */
    public MetalDragonNPC() {
        super(1590, null);
    }

    /**
     * Constructs a new {@code MetalDragonNPC} {@code Object}.
     *
     * @param id       The NPC id.
     * @param location The location.
     */
    public MetalDragonNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return combatAction;
    }

    @Override
    public int getDragonfireProtection(boolean fire) {
        return 0x2 | 0x4 | 0x8;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new MetalDragonNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{ 1590, //Bronze dragon
            1591, //Iron dragon
            1592, //Steel dragon
            3590, //Steel dragon (POH)
            5363, //Mithril dragon
            20270,
            20272,
            27275,
            27274,
            20274
        };
    }

}
