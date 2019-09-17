package plugin.npc.familiar;

import java.security.SecureRandom;

import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.impl.Projectile;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the Abyssal Titan familiar.
 *
 * @author Aero
 */
public class AbyssalTitanNPC extends BurdenBeast {

    /**
     * Constructs a new {@code AbyssalTitanNPC} {@code Object}.
     */
    public AbyssalTitanNPC() {
        this(null, 7349);
    }

    /**
     * Constructs a new {@code AbyssalTitanNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public AbyssalTitanNPC(Player owner, int id) {
        super(owner, id, 3200, 12796, 10, 20);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new AbyssalTitanNPC(owner, id);
    }

    @Override
    public boolean isAllowed(Player owner, Item item) {
        if (item.getId() != 1436 && item.getId() != 7936) {
            owner.getActionSender().sendMessage("Your familiar can only hold unnoted essence.");
            return false;
        }
        return super.isAllowed(owner, item);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        final Entity target = special.getTarget();
        if (!canCombatSpecial(target)) {
            return false;
        }
        faceTemporary(target, 2);
        sendFamiliarHit(target, 7);
        visualize(Animation.create(7693), Graphics.create(1422));
        Projectile.magic(this, target, 1423, 40, 36, 51, 10).send();
        if (new SecureRandom().nextInt(100) != 1) {
            target.getSkills().decrementPrayerPoints(RandomUtil.random(3, 5));
        }
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7349, 7350 };
    }

}
