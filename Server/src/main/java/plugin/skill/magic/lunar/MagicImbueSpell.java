package plugin.skill.magic.lunar;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;

/**
 * The magic imbue spell.
 *
 * @author 'Vexia
 */
public class MagicImbueSpell extends MagicSpell {

    /**
     * The graphic.
     */
    private static final Graphics GRAPHIC = new Graphics(141, 96);

    /**
     * The animation.
     */
    private static final Animation ANIMATION = new Animation(722);

    /**
     * Constructs a new {@code StatRestoreSpell} {@code Object}.
     */
    public MagicImbueSpell() {
        super(SpellBook.LUNAR, 82, 86, null, null, null, new Item[]{ new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.FIRE_RUNE.getId(), 7), new Item(Runes.WATER_RUNE.getId(), 7) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(30202, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = ((Player) entity);
        if (!super.meetsRequirements(player, true, true)) {
            return false;
        }
        player.setAttribute("spell:imbue", true);
        player.graphics(GRAPHIC);
        player.animate(ANIMATION);
        player.getActionSender().sendMessage("You are charged to combine runes!");
        World.submit(new Pulse(20, player) {

            @Override
            public boolean pulse() {
                player.removeAttribute("spell:imbue");
                return true;
            }
        });
        return true;
    }
}
