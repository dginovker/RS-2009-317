package plugin.npc.familiar;

import java.util.List;

import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Represents the Spirit Kalphite familiar.
 * @author Vexia
 * @author Aero
 */
public class SpiritKalphiteNPC extends BurdenBeast {

    /**
     * Constructs a new {@code SpiritKalphiteNPC} {@code Object}.
     */
    public SpiritKalphiteNPC() {
        this(null, 6994);
    }

    /**
     * Constructs a new {@code SpiritKalphiteNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public SpiritKalphiteNPC(Player owner, int id) {
        super(owner, id, 2200, 12063, 6, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritKalphiteNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        if (!isOwnerAttackable()) {
            return false;
        }
        final List<Entity> entitys = RegionManager.getLocalEntitys(owner, 6);
        visualize(Animation.create(8517), Graphics.create(1350));
        World.submit(new Pulse(1, owner) {

            @Override
            public boolean pulse() {
                int count = 0;
                for (Entity entity : entitys) {
                    if (count > 5) {
                        return true;
                    }
                    if (!canCombatSpecial(entity)) {
                        continue;
                    }
                    Projectile.magic(SpiritKalphiteNPC.this, entity, 1349, 40, 36, 50, 5).send();
                    sendFamiliarHit(entity, 20);
                    count++;
                }
                return true;
            }
        });
        return false;
    }

    @Override
    public String getText() {
        return "Hsssss!";
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6994, 6995 };
    }

}
