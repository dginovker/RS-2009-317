package plugin.skill.magic;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Represents the the charging orb magic spell.
 * @author Emperor
 * @version 1.0
 */
public final class ChargeOrbSpells extends MagicSpell {

    /**
     * The animation.
     */
    private static final Animation ANIMATION = Animation.create(791);

    /**
     * The unpowered orb item.
     */
    private static final Item UNPOWERED_ORB = new Item(567);

    /**
     * The object id.
     */
    private int objectId;

    /**
     * The item id.
     */
    private int itemId;

    /**
     * Constructs a new {@code ChargeOrbSpells} {@code Object}
     */
    public ChargeOrbSpells() {
        /*
         * empty.
         */
    }

    /**
     * Constructs a new {@code ChargeOrbSpells} {@code Object}.
     * @param level The level required.
     * @param objectId The object id.
     * @param itemId The item to add.
     * @param g The graphics.
     * @param runes The runes required.
     */
    public ChargeOrbSpells(int level, int objectId, int itemId, Graphics g, Item... runes) {
        super(SpellBook.MODERN, level, level + 10, ANIMATION, g, null, runes);
        this.objectId = objectId;
        this.itemId = itemId;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        SpellBook.MODERN.register(1179, new ChargeOrbSpells(56, 2151, 571, new Graphics(149, 96), Runes.WATER_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
        SpellBook.MODERN.register(1182, new ChargeOrbSpells(60, 29415, 575, new Graphics(151, 96), Runes.EARTH_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
        SpellBook.MODERN.register(1184, new ChargeOrbSpells(63, 2153, 569, new Graphics(152, 96), Runes.FIRE_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
        SpellBook.MODERN.register(1186, new ChargeOrbSpells(66, 2152, 573, new Graphics(150, 96), Runes.AIR_RUNE.getItem(30), Runes.COSMIC_RUNE.getItem(3), UNPOWERED_ORB));
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (!(target instanceof GameObject)) {
            return false;
        }
        Player p = (Player) entity;
        GameObject obj = (GameObject) target;
        if (!meetsRequirements(entity, true, true)) {
            return false;
        }
        if (obj == null || obj.getId() != objectId) {
            p.getActionSender().sendMessage("You can't cast this spell on this object!");
            return false;
        }
        if (obj.getLocation().getDistance(p.getLocation()) > 3) {
            p.getActionSender().sendMessage("You're standing too far from the obelisk's reach.");
            return false;
        }
        p.faceLocation(obj.getLocation());
        visualize(p, target);
        p.getInventory().add(new Item(itemId));
        return true;
    }

}
