package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Forge Regent familiar.
 *
 * @author Aero
 */
public class ForgeRegentNPC extends Familiar {

    /**
     * Constructs a new {@code ForgeRegentNPC} {@code Object}.
     */
    public ForgeRegentNPC() {
        this(null, 7335);
    }

    /**
     * Constructs a new {@code ForgeRegentNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public ForgeRegentNPC(Player owner, int id) {
        super(owner, id, 4500, 12782, 6);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new ForgeRegentNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7335, 7336 };
    }

}
