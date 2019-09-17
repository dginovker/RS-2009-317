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
 * Represents the plugin for the magic spell alchemy.
 *
 * @author Emperor
 * @author 'Vexia
 * @version 1.0
 */
public final class AlchemySpell extends MagicSpell {

    /**
     * If the spell is high alchemy.
     */
    private boolean highAlchemy;

    /**
     * Constructs a new {@code AlchemySpell} {@code Object}.
     */
    public AlchemySpell() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code AlchemySpell} {@code Object}.
     *
     * @param level       The required level.
     * @param anim        the animation.
     * @param graphic     The graphic.
     * @param highAlchemy If this spell is high alchemy.
     * @param runes       The runes required.
     */
    public AlchemySpell(int level, final double experience, Animation anim, Graphics graphic, boolean highAlchemy, Item... runes) {
        super(SpellBook.MODERN, level, experience, anim, graphic, null, runes);
        this.highAlchemy = highAlchemy;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(1162, new AlchemySpell(21, 31, Animation.create(712), new Graphics(112, 96), false, Runes.FIRE_RUNE.getItem(3), Runes.NATURE_RUNE.getItem(1)));
        SpellBook.MODERN.register(1178, new AlchemySpell(55, 65, Animation.create(713), new Graphics(113, 96), true, Runes.FIRE_RUNE.getItem(5), Runes.NATURE_RUNE.getItem(1)));
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        Item item = (Item) target;
        Player p = (Player) entity;
        p.getInterfaceState().setViewedTab(6);
        if (entity.inCombat()) {
            p.getActionSender().sendMessage("You can't do that during combat.");
            return false;
        }
        if (item == null || p.getInventory().get(item.getSlot()) != item) {
            return false;
        }
        if (item.getId() == Item.COINS || !item.getDefinition().isTradeable()) {
            p.getActionSender().sendMessage("You cannot convert this item to gold.");
            return false;
        }
        Item coins = new Item(Item.COINS, item.getDefinition().getAlchemyValue(highAlchemy));
        if (coins.getCount() > 1 && !p.getInventory().hasRoomFor(coins)) {
            p.getActionSender().sendMessage("Not enough space in your inventory!");
            return false;
        }
        if (!meetsRequirements(p, true, true)) {
            return false;
        }
        p.lock(3);
        visualize(p, target);
        if (p.getInventory().remove(new Item(item.getId(), 1))) {
            p.getAudioManager().send(highAlchemy ? 97 : 98);
            if (coins.getCount() != 0) {
                p.getInventory().add(coins);
            }
        }
        return true;
    }

}
