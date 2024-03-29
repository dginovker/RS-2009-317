package plugin.npc.familiar;

import java.util.List;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the Spirit Tz-Kih familiar.
 * @author Vexia
 * @author Aero
 */
public class SpiritTzKihNPC extends Familiar {

    /**
     * Constructs a new {@code SpiritTzKihNPC} {@code Object}.
     */
    public SpiritTzKihNPC() {
        this(null, 7361);
    }

    /**
     * Constructs a new {@code SpiritTzKihNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public SpiritTzKihNPC(Player owner, int id) {
        super(owner, id, 1800, 12808, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new SpiritTzKihNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final List<Entity> entitys = RegionManager.getLocalEntitys(owner, 8);
        if (entitys.size() == 0) {
            return false;
        }
        boolean success = false;
        Entity target = null;
        for (int i = 0; i < 2; i++) {
            if (entitys.size() >= i) {
                target = entitys.get(i);
                if (target == null || target == this || target == owner) {
                    continue;
                }
                if (!canCombatSpecial(target)) {
                    continue;
                }
                success = true;
                sendFamiliarHit(target, 7, Graphics.create(1329));
            }
        }
        if (success) {
            animate(Animation.create(8257));
            return true;
        }
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7361, 7362 };
    }

}
