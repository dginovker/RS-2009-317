package plugin.npc.osrs.kraken;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.map.Location;

/**
 *
 * Created by Stan van der Bend on 11/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.kraken
 */
public class KrakenTentacleNPC extends AbstractNPC{

    private KrakenTentacleNPC(int id, Location location) {
        super(id, location);
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KrakenTentacleNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{3580,3618,3621};
    }
}
class KrakenTentacleCombat extends CombatSwingHandler{

    /**
     * Constructs a new {@code CombatSwingHandler} {@code Object}
     *
     * @param type The combat style.
     */
    public KrakenTentacleCombat(CombatStyle type) {
        super(type);
    }

    @Override
    public int swing(Entity entity, Entity victim, BattleState state) {
        return 0;
    }

    @Override
    public void impact(Entity entity, Entity victim, BattleState state) {

    }

    @Override
    public void visualizeImpact(Entity entity, Entity victim, BattleState state) {

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
    public double getSetMultiplier(Entity e, int skillId) {
        return 0;
    }


}
