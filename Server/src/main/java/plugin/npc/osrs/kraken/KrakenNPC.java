package plugin.npc.osrs.kraken;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.BattleState;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 *
 * Created by Stan van der Bend on 11/01/2018.
 *
 * project: GielinorGS
 * package: plugin.npc.osrs.kraken
 */
public class KrakenNPC extends AbstractNPC{

    private final static int NPC_ID = 3847;

    private KrakenCombat krakenCombat = new KrakenCombat();

    private KrakenNPC(int id, Location location) {
        super(id, location);
    }

    public KrakenNPC(Location location) {
        super(NPC_ID, location);
    }

    @Override
    public CombatSwingHandler getSwingHandler(boolean swing) {
        return krakenCombat;
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return new KrakenNPC(id, location);
    }

    @Override
    public int[] getIds() {
        return new int[]{NPC_ID};
    }

}

class KrakenCombat extends CombatSwingHandler {

    private final Animation attack_animation = new Animation(1);
    private final Graphics start_graphic = new Graphics(1);

    KrakenCombat() {
        super(CombatStyle.MAGIC);
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

        createProjectile(entity, victim).send();

        entity.visualize(attack_animation, start_graphic);

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

    private Projectile createProjectile(Entity entity, Entity target){
        return Projectile.create(entity, target, 2705, 43, 31, 44, 3, 0,  0);
    }
}
