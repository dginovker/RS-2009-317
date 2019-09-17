package org.gielinor.rs2.plugin;

import org.gielinor.game.content.activity.ActivityManager;
import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.node.entity.player.info.login.LoginConfiguration;
import org.gielinor.rs2.model.command.Command;
import org.gielinor.utilities.debug.TimeStamp;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Represents a class used to handle the loading of all plugins.
 *
 * @author Emperor
 */
public final class PluginManager {

    private static final Logger log = LoggerFactory.getLogger(PluginManager.class);

    /**
     * The amount of plugins loaded.
     */
    private static int pluginCount;

    /**
     * Initializes the plugin manager.
     */
    public static void init() {
        try {
            TimeStamp timeStamp = new TimeStamp();
            loadLocal();
            long ms = timeStamp.stop();
            log.info("Initialized {} plugins in {} ms.", pluginCount, ms);
        } catch (Throwable t) {
            log.error("Failed to initialize plugins!", t);
        }
    }

    /**
     * Loads the plugins in the local directory.
     */
    private static void loadLocal() {
        Set<Class<? extends Plugin>> results = new Reflections("plugin").getSubTypesOf(Plugin.class);
        for (Class<? extends Plugin> clazz : results) {
            if (hasDefaultConstructor(clazz)) {
                try {
                    Plugin instance = clazz.newInstance();
                    definePlugin(instance);
                } catch (InstantiationException | IllegalAccessException ignored) {
                    // log.warn("Failed to instantiate [{}].", clazz.getName(), ignored);
                }
            }
            // else {
            // ---
            // This happens when we try to load plugins that don't have default constructors;
            // because these are (usually) not actually plugins, but classes inside
            // plugins, we ignore these.
            // ---
            // log.warn("[{}] lacks default constructor - skipping.", clazz.getName());
            // }
        }
    }

    private static boolean hasDefaultConstructor(Class<? extends Plugin> clazz) {
        for (Constructor<?> c : clazz.getDeclaredConstructors()) {
            if (Modifier.isPublic(c.getModifiers()) && c.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Defines the plugin.
     *
     * @param plugin
     *            The plugin.
     */
    @SuppressWarnings("unchecked")
    public static void definePlugin(Plugin<?> plugin) {
        PluginManifest manifest = plugin.getClass().getAnnotation(PluginManifest.class);
        if (manifest == null) {
            // TODO This should probably check all superclasses for annotations?
            manifest = plugin.getClass().getSuperclass().getAnnotation(PluginManifest.class);
        }
        if (manifest == null || manifest.type() == PluginType.ACTION) {
            try {
                plugin.newInstance(null);
            } catch (Throwable t) {
                log.error("Error loading plugin [{}].", plugin.getClass().getName(), t);
            }
        } else if (manifest.type() == PluginType.DIALOGUE) {
            ((DialoguePlugin) plugin).init();
        } else if (manifest.type() == PluginType.ACTIVITY) {
            ActivityManager.register((ActivityPlugin) plugin);
        } else if (manifest.type() == PluginType.LOGIN) {
            LoginConfiguration.getLoginPlugins().add((Plugin<Object>) plugin);
        } else if (manifest.type() == PluginType.COMMAND) {
            Command.getCommand().add((Plugin<Object>) plugin);
        } else {
            log.warn("Unhandled Manifest type in plugin [{}]: [{}].",
                plugin, manifest.type());
        }
        pluginCount++;
    }

    /**
     * Gets the amount of plugins currently loaded.
     *
     * @return The plugin count.
     */
    public static int getAmountLoaded() {
        return pluginCount;
    }

    /**
     * Defines a list of plugins.
     *
     * @param plugins the plugins.
     */
    public static void definePlugin(Plugin<?>... plugins) {
        for (Plugin<?> p : plugins) {
            definePlugin(p);
        }
    }
}
