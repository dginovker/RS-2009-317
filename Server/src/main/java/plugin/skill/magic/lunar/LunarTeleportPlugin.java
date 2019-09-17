package plugin.skill.magic.lunar;

import org.gielinor.game.component.Component;
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
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.utilities.misc.RandomUtil;

/**
 * Represents the plugin to handle all teleport spells.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class LunarTeleportPlugin extends MagicSpell {
    /**
     *
     */

    /**
     * Represents the component used for group spells.
     */
    private static final Component COMPONENT = new Component(12468);

    /**
     * Represents the location to teleport to.
     */
    private Location location;

    /**
     * Represents if it's a group teleport.
     */
    private boolean group;

    /**
     * Constructs a new {@code LunarTeleportPlugin} {@code Object}.
     */
    public LunarTeleportPlugin() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code LunarTeleportPlugin} {@code Object}.
     *
     * @param level
     *            the level.
     * @param experience
     *            the experience.
     * @param items
     *            the items.
     */
    public LunarTeleportPlugin(final int level, final double experience, final Location location, boolean group,
        final Item... items) {
        super(SpellBook.LUNAR, level, experience, null, null, null, items);
        this.location = location;
        this.group = group;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        final Player player = (Player) entity;
        if (entity != null) {
            ((Player) entity).getInterfaceState().close();
        }
        if (!meetsRequirements(player, true, true)) {
            return false;
        }
        if (entity.getAttribute("teleport-delay", 0) > World.getTicks()) {
            return false;
        }
        entity.setAttribute("teleport-delay", World.getTicks() + 6);
        player.getTeleporter().send(location.transform(0, RandomUtil.random(3), 0),
            getSpellId() == 16 ? TeleportType.HOME : TeleportType.LUNAR);
        if (group) {
            String destination = "Moonclan Island";
            switch (getSpellId()) {
                case 34:
                    destination = "Waterbirth Island";
                    break;
                case 35:
                    destination = "Barbarian outpost";
                    break;
                case 36:
                    destination = "Port Khazard";
                    break;
                case 37:
                    destination = "Fishing guild";
                    break;
                case 38:
                    destination = "Catherby";
                    break;
                case 39:
                    destination = "Ice plateau";
                    break;
            }
            for (Player p : RegionManager.getLocalPlayers(player, 1)) {
                if (p == player) {
                    continue;
                }
                if (!p.isActive() || p.getLocks().isTeleportLocked()) {
                    player.getActionSender().sendMessage("The other player is currently busy.");
                    continue;
                }
                if (!p.getSettings().isAcceptAid()) {
                    if (!((Player) entity).specialDetails()) {
                        player.getActionSender().sendMessage("The player is not accepting any aid.");
                        continue;
                    }
                }
                visualize(entity, p);
                p.setAttribute("t-o_location", location);
                p.getActionSender().sendString(player.getUsername(), 12558);
                p.getActionSender().sendString(destination, 12560);
                p.getInterfaceState().open(COMPONENT);
            }
        }
        return true;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        // home
        //SpellBook.LUNAR.register(30000, new LunarTeleportPlugin(0, 0, Location.create(2100, 3914, 0), false));
        SpellBook.LUNAR.register(30000, new LunarTeleportPlugin(0, 0, GameConstants.HOME_LOCATION, false));
        // moonclan teleport
        SpellBook.LUNAR.register(30064,
            new LunarTeleportPlugin(69, 66, Location.create(2111, 3916, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.EARTH_RUNE.getId(), 2)));
        // moonclan group teleport
        SpellBook.LUNAR.register(30075,
            new LunarTeleportPlugin(70, 67, Location.create(2111, 3916, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.EARTH_RUNE.getId(), 4)));
        // ourina teleport
        SpellBook.LUNAR.register(30083,
            new LunarTeleportPlugin(71, 69, Location.create(2469, 3247, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.EARTH_RUNE.getId(), 6)));
        // waterbirth teleport
        SpellBook.LUNAR.register(30106,
            new LunarTeleportPlugin(72, 71, Location.create(2527, 3739, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.WATER_RUNE.getId(), 1)));
        // waterbirth group teleport
        SpellBook.LUNAR.register(30114,
            new LunarTeleportPlugin(73, 72, Location.create(2527, 3739, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 1), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.WATER_RUNE.getId(), 5)));
        // barbarian post teleport.
        SpellBook.LUNAR.register(30138,
            new LunarTeleportPlugin(75, 76, Location.create(2544, 3572, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.FIRE_RUNE.getId(), 3)));
        // barbarian group teleport
        SpellBook.LUNAR.register(30146,
            new LunarTeleportPlugin(77, 77, Location.create(2544, 3572, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.FIRE_RUNE.getId(), 6)));
        // khzard teleport
        SpellBook.LUNAR.register(30162,
            new LunarTeleportPlugin(78, 80, Location.create(2656, 3157, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.WATER_RUNE.getId(), 4)));
        // khzard group teleport
        SpellBook.LUNAR.register(30170,
            new LunarTeleportPlugin(79, 81, Location.create(2656, 3157, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 2), new Item(Runes.ASTRAL_RUNE.getId(), 2),
                new Item(Runes.WATER_RUNE.getId(), 8)));
        // fishing guild teleport
        SpellBook.LUNAR.register(30226,
            new LunarTeleportPlugin(85, 89, Location.create(2611, 3393, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3),
                new Item(Runes.WATER_RUNE.getId(), 10)));
        // fishing guild group teleport
        SpellBook.LUNAR.register(30234,
            new LunarTeleportPlugin(86, 90, Location.create(2611, 3393, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3),
                new Item(Runes.WATER_RUNE.getId(), 14)));
        // catherby teleport
        SpellBook.LUNAR.register(30250,
            new LunarTeleportPlugin(87, 92, Location.create(2804, 3433, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3),
                new Item(Runes.WATER_RUNE.getId(), 10)));
        // catherby group teleport
        SpellBook.LUNAR.register(30258,
            new LunarTeleportPlugin(88, 93, Location.create(2804, 3433, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3),
                new Item(Runes.WATER_RUNE.getId(), 12)));
        // ice plateua teleport
        SpellBook.LUNAR.register(30266,
            new LunarTeleportPlugin(89, 96, Location.create(2972, 3873, 0), false,
                new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3),
                new Item(Runes.WATER_RUNE.getId(), 8)));
        // ice plateua group teleport
        SpellBook.LUNAR.register(30274,
            new LunarTeleportPlugin(90, 99, Location.create(2972, 3873, 0), true,
                new Item(Runes.LAW_RUNE.getId(), 3), new Item(Runes.ASTRAL_RUNE.getId(), 3),
                new Item(Runes.WATER_RUNE.getId(), 16)));
        return this;
    }

}
