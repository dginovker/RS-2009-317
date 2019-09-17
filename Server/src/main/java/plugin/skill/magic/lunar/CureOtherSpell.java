package plugin.skill.magic.lunar;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.entity.state.EntityState;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The cure other spell.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CureOtherSpell extends MagicSpell {

    /**
     * Represents the animation of this graphics.
     */
    private static final Animation ANIMATION = new Animation(4411);

    /**
     * Repesents the graphick, next spells of this spell.
     */
    private static final Graphics GRAPHIC = new Graphics(738, 130);

    /**
     * Constructs a new {@code CureOtherSpell} {@code Object}.
     */
    public CureOtherSpell() {
        super(SpellBook.LUNAR, 68, 65, ANIMATION, null, null, new Item[]{ new Item(Runes.ASTRAL_RUNE.getId(), 1), new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 10) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(30048, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        Player p = (Player) entity;
        if (!(target instanceof Player)) {
            p.getActionSender().sendMessage("You can only cast this spell on other players.");
            return false;
        }
        Player o = (Player) target;
        if (!o.isActive() || o.getLocks().isInteractionLocked()) {
            p.getActionSender().sendMessage("This player is busy.");
            return false;
        }
        if (!o.getSettings().isAcceptAid()) {
            if (!((Player) entity).specialDetails()) {
                p.getActionSender().sendMessage("The player is not accepting any aid.");
                return false;
            }
        }
        if (!o.getStateManager().hasState(EntityState.POISONED)) {
            p.getActionSender().sendMessage("This player is not poisoned.");
            return false;
        }
        if (!meetsRequirements(entity, true, true)) {
            return false;
        }
        p.face(o);
        p.animate(ANIMATION);
        o.graphics(GRAPHIC);
        o.getStateManager().remove(EntityState.POISONED);
        return true;
    }

}
