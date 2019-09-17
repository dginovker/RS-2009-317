package plugin.skill.magic;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the bones to banana spell.
 * @author Emperor
 * @author 'Vexia
 * @version 1.0
 */
public final class BonesConvertingSpells extends MagicSpell {

    /**
     * The graphic.
     */
    private static final Graphics GRAPHIC = new Graphics(141, 96);

    /**
     * The animation.
     */
    private static final Animation ANIMATION = new Animation(722);

    /**
     * The item to replace the bones with.
     */
    private Item converted;

    /**
     * Constructs a new {@code BonesConvertingSpells} {@code Object}.
     *
     */
    public BonesConvertingSpells() {

    }

    /**
     * Constructs a new {@code BonesConvertingSpells} {@code Object}.
     * @param level The level required.
     * @param converted The item to replace the bones with.
     * @param anim The animation.
     * @param graphic The graphic.
     * @param runes The runes.
     */
    public BonesConvertingSpells(int level, final double experience, Item converted, Animation anim, Graphics graphic, Item... runes) {
        super(SpellBook.MODERN, level, experience, anim, graphic, null, runes);
        this.converted = converted;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(9, new BonesConvertingSpells(15, 25, new Item(1963), ANIMATION, GRAPHIC, Runes.EARTH_RUNE.getItem(2), Runes.WATER_RUNE.getItem(2), Runes.NATURE_RUNE.getItem(1)));
        SpellBook.MODERN.register(40, new BonesConvertingSpells(60, 65, new Item(6883), ANIMATION, GRAPHIC, Runes.EARTH_RUNE.getItem(4), Runes.WATER_RUNE.getItem(4), Runes.NATURE_RUNE.getItem(2)));
        return this;
    }

    @Override
    public boolean meetsRequirements(Entity caster, boolean message, boolean remove) {
        if (!((Player) caster).getInventory().contains(526, 1) && !((Player) caster).getInventory().contains(532, 1)) {
            if (message) {
                ((Player) caster).getActionSender().sendMessage("You do not have enough bones to cast this spell.");
            }
            return false;
        }
        return super.meetsRequirements(caster, message, remove);
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        Player p = (Player) entity;
        if (!p.getAttribute("cbs_tab", false) && !meetsRequirements(entity, true, true)) {
            return false;
        }
        p.removeAttribute("cbs_tab");
        for (Item item : p.getInventory().toArray()) {
            if (item != null) {
                if (item.getId() == 526 || item.getId() == 532) {
                    p.getInventory().replace(new Item(converted.getId()), item.getSlot());
                }
            }
        }
        visualize(entity, target);
        return true;
    }
}
