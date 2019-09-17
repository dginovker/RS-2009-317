package plugin.npc.familiar;

import org.gielinor.game.content.skill.member.summoning.SummoningScroll;
import org.gielinor.game.content.skill.member.summoning.familiar.BurdenBeast;
import org.gielinor.game.content.skill.member.summoning.familiar.Familiar;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.parser.item.ItemConfiguration;

/**
 * Represents the Pack Yak familiar.
 *
 * @author Aero
 */
public class PackYakNPC extends BurdenBeast {

    /**
     * Constructs a new {@code PackYakNPC} {@code Object}.
     */
    public PackYakNPC() {
        this(null, 6873);
    }

    /**
     * Constructs a new {@code PackYakNPC} {@code Object}.
     *
     * @param owner The owner.
     * @param id    The id.
     */
    public PackYakNPC(Player owner, int id) {
        super(owner, id, 5800, 12093, 12, 30);
    }

    @Override
    public Familiar construct(Player owner, int id) {
        return new PackYakNPC(owner, id);
    }

    @Override
    protected boolean specialMove(FamiliarSpecial familiarSpecial) {
        Player player = owner;
        Item item = new Item(familiarSpecial.getItem().getId(), 1);
        if (item.getId() == SummoningScroll.WINTER_STORAGE_SCROLL.getItemId()) {
            return false;
        }
        if (!item.getDefinition().getConfiguration(ItemConfiguration.BANKABLE, true)) {
            player.getActionSender().sendMessage("A magical force prevents you from banking this item");
            return false;
        }
        if (!item.getDefinition().isUnnoted() || !player.getBank().hasRoomFor(item)) {
            player.getActionSender().sendMessage("The pack yak can't send that item to your bank.");
            return false;
        }
        if (player.getInventory().contains(item)) {
            if (player.getInventory().remove(item) && player.getBank().add(item)) {
                graphics(Graphics.create(1358));
                player.getActionSender().sendMessage("The pack yak has sent an item to your bank.");
                return true;
            }
            player.getActionSender().sendMessage("The pack yak can't send that item to your bank.");
            return false;
        }
        return true;
    }

    @Override
    public void visualizeSpecialMove() {
        owner.visualize(Animation.create(7660), Graphics.create(1316));
    }

    @Override
    public int[] getIds() {
        return new int[]{ 6873, 6874 };
    }

    @Override
    protected String getText() {
        return "Baroo!";
    }

}
