package plugin.npc.familiar;

import org.gielinor.game.content.skill.Skills;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.content.skill.member.summoning.familiar.Forager;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

/**
 * Represents the Magpie familiar.
 *
 * @author Aero
 * @author Vexia
 */
public class MagpieNPC extends Forager {

    /**
     * The items to forage.
     */
    private static final Item[] ITEMS = new Item[]{
        new Item(1623),
        new Item(1621),
        new Item(1619),
        new Item(1617),
        new Item(1625),
        new Item(1627),
        new Item(1629),
        new Item(1637),
        new Item(1641),
        new Item(1639),
        new Item(1643)
    };

    /**
     * Constructs a new {@code MagpieNPC} {@code Object}.
     */
    public MagpieNPC() {
        this(null, 6824);
    }

    /**
     * Constructs a new {@code MagpieNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public MagpieNPC(Player owner, int id) {
        super(owner, id, 3400, 12041, 3, ITEMS);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new MagpieNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial special) {
        visualize(Animation.create(8020), Graphics.create(1336));
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.getSkills().updateLevel(Skills.THIEVING, 2);
        owner.visualize(new Animation(7660), new Graphics(1296));
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6824 };
    }

}
