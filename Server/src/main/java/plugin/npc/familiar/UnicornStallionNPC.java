package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the Unicorn Stallion familiar.
 *
 * @author Aero
 */
public class UnicornStallionNPC extends Familiar {

    /**
     * Constructs a new {@code UnicornStallion} {@code Object}.
     */
    public UnicornStallionNPC() {
        this(null, 6822);
    }

    /**
     * Constructs a new {@code UnicornStallion} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public UnicornStallionNPC(Player owner, int id) {
        super(owner, id, 5400, 12039, 20);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new UnicornStallionNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial familiarSpecial) {
        int maximumHealth = owner.getSkills().getMaximumLifepoints();
        visualize(Animation.create(8267), Graphics.create(1356));
        owner.getSkills().heal((int) (maximumHealth * 0.15));
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1298));
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6822, 6823 };
    }

}
