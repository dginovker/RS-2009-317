package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Ice Titan familiar.
 * @author Aero
 */
public class IceTitanNPC extends Familiar {

    /**
     * Constructs a new {@code IceTitanNPC} {@code Object}.
     */
    public IceTitanNPC() {
        this(null, 7359);
    }

    /**
     * Constructs a new {@code IceTitanNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public IceTitanNPC(Player owner, int id) {
        super(owner, id, 6400, 12806, 20);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new IceTitanNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7359, 7360 };
    }

}
