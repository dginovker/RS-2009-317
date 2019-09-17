package plugin.skill.magic;

import org.gielinor.game.content.global.travel.Teleport.TeleportType;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.GameConstants;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin to handle all teleport spells in the modern book.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class ModernTeleportPlugin extends MagicSpell {

    /**
     * Represents the location to teleport to.
     */
    private Location location;

    /**
     * Constructs a new {@code ModernTeleportPlugin} {@code Object}.
     */
    public ModernTeleportPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code ModernTeleportPlugin.java} {@code Object}.
     *
     * @param level      the level.
     * @param experience the experience.
     * @param items      the items.
     */
    public ModernTeleportPlugin(final int level, final double experience, final Location location, final Item... items) {
        super(SpellBook.MODERN, level, experience, null, null, null, items);
        this.location = location;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (entity instanceof Player) {
            ((Player) entity).getInterfaceState().close();
        }
        if (!super.meetsRequirements(entity, true, false)) {
            return false;
        }
        if (entity.getAttribute("teleport-delay", 0) > World.getTicks()) {
            return false;
        }
        if (entity.getTeleporter().send(location.transform(0, RandomUtil.random(3), 0), getSpellId() == 0 ? TeleportType.HOME : TeleportType.NORMAL)) {
            if (!super.meetsRequirements(entity, true, true)) {
                entity.getTeleporter().getCurrentTeleport().stop();
                return false;
            }
            entity.setAttribute("teleport-delay", World.getTicks() + 6);
            return true;
        }
        return false;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        //home
        SpellBook.MODERN.register(19210, new ModernTeleportPlugin(0, 0, GameConstants.HOME_LOCATION));
        //varrock
        SpellBook.MODERN.register(1164, new ModernTeleportPlugin(0, 0, Location.create(3213, 3424, 0)));
        //lumby
        SpellBook.MODERN.register(1167, new ModernTeleportPlugin(0, 0, Location.create(3222, 3218, 0)));
        //fally
        SpellBook.MODERN.register(1170, new ModernTeleportPlugin(0, 0, Location.create(2965, 3378, 0)));
        //house
        //SpellBook.MODERN.register(23, new ModernTeleportPlugin(40, 50, GameConstants.HOME_LOCATION, new Item(Runes.LAW_RUNE.getId()), new Item(Runes.AIR_RUNE.getId(), 1), new Item(Runes.EARTH_RUNE.getId(), 1)));
        //camelot
        SpellBook.MODERN.register(1174, new ModernTeleportPlugin(0, 0, Location.create(2758, 3478, 0)));
        //ardougne
        SpellBook.MODERN.register(1540, new ModernTeleportPlugin(0, 0, Location.create(2662, 3307, 0)));
        //watchtower
        SpellBook.MODERN.register(1541, new ModernTeleportPlugin(58, 68, Location.create(2549, 3112, 0), new Item(Runes.EARTH_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2)));
        //trollheim
        SpellBook.MODERN.register(7455, new ModernTeleportPlugin(61, 71, Location.create(2891, 3678, 0), new Item(Runes.FIRE_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2)));
        //ape atoll
        SpellBook.MODERN.register(18470, new ModernTeleportPlugin(64, 74, Location.create(2754, 2784, 0), new Item(Runes.FIRE_RUNE.getId(), 2), new Item(Runes.WATER_RUNE.getId(), 2), new Item(Runes.LAW_RUNE.getId(), 2), new Item(1963, 1)));
        return this;
    }

}