package org.gielinor.game.content.activity;

import org.gielinor.content.periodicity.daily.impl.MinigameSpotlight;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.utilities.misc.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.activity.clanwars.WarSession;
import plugin.activity.duelarena.DuelSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the activities.
 *
 * @author Emperor
 */
public final class ActivityManager {

    private static final Logger log = LoggerFactory.getLogger(ActivityManager.class);

    /**
     * The mapping of instanced activities.
     */
    private static final Map<String, ActivityPlugin> ACTIVITIES = new HashMap<>();

    /**
     * Constructs a new {@code ActivityManager} {@code Object}.
     */
    private ActivityManager() {
    }

    /**
     * Registers an activity plugin.
     *
     * @param plugin The plugin to register.
     */
    public static void register(ActivityPlugin plugin) {
        plugin.register();
        ACTIVITIES.put(plugin.getName(), plugin);
        if (!plugin.isInstanced()) {
            plugin.configure();
        }
    }

    /**
     * Starts an instanced activity.
     *
     * @param player The player.
     * @param name   The name.
     * @param login  If we are logging in.
     * @param args   The arguments.
     */
    public static boolean start(Player player, String name, boolean login, Object... args) {
        ActivityPlugin plugin = ACTIVITIES.get(name);
        if (plugin == null) {
            log.warn("Unhandled activity: [{}]", name);
            return false;
        }
        try {
            if (plugin.isInstanced()) {
                (plugin = plugin.newInstance(player)).configure();
            }
            return plugin.start(player, login, args);
        } catch (Throwable ex) {
            log.error("Failed to start activity [{}].", name, ex);
            player.getActionSender().sendMessage("Error starting activity " + (plugin == null ? "null" : plugin.getName()) + "!");
        }
        return false;
    }

    /**
     * Handles declining any challenges or others.
     *
     * @param player The player.
     */
    public static void handleDeclines(Player player) {
        final DuelSession duelSession = DuelSession.getExtension(player);
        if (duelSession != null) {
            DuelSession.decline(player);
        }
        final WarSession warSession = WarSession.getExtension(player);
        if (warSession != null) {
            WarSession.decline(player);
        }
    }

    /**
     * Gets the activity by the name.
     *
     * @param name the name.
     * @return the activity.
     */
    public static ActivityPlugin getActivity(String name) {
        return ACTIVITIES.get(name);
    }

    /**
     * Gets a random activity for minigame spotlight.
     * @return the random activity
     */
    public static ActivityPlugin getRandomActivity() {
        List<String> spotlights = Arrays.asList(
            "pest control novice",
            // "Duel arena",
            "Barrows",
            "fight caves",
            "fight pits"
        );

        String chosenSpotlight = RandomUtil.random(spotlights);
        ActivityPlugin activityPlugin = ACTIVITIES.get(chosenSpotlight);
        if (MinigameSpotlight.Companion.getCurrentSpotlight() != null) {
            if (Objects.equals(activityPlugin.getName(), MinigameSpotlight.Companion.getCurrentSpotlight().getName())) {
                return getRandomActivity();
            }
        }
        return activityPlugin;
    }

}
