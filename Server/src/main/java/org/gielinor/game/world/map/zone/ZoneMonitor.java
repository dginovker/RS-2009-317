package org.gielinor.game.world.map.zone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.music.MusicZone;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.config.Constants;

import org.gielinor.rs2.task.NodeTask;
import plugin.activity.duelarena.DuelRule;
import plugin.activity.duelarena.DuelSession;

/**
 * Handles the zones for an entity.
 *
 * @author Emperor
 */
public final class ZoneMonitor {

    /**
     * The entity.
     */
    private final Entity entity;

    /**
     * The currently entered zones.
     */
    private final List<RegionZone> zones = new ArrayList<>();

    /**
     * The currently entered music zones.
     */
    private final List<MusicZone> musicZones = new ArrayList<>();

    /**
     * Constructs a new {@code ZoneMonitor} {@code Object}.
     *
     * @param entity The entity.
     */
    public ZoneMonitor(Entity entity) {
        this.entity = Objects.requireNonNull(entity);
    }

    /**
     * Gets the zone type.
     *
     * @return The zone type.
     */
    public int getType() {
        for (RegionZone zone : zones) {
            if (zone.getZone().getZoneType() != 0) {
                return zone.getZone().getZoneType();
            }
        }
        return 0;
    }

    /**
     * Checks if the player can logout.
     *
     * @return <code>True</code> if so.
     */
    public boolean canLogout() {
        for (RegionZone z : zones) {
            if (!z.getZone().canLogout((Player) entity)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the restriction was flagged.
     *
     * @param restriction The restriction flag.
     * @return <code>True</code> if so.
     */
    public boolean isRestricted(ZoneRestriction restriction) {
        return isRestricted(restriction.getFlag());
    }

    /**
     * Checks if the restriction was flagged.
     *
     * @param flag The restriction flag.
     * @return <code>True</code> if so.
     */
    public boolean isRestricted(int flag) {
        for (RegionZone z : zones) {
            if (z.getZone().isRestricted(flag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles a death.
     *
     * @param killer The killer.
     * @return <code>True</code> if the death got handled.
     */
    public boolean handleDeath(Entity killer) {
        for (RegionZone z : zones) {
            if (z.getZone().death(entity, killer)) {
                return true;
            }
        }
        return false;
    }

    public NodeTask deathTaskOverride(Entity killer) {
        for (RegionZone zone : zones) {
            NodeTask deathTask = zone.getZone().overrideDeathTask(entity, killer);
            if (deathTask != null) {
                return deathTask;
            }
        }
        return null;
    }

    /**
     * Checks if the entity is able to continue attacking the target.
     *
     * @param target   The target.
     * @param style    The combat style used.
     * @param messages Send messages?
     * @return <code>True</code> if so.
     */
    public boolean continueAttack(Node target, CombatStyle style, boolean messages) {
        for (RegionZone z : zones) {
            if (!z.getZone().continueAttack(entity, target, style, true)) {
                if (entity instanceof Player) {
                    if (((Player) entity).getFamiliarManager().hasFamiliar()) {
                        ((Player) entity).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                    }
                }
                if (target instanceof Player) {
                    if (((Player) target).getFamiliarManager().hasFamiliar()) {
                        ((Player) target).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                    }
                }
                return false;
            }
        }
        if (entity instanceof Player && target instanceof Player) {
            if (!((Player) entity).getSkullManager().isWilderness() || !((Player) target).getSkullManager().isWilderness()) {
                if (((Player) entity).specialDetails()) {
                    return true;
                }
                if (messages) {
                    ((Player) entity).getActionSender().sendMessage("You can only attack other players in the wilderness.");
                }
                if (((Player) entity).getFamiliarManager().hasFamiliar()) {
                    ((Player) entity).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                }
                if (((Player) target).getFamiliarManager().hasFamiliar()) {
                    ((Player) target).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                }
                return false;
            }
            Player player = (Player) entity;
            if (player.getSkullManager().isWilderness() || ((Player) target).getSkullManager().isWilderness()) {
                if (!player.canInteractWith((Player) target)) {
                    if (!player.specialDetails() || !((Player) target).specialDetails()) {
                        if (messages) {
                            player.getActionSender().sendMessage("You cannot attack this player.");
                        }
                        if (((Player) entity).getFamiliarManager().hasFamiliar()) {
                            ((Player) entity).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                        }
                        if (((Player) target).getFamiliarManager().hasFamiliar()) {
                            ((Player) target).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                        }
                        return false;
                    }
                }
            }
        }
        if (entity instanceof Player) {
            if (DuelSession.getExtension(entity.asPlayer()) != null) {
                switch (style) {
                    case RANGE:
                        if (DuelRule.NO_RANGE.enforce(entity.asPlayer(), messages)) {
                            return false;
                        }
                        break;
                    case MELEE:
                        if (DuelRule.NO_MELEE.enforce(entity.asPlayer(), messages)) {
                            return false;
                        }
                        break;
                    case MAGIC:
                        if (DuelRule.NO_MAGIC.enforce(entity.asPlayer(), messages)) {
                            return false;
                        }
                        break;
                }
            }
        }
        if (target instanceof Entity && !MapZone.checkMulti(entity, (Entity) target, messages)) {
            if (entity instanceof Player) {
                if (((Player) entity).getFamiliarManager().hasFamiliar()) {
                    ((Player) entity).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                }
            }
            if (target instanceof Player) {
                if (((Player) target).getFamiliarManager().hasFamiliar()) {
                    ((Player) target).getFamiliarManager().getFamiliar().getProperties().getCombatPulse().stop();
                }
            }
            return false;
        }
        return !(target instanceof Entity && !MapZone.checkMulti(entity, (Entity) target, messages));
    }

    /**
     * Checks if multiway combat zone rules should be ignored.
     *
     * @param victim The victim.
     * @return <code>True</code> if this entity can attack regardless of multiway combat zone.
     */
    public boolean isIgnoreMultiBoundaries(Entity victim) {
        for (RegionZone z : zones) {
            if (z.getZone().ignoreMultiBoundaries(entity, victim)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entity can interact with the target.
     *
     * @param target The target to interact with.
     * @param option The option.
     * @return <code>True</code> if the option got handled.
     */
    public boolean interact(Node target, Option option) {
        for (RegionZone z : zones) {
            if (z.getZone().interact(entity, target, option)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player handled the action button using a map zone.
     *
     * @param interfaceId The interface id.
     * @param buttonId    The button id.
     * @param slot        The slot.
     * @param itemId      The item id.
     * @param opcode      The packet opcode.
     * @return <code>True</code> if the button got handled.
     */
    public boolean clickButton(int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        for (RegionZone z : zones) {
            if (z.getZone().actionButton((Player) entity, interfaceId, buttonId, slot, itemId, opcode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entity can teleport.
     *
     * @param type The teleport type (0=spell, 1=item, 2=object, 3=npc -1= force)
     * @param node TODO
     * @return <code>True</code> if so.
     */
    public boolean teleport(int type, Node node) {
        for (RegionZone z : zones) {
            if (!z.getZone().teleport(entity, type, node)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the entity can fire a random event.
     *
     * @return <code>True</code> if so.
     */
    public boolean canFireRandomEvent() {
        for (RegionZone z : zones) {
            if (!z.getZone().isFireRandoms()) {
                return false;
            }
        }
        return true;
    }

    public boolean canUsePrayer() {
        for (RegionZone z : zones) {
            if (!z.getZone().canUsePrayer()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the zones.
     *
     * @return <code>True</code> if the entity successfully left all regions.
     */
    public boolean clear() {
        for (RegionZone z : zones) {
            if (!z.getZone().leave(entity, true)) {
                return false;
            }
        }
        for (MusicZone z : musicZones) {
            z.leave(entity, true);
        }
        zones.clear();
        musicZones.clear();
        return true;
    }

    /**
     * Checks if the entity can move.
     *
     * @param location    The destination location.
     * @param destination The destination location.
     * @return <code>True</code> if so.
     */
    public boolean move(Location location, Location destination) {
        for (RegionZone z : zones) {
            if (!z.getZone().move(entity, location, destination)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles a location update.
     *
     * @param last The last location of the entity.
     * @return <code>False</code> If the entity could not enter/leave a region.
     */
    public boolean updateLocation(Location last) {
        Location l = entity.getLocation();
        checkMusicZones();
        entity.updateLocation(last);
        for (Iterator<RegionZone> it = zones.iterator(); it.hasNext(); ) {
            RegionZone zone = it.next();
            if (!zone.getBorders().insideBorder(l.getX(), l.getY())) {
                if (zone.getZone().isDynamicZone()) {
                    continue;
                }
                if (!zone.getZone().leave(entity, false)) {
                    return false;
                }
                it.remove();
            }
        }
        for (RegionZone zone : entity.getViewport().getRegion().getRegionZones()) {
            if (!zone.getBorders().insideBorder(l.getX(), l.getY())) {
                continue;
            }
            boolean alreadyEntered = false;
            for (RegionZone z : zones) {
                if (z.getZone() == zone.getZone()) {
                    alreadyEntered = true;
                    break;
                }
            }
            if (alreadyEntered) {
                zone.getZone().locationUpdate(entity, last);
                continue;
            }

            if (!zone.getZone().enter(entity)) {
                return false;
            }
            zones.add(zone);
            zone.getZone().locationUpdate(entity, last);
        }
        return true;
    }

    /**
     * Checks the music zones.
     */
    public void checkMusicZones() {
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        Location l = player.getLocation();
        for (Iterator<MusicZone> it = musicZones.iterator(); it.hasNext(); ) {
            MusicZone zone = it.next();
            if (!zone.getBorders().insideBorder(l.getX(), l.getY())) {
                zone.leave(player, false);
                it.remove();
            }
        }
        for (MusicZone zone : player.getViewport().getRegion().getMusicZones()) {
            if (!zone.getBorders().insideBorder(l.getX(), l.getY())) {
                continue;
            }
            if (!musicZones.contains(zone)) {
                zone.enter(player);
                musicZones.add(zone);
            }
        }
        if (musicZones.isEmpty() && !player.getMusicPlayer().isPlaying()) {
            player.getMusicPlayer().playDefault();
        }
    }

    /**
     * Checks if the entity is in a zone.
     *
     * @param name The name of the zone.
     * @return <code>True</code> if so.
     */
    public boolean isInZone(String name) {
        int uid = name.hashCode();
        for (RegionZone zone : zones) {
            if (zone.getZone().getUid() == uid) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the proper region zone for the given map zone.
     *
     * @param zone The map zone.
     */
    public void remove(MapZone zone) {
        for (Iterator<RegionZone> it = zones.iterator(); it.hasNext(); ) {
            if (it.next().getZone() == zone) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Gets the zones list.
     *
     * @return The list of region zones the entity is in.
     */
    public List<RegionZone> getZones() {
        return zones;
    }

    /**
     * Gets the musicZones.
     *
     * @return The musicZones.
     */
    public List<MusicZone> getMusicZones() {
        return musicZones;
    }

}
