package plugin.combat.special;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.handlers.MeleeSwingHandler;
import org.gielinor.game.node.entity.impl.Animator.Priority;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Direction;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.Point;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the Dragon spear special attack.
 *
 * reference:
 *  - http://oldschoolrunescape.wikia.com/wiki/Special_attacks
 *  - http://oldschoolrunescape.wikia.com/wiki/Dragon_spear
 *
 * @author Emperor
 * @version 1.0
 */
public final class ShoveSpecial extends MeleeSwingHandler implements Plugin<Object> {

    private static final int SPECIAL_ENERGY = 25;
    private static final Animation
        ANIMATION = new Animation(1064, Priority.HIGH),
        STUN_ANIM = new Animation(424);
    private static final Graphics
        GRAPHIC = new Graphics(253, 96);

    @Override public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override public Plugin<Object> newInstance(Object arg) {
        CombatStyle.MELEE.getSwingHandler().register(1249, this);
        CombatStyle.MELEE.getSwingHandler().register(1263, this);
        CombatStyle.MELEE.getSwingHandler().register(3176, this);
        CombatStyle.MELEE.getSwingHandler().register(5716, this);
        CombatStyle.MELEE.getSwingHandler().register(5730, this);
        CombatStyle.MELEE.getSwingHandler().register(11716, this);
        CombatStyle.MELEE.getSwingHandler().register(Item.ZAMORAKIAN_HASTA, this);
        return this;
    }

    @Override public int swing(Entity entity, final Entity victim, BattleState state) {
        if (victim.size() > 1) {
            ((Player) entity).getActionSender().sendMessage("That creature is too large to knock back!");
            return -1;
        }
        if (!((Player) entity).getSettings().drainSpecial(SPECIAL_ENERGY)) {
            return -1;
        }
        state.setEstimatedHit(-1);
        Direction dir = null;
        int vx = victim.getLocation().getX();
        int vy = victim.getLocation().getY();
        int sx = entity.getLocation().getX();
        int sy = entity.getLocation().getY();
        if (vx == sx && vy > sy) {
            dir = Direction.NORTH;
        } else if (vx == sx && vy < sy) {
            dir = Direction.SOUTH;
        } else if (vx > sx && vy == sy) {
            dir = Direction.EAST;
        } else if (vx < sx && vy == sy) {
            dir = Direction.WEST;
        } else if (vx > sx && vy > sy) {
            dir = Direction.NORTH_EAST;
        } else if (vx < sx && vy > sy) {
            dir = Direction.NORTH_WEST;
        } else if (vx > sx && vy < sy) {
            dir = Direction.SOUTH_EAST;
        } else if (vx < sx && vy < sy) {
            dir = Direction.SOUTH_WEST;
        }
        victim.getWalkingQueue().reset();
        victim.getPulseManager().clear();
        victim.animate(STUN_ANIM);
        victim.getStateManager().set(EntityState.STUNNED, 5);
        if (dir != null) {
            Point p = Direction.getWalkPoint(dir);
            Location dest = victim.getLocation().transform(p.getX(), p.getY(), 0);
            if (Pathfinder.find(victim, dest, false, Pathfinder.DUMB).isSuccessful()) {
                victim.getWalkingQueue().addPath(dest.getX(), dest.getY());
            }
        }
        entity.asPlayer().getAudioManager().send(2533);
        return 1;
    }

    @Override public void visualize(Entity entity, Entity victim, BattleState state) {
        entity.visualize(ANIMATION, GRAPHIC);
    }

    @Override public void visualizeImpact(Entity entity, Entity victim, BattleState state) { }
    @Override public void impact(Entity entity, Entity victim, BattleState state) { }
}
