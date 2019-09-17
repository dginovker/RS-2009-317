package plugin.skill.magic.ancient;

import org.gielinor.game.content.global.travel.Teleport.TeleportType;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
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
 * Represents the plugin used to handle all ancient teleporting plugins.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class AncientTeleportPlugin extends MagicSpell {

    /**
     * Represents the location to teleport to.
     */
    private Location location;

    /**
     * Constructs a new {@code AncientTeleportPlugin} {@code Object}.
     */
    public AncientTeleportPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code AncientTeleportPlugin.java} {@code Object}.
     *
     * @param level      the level.
     * @param experience the experience.
     * @param items      the items.
     */
    public AncientTeleportPlugin(final int level, final double experience, final Location location, final Item... items) {
        super(SpellBook.ANCIENT, level, experience, null, null, null, items);
        this.location = location;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        if (entity instanceof Player) {
            ((Player) entity).getInterfaceState().close();
        }
        if (entity.getAttribute("teleport-delay", 0) > World.getTicks()) {
            return false;
        }
        if (super.meetsRequirements(entity, true, true)) {
            entity.getTeleporter().send(location.transform(0, RandomUtil.random(3), 0), getSpellId() == 24 ? TeleportType.HOME : TeleportType.ANCIENT);
            entity.setAttribute("teleport-delay", World.getTicks() + 6);
            return true;
        }
        return false;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        //home
        SpellBook.ANCIENT.register(21741, new AncientTeleportPlugin(0, 0, GameConstants.HOME_LOCATION));
        //paddewwa teleport
        SpellBook.ANCIENT.register(13035, new AncientTeleportPlugin(54, 64, Location.create(3098, 9882, 0)));
        //sennisten teleport
        SpellBook.ANCIENT.register(13045, new AncientTeleportPlugin(60, 70, Location.create(3320, 3338, 0)));
        //karyll teleport
        SpellBook.ANCIENT.register(13053, new AncientTeleportPlugin(66, 76, Location.create(3493, 3472, 0)));
        //lassar teleport
        SpellBook.ANCIENT.register(13061, new AncientTeleportPlugin(72, 82, Location.create(3003, 3470, 0)));
        //dareeyak teleport
        SpellBook.ANCIENT.register(13069, new AncientTeleportPlugin(78, 88, Location.create(2966, 3696, 0)));
        //carralangar teleport
        SpellBook.ANCIENT.register(13079, new AncientTeleportPlugin(84, 82, Location.create(3163, 3664, 0)));
        //annakarl teleport
        SpellBook.ANCIENT.register(13087, new AncientTeleportPlugin(90, 100, Location.create(3287, 3883, 0)));
        //ghorock teleport
        SpellBook.ANCIENT.register(13095, new AncientTeleportPlugin(96, 106, Location.create(2972, 3873, 0)));
        return this;
    }

}
