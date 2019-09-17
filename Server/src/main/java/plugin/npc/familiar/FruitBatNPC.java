package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Fruit Bat familiar.
 *
 * @author Aero
 */
public class FruitBatNPC extends Familiar {

    /**
     * Constructs a new {@code FruitBatNPC} {@code Object}.
     */
    public FruitBatNPC() {
        this(null, 6817);
    }

    /**
     * Constructs a new {@code FruitBatNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public FruitBatNPC(Player owner, int id) {
        super(owner, id, 4500, 12033, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new FruitBatNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6817 };
    }

}
