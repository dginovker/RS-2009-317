package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.misc.RandomUtil;

import java.util.List;

/**
 * Represents the Giant Chinchompa familiar.
 *
 * @author Aero
 * @author Vexia
 */
public class GiantChinchompaNPC extends Familiar {

    /**
     * Constructs a new {@code GiantChinchompaNPC} {@code Object}.
     */
    public GiantChinchompaNPC() {
        this(null, 7353);
    }

    /**
     * Constructs a new {@code GiantChinchompaNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public GiantChinchompaNPC(Player owner, int id) {
        super(owner, id, 3100, 12800, 3);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new GiantChinchompaNPC(owner, id);
    }

    @Override
    public void onAttack(Entity entity) {
        super.onAttack(entity);
        if (RandomUtil.random(20) == 10) {
            executeSpecialMove(new FamiliarSpecial(null));
        }
    }

    @Override
    protected boolean specialMove(FamiliarSpecial familiarSpecial) {
        if (!isOwnerAttackable()) {
            return false;
        }
        final List<Entity> entitys = RegionManager.getLocalEntitys(owner, 6);
        entitys.remove(owner);
        entitys.remove(this);
        sendChat("Squeak!");
        animate(Animation.create(7758));
        graphics(Graphics.create(1364));
        World.submit(new Pulse(3, owner, this) {

            @Override
            public boolean pulse() {
                for (Entity entity : entitys) {
                    if (DeathTask.isDead(entity)) {
                        continue;
                    }
                    if (!entity.getZoneMonitor().continueAttack(entity, CombatStyle.MELEE, false)) {
                        continue;
                    }
                    if (entity instanceof Player && ((Player) entity).isIronman()) {
                        continue;
                    }
                    if (canCombatSpecial(entity, false)) {
                        entity.getImpactHandler().manualHit(GiantChinchompaNPC.this, RandomUtil.random(13), HitsplatType.NORMAL);
                    }
                }
                dismiss();
                return true;
            }
        });
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7353, 7354 };
    }

}
