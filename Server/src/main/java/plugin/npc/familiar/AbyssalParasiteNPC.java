package plugin.npc.familiar;

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
 * Represents the Abyssal Parasite familiar.
 *
 * @author Aero
 *         TODO Abyss
 */
public class AbyssalParasiteNPC extends BurdenBeast {

    /**
     * The special move flag.
     */
    @SuppressWarnings("unused")
    private boolean specialMove = false;

    /**
     * Constructs a new {@code AbyssalParasiteNPC} {@code Object}.
     */
    public AbyssalParasiteNPC() {
        this(null, 6818);
    }

    /**
     * Constructs a new {@code AbyssalParasiteNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public AbyssalParasiteNPC(Player owner, int id) {
        super(owner, id, 3000, 12035, 1, 7);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new AbyssalParasiteNPC(owner, id);
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
        visualize(Animation.create(7672), Graphics.create(1422));
        Projectile.magic(this, target, 1423, 40, 36, 51, 10).send();
        target.getSkills().decrementPrayerPoints(RandomUtil.random(1, 3));
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6818, 6819 };
    }

}
