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
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * The cure group spell.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CureGroupSpell extends MagicSpell {

    /**
     * Represents the animation of this graphics.
     */
    private static final Animation ANIMATION = new Animation(4409);

    /**
     * Represents the graphic to use.
     */
    private static final Graphics GRAPHIC = new Graphics(751, 130);

    /**
     * Constructs a new {@code CureGroupSpell} {@code Object}.
     */
    public CureGroupSpell() {
        super(SpellBook.LUNAR, 74, 74, ANIMATION, null, null, new Item[]{ new Item(Runes.ASTRAL_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.COSMIC_RUNE.getId(), 2) });
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.LUNAR.register(30122, this);
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        Player p = (Player) entity;
        if (!meetsRequirements(entity, true, true)) {
            return false;
        }
        p.animate(ANIMATION);
        p.graphics(GRAPHIC);
        p.getStateManager().remove(EntityState.POISONED);
        for (Player otherPlayer : RegionManager.getLocalPlayers(p, 1)) {
            Player o = otherPlayer;
            if (!o.isActive() || o.getLocks().isInteractionLocked()) {
                continue;
            }
            if (!o.getSettings().isAcceptAid()) {
                if (!((Player) entity).specialDetails()) {
                    continue;
                }
            }
            o.getStateManager().remove(EntityState.POISONED);
            o.graphics(GRAPHIC);
        }
        return true;
    }

}
