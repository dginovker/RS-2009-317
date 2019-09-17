package plugin.npc.familiar;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the War tortoise familiar.
 *
 * @author Emperor
 */
public final class WarTortoiseNPC extends BurdenBeast {

    /**
     * Constructs a new {@code WarTortoiseNPC} {@code Object}.
     */
    public WarTortoiseNPC() {
        this(null, 6815);
    }

    /**
     * Constructs a new {@code WarTortoiseNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The NPC id.
     */
    public WarTortoiseNPC(Player owner, int id) {
        super(owner, id, 4300, 12031, 20, 18);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new WarTortoiseNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        owner.getSkills().updateLevel(Skills.DEFENCE, 9, owner.getSkills().getStaticLevel(Skills.DEFENCE) + 9);
        visualize(Animation.create(8288), Graphics.create(1414));
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1310));
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6815, 6816 };
    }

}
