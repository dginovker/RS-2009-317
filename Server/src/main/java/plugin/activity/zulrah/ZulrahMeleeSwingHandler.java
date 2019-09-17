package plugin.activity.zulrah;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.InteractionType;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;

/**
 * Represents the Zulrah melee swing handler.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ZulrahMeleeSwingHandler extends ZulrahCombatSwingHandler {

    /**
     * The animation for the attack.
     */
    public static final Animation ANIMATION = new Animation(5807, true);

    /**
     * Constructs a new <code>ZulrahMeleeSwingHandler</code>.
     */
    public ZulrahMeleeSwingHandler() {
        super(CombatStyle.MELEE);
    }

    @Override
    public InteractionType canSwing(Entity entity, Entity victim) {
        return Location.getDistance(entity.getCenterLocation(), victim.getLocation()) > 4.5 ? InteractionType.NO_INTERACT : InteractionType.STILL_INTERACT;
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState battleState) {
        battleState.setEstimatedHit(10);
        return 1;
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState battleState) {
    }

    @Override
    public void onImpact(final Entity entity, final Entity victim, final BattleState battleState) {
        if (getZulrahNPC() == null || !getZulrahNPC().isActive()) {
            return;
        }
        if (battleState == null) {
            return;
        }
        if (battleState.getTargets() != null && battleState.getTargets().length > 0) {
            if (!(battleState.getTargets().length == 1 && battleState.getTargets()[0] == battleState)) {
                for (BattleState s : battleState.getTargets()) {
                    if (s != null && s != battleState) {
                        onImpact(entity, s.getVictim(), s);
                    }
                }
                return;
            }
        }
        assert victim != null;
        victim.onImpact(entity, battleState);
    }

    @Override
    public void visualize(Entity entity, Entity victim, BattleState battleState) {
        entity.animate(ANIMATION);
    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState battleState) {

    }

    @Override
    public int calculateAccuracy(Entity entity) {
        return 0;
    }

    @Override
    public int calculateHit(Entity entity, Entity victim, double modifier) {
        return 0;
    }

    @Override
    public int calculateDefence(Entity entity, Entity attacker) {
        return 0;
    }

    @Override
    public double getSetMultiplier(Entity entity, int skillId) {
        return 0;
    }

    @Override
    public int getCombatDistance(Entity entity, Entity victim, int distance) {
        return 20;
    }
}
