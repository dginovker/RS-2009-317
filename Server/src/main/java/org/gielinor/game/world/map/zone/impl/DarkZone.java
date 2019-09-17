package org.gielinor.game.world.map.zone.impl;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.global.LightSource;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.Node;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.combat.ImpactHandler.HitsplatType;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.zone.MapZone;
import org.gielinor.game.world.map.zone.RegionZone;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.MinimapStateContext;
import org.gielinor.net.packet.out.MinimapState;
import org.gielinor.rs2.pulse.Pulse;

import plugin.interaction.item.MaxCapePlugin;

/**
 * Handles a dark area.
 *
 * @author Emperor
 */
public final class DarkZone extends MapZone {

    /**
     * The darkness overlay.
     */
    public static final Component DARKNESS_OVERLAY = new Component(257) {

        @Override
        public void open(final Player player) {
            Pulse pulse = player.getExtension(DarkZone.class);
            if (pulse != null && pulse.isRunning()) {
                return;
            }
            pulse = new Pulse(2, player) {

                int count = 0;

                @Override
                public boolean pulse() {
                    if (count == 0) {
                        player.getActionSender().sendMessage("You hear tiny insects skittering over the ground...");
                    } else if (count == 5) {
                        player.getActionSender().sendMessage("Tiny biting insects swarm all over you!");
                    } else if (count > 5) {
                        player.getImpactHandler().manualHit(player, 1, HitsplatType.NORMAL);
                    }
                    count++;
                    return false;
                }
            };
            World.submit(pulse);
            player.addExtension(DarkZone.class, pulse);
            super.open(player);
        }

        @Override
        public boolean close(final Player player) {
            if (!super.close(player)) {
                return false;
            }
            Pulse pulse = player.getExtension(DarkZone.class);
            if (pulse != null) {
                pulse.stop();
            }
            return true;
        }
    };

    /**
     * Constructs a new {@code DarkZone} {@code Object}.
     */
    public DarkZone() {
        super("Dark zone", true);
    }

    @Override
    public void configure() {
        register(new ZoneBorders(1728, 5120, 1791, 5247));
        registerRegion(12693);
        registerRegion(12949);
        // Cave horrors
        registerRegion(14994);
        registerRegion(14995);
        // Lumbridge to Dorgesh-kaan
        register(new ZoneBorders(3221, 9602, 3308, 9659));
        //register(ZoneBorders.forRegion(14994));
    }

    @Override
    public boolean continueAttack(Entity entity, Node target, CombatStyle combatStyle, boolean message) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            return player.getInterfaceState().getOverlay() != DARKNESS_OVERLAY;
        }
        return true;
    }

    @Override
    public boolean interact(Entity entity, Node target, Option option) {
        if (target instanceof Item) {
            Item item = (Item) target;
            LightSource s = LightSource.forProductId(item.getId());
            if (s != null) {
                String name = option.getName().toLowerCase();
                if (name.equals("drop")) {
                    ((Player) entity).getActionSender().sendMessage("Dropping the " + s.getName() + " would leave you without a light source.");
                    return true;
                }
                if (name.equals("extinguish")) {
                    ((Player) entity).getActionSender().sendMessage("Extinguishing the " + s.getName() + " would leave you without a light source.");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean enter(Entity entity) {
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            if (MaxCapePlugin.isWearing(player) || MaxCapePlugin.containsCape(player) || Perk.YOU_ADOPTED_THE_DARKNESS.enabled(player)) {
                player.getInterfaceState().closeOverlay();
                return true;
            }
            LightSource source = LightSource.getActiveLightSource(player);
            if (source == null) {
                if (player.getLocation().getRegionId() == 13206 || player.getLocation().getRegionId() == 12950) {
                    PacketRepository.send(MinimapState.class, new MinimapStateContext(player, 2));
                }
                player.getInterfaceState().openOverlay(DARKNESS_OVERLAY);
            } else if (source.getInterfaceId() > 0) {
                player.getInterfaceState().openOverlay(new Component(source.getInterfaceId()));
            }
        }
        return true;
    }

    @Override
    public boolean leave(Entity entity, boolean logout) {
        if (entity instanceof Player) {
            PacketRepository.send(MinimapState.class, new MinimapStateContext((Player) entity, 0));
            ((Player) entity).getInterfaceState().closeOverlay();
        }
        return true;
    }

    /**
     * Updates the overlay.
     *
     * @param player The player.
     */
    public void updateOverlay(Player player) {
        LightSource source = LightSource.getActiveLightSource(player);
        if (MaxCapePlugin.isWearing(player) || MaxCapePlugin.containsCape(player) || Perk.YOU_ADOPTED_THE_DARKNESS.enabled(player)) {
            player.getInterfaceState().closeOverlay();
            return;
        }
        int overlay = -1;
        if (player.getInterfaceState().getOverlay() != null) {
            overlay = player.getInterfaceState().getOverlay().getId();
        }
        if (source == null) {
            if (overlay != DARKNESS_OVERLAY.getId()) {
                player.getInterfaceState().openOverlay(DARKNESS_OVERLAY);
            }
            return;
        }
        Pulse pulse = player.getExtension(DarkZone.class);
        if (pulse != null) {
            pulse.stop();
        }
        if (source.getInterfaceId() != overlay) {
            if (source.getInterfaceId() == -1) {
                player.getInterfaceState().closeOverlay();
                return;
            }
            player.getInterfaceState().openOverlay(new Component(source.getInterfaceId()));
        }
    }

    /**
     * Checks if the player is in a dark area and will update accordingly.
     *
     * @param p The player.
     */
    public static void checkDarkArea(Player p) {
        for (RegionZone r : p.getZoneMonitor().getZones()) {
            if (r.getZone() instanceof DarkZone) {
                DarkZone zone = (DarkZone) r.getZone();
                zone.updateOverlay(p);
                break;
            }
        }
    }
}
