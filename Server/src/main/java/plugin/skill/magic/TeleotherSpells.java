package plugin.skill.magic;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.global.travel.Teleport.TeleportType;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.magic.Runes;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.equipment.SpellType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager.SpellBook;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;

/**
 * Handles the teleport other spells.
 *
 * @author Emperor
 */
public final class TeleotherSpells extends MagicSpell {

    /**
     * The destination.
     */
    private String destination;

    /**
     * The destination location.
     */
    private Location location;

    /**
     * Constructs a new {@code TeleotherSpells} {@code Object}.
     */
    public TeleotherSpells() {
        /*
         * empty.
         */
    }

    /**
     * Constructs a new {@code TeleotherSpells} {@code Object}.
     *
     * @param level       The level required.
     * @param destination The destination name.
     * @param location    The location to teleport to.
     * @param runes       The runes required.
     */
    public TeleotherSpells(int level, double experience, String destination, Location location, Item... runes) {
        super(SpellBook.MODERN, level, experience, Animation.create(1818), Graphics.create(343), new Audio(199, 0, 0), runes);
        this.destination = destination;
        this.location = location;
    }

    @Override
    public Plugin<SpellType> newInstance(SpellType arg) throws Throwable {
        ComponentDefinition.put(12468, new ComponentPlugin() {

            @Override
            public Plugin<Object> newInstance(Object arg) throws Throwable {
                return this;
            }

            @Override
            public boolean handle(Player player, Component component, int opcode, int button, int slot, int itemId) {
                switch (button) {
                    case 12566:
                        player.lock(2);
                        if (player.getTeleporter().send(player.getAttribute("t-o_location", player.getLocation()), TeleportType.TELE_OTHER)) {
                            player.visualize(Animation.create(1816), Graphics.create(342));
                        }
                        player.getInterfaceState().close();
                        return true;
                    case 12568:
                        player.getInterfaceState().close();
                        return true;
                }
                return false;
            }
        });
        SpellBook.MODERN.register(12425, new TeleotherSpells(74, 84, "Lumbridge", Location.create(3222, 3217, 0), Runes.SOUL_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1), Runes.EARTH_RUNE.getItem(1)));
        SpellBook.MODERN.register(12435, new TeleotherSpells(82, 92, "Falador", Location.create(2965, 3378, 0), Runes.SOUL_RUNE.getItem(1), Runes.LAW_RUNE.getItem(1), Runes.WATER_RUNE.getItem(1)));
        SpellBook.MODERN.register(12455, new TeleotherSpells(90, 100, "Camelot", Location.create(2758, 3478, 0), Runes.SOUL_RUNE.getItem(2), Runes.LAW_RUNE.getItem(1)));
        return this;
    }

    @Override
    public boolean cast(Entity entity, Node target) {
        Player p = (Player) entity;
        if (!(target instanceof Player)) {
            p.getActionSender().sendMessage("You can only cast this spell on other players.");
            return false;
        }
        if (!entity.getZoneMonitor().teleport(0, null)) {
            return false;
        }
        Player o = (Player) target;
        if (!o.isActive() || o.getLocks().isTeleportLocked() || o.getInterfaceState().isOpened()) {
            p.getActionSender().sendMessage("The other player is currently busy.");
            return false;
        }
        if (!o.getSettings().isAcceptAid()) {
            if (!((Player) entity).specialDetails()) {
                p.getActionSender().sendMessage(o.getUsername() + " is not accepting aid.");
                return false;
            }
        }
        if (!meetsRequirements(entity, true, true)) {
            return false;
        }
        visualize(entity, target);
        p.faceLocation(o.getLocation());
        o.setAttribute("t-o_location", location);
        o.getActionSender().sendString(p.getUsername(), 12558);
        o.getActionSender().sendString(destination, 12560);
        o.getInterfaceState().open(new Component(12468));
        return true;
    }

}
