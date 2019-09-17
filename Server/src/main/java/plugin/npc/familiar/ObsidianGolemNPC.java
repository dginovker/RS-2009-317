package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;

/**
 * Represents the Obsidian Golem familiar.
 * @author Aero
 */
public class ObsidianGolemNPC extends Familiar {

    /**
     * Constructs a new {@code ObsidianGolemNPC} {@code Object}.
     */
    public ObsidianGolemNPC() {
        this(null, 7345);
    }

    /**
     * Constructs a new {@code ObsidianGolemNPC} {@code Object}.
     * @param owner The owner.
     * @param id The id.
     */
    public ObsidianGolemNPC(Player owner, int id) {
        super(owner, id, 5500, 12792, 12);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new ObsidianGolemNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[]{ 7345, 7346 };
    }

}
