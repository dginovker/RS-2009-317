package org.gielinor.game.content.global.action;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles digging with a spade.
 * @author Emperor
 */
public final class DigSpadeHandler {

    private static final Logger log = LoggerFactory.getLogger(DigSpadeHandler.class);

    /**
     * The digging actions.
     */
    private static final Map<Location, DigAction> actions = new HashMap<>();

    /**
     * The digging animation.
     */
    public static final Animation ANIMATION = Animation.create(830);

    /**
     * Handles a digging action.
     * @param player The player.
     * @return <code>True</code> if the action got handled.
     */
    public static boolean dig(final Player player) {
        final DigAction action = actions.get(player.getLocation());
        player.animate(ANIMATION);
        player.lock(1);
        if (action != null) {
            World.submit(new Pulse(1, player) {

                @Override
                public boolean pulse() {
                    action.run(player);
                    return true;
                }
            });
            return true;
        }
        return false;
    }

    /**
     * Registers a new digging action.
     * @param location The location to dig on.
     * @param action The action.
     * @return <code>True</code> if the action got registered.
     */
    public static boolean register(Location location, DigAction action) {
        if (location == null)
            throw new NullPointerException("location");
        if (action == null)
            throw new NullPointerException("action");
        if (actions.containsKey(location)) {
            log.warn("Attempted to add duplicate dig action at {}: [{}].",
                location, action.getClass().getName());
            return false;
        }
        actions.put(location, action);
        return true;
    }
}
