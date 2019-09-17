package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Settings;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ConfigContext;
import org.gielinor.net.packet.out.Config;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Manages a player's configurations.
 *
 * @author Emperor
 */
public final class ConfigurationManager implements SavingModule {

    /**
     * The amount of configurations.
     */
    public static final int SIZE = 2000;

    /**
     * The player.
     */
    private final Player player;

    /**
     * The configurations.
     */
    private final int[] configurations = new int[SIZE];

    /**
     * The configurations.
     */
    private final int[] savedConfigurations = new int[SIZE];

    /**
     * Constructs a new {@code ConfigurationManager} {@code Object}.
     *
     * @param player The player.
     */
    public ConfigurationManager(Player player) {
        this.player = player;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        for (int index = 0; index < savedConfigurations.length; index++) {
            int value = savedConfigurations[index];
            if (value != 0) {
                byteBuffer.putShort((short) index);
                byteBuffer.putInt(value);
            }
        }
        byteBuffer.putShort((short) -1);
    }

    @Override
    public void parse(ByteBuffer buffer) {
        int index = 0;
        while ((index = buffer.getShort()) != -1) {
            savedConfigurations[index] = buffer.getInt();
        }
    }

    /**
     * Initializes the configurations.
     */
    public void init() {
        for (int i = 0; i < savedConfigurations.length; i++) {
            int value = savedConfigurations[i];
            if (value != 0) {
                set(i, value, false);
            }
        }
    }

    /**
     * Resets the configurations.
     */
    public void reset() {
        for (int i = 0; i < configurations.length; i++) {
            configurations[i] = 0;
        }
    }

    /**
     * Sets a configuration.
     *
     * @param config The configuration.
     * @param value  The value.
     */
    public void set(Configuration config, boolean value) {
        set(config.id, value);
    }

    /**
     * Sets a configuration.
     *
     * @param id    The id of the configuration.
     * @param value The value.
     */
    public void set(int id, boolean value) {
        set(id, value ? 1 : 0);
    }

    /**
     * Sets a configuration.
     *
     * @param config The configuration.
     * @param value  The value.
     */
    public void set(Configuration config, int value) {
        set(config, value, false);
    }

    /**
     * Sets the configuration.
     *
     * @param config The configuration id.
     * @param value  The value.
     * @param saved  If the configuration should be saved.
     */
    public void set(Configuration config, int value, boolean saved) {
        set(config.id, value, saved);
    }

    /**
     * Sets a configuration.
     *
     * @param id    The configuration id.
     * @param value The value.
     */
    public void set(int id, int value) {
        set(id, value, false);
        if (id == 300) {
            for (int specialId : Settings.SPECIAL_ATTACK_TEXT) {
                player.getActionSender().sendString(specialId, "Special Attack (" + (value / 10) + "%)");
            }
        }
    }

    /**
     * Sets a configuration for a set amount of time.
     *
     * @param id    the id.
     * @param value the value.
     * @param delay the delay.
     */
    public void set(final int id, final int value, int delay) {
        set(id, value);
        World.submit(new Pulse(delay, player) {

            @Override
            public boolean pulse() {
                set(id, 0);
                return true;
            }
        });
    }

    /**
     * Sets a configuration.
     *
     * @param id    The configuration id.
     * @param value The value.
     */
    public void set(int id, int value, boolean saved) {
        if (configurations[id] != value) {
            PacketRepository.send(Config.class, new ConfigContext(player, id, configurations[id] = value));
        }
        if (saved) {
            savedConfigurations[id] = value;
        }
    }

    /**
     * Forces a configuration.
     *
     * @param id    The configuration id.
     * @param value The value.
     */
    public void force(int id, int value, boolean saved) {
        configurations[id] = value;
        PacketRepository.send(Config.class, new ConfigContext(player, id, configurations[id] = value));
        if (saved) {
            savedConfigurations[id] = value;
        }
    }

    /**
     * Sends the configuration without caching.
     */
    public void send(int id, int value) {
        PacketRepository.send(Config.class, new ConfigContext(player, id, value));
    }

    /**
     * Gets the configuration value.
     *
     * @param id The config id.
     * @return The value.
     */
    public int get(int id) {
        return configurations[id];
    }

    /**
     * Gets the configuration value.
     *
     * @param configuration The {@link org.gielinor.game.node.entity.player.link.ConfigurationManager.Configuration}.
     * @return The value.
     */
    public int get(Configuration configuration) {
        return configurations[configuration.getId()];
    }

    /**
     * Holds the configurations.
     *
     * @author Emperor
     */
    public enum Configuration {

        BRIGHTNESS(166),
        MUSIC_VOLUME(168),
        EFFECT_VOLUME(169),
        MOUSE_BUTTON(170),
        CHAT_EFFECT(171),
        RETALIATE(172),
        RUNNING(173),
        SPLIT_PRIVATE(287),
        ACCEPT_AID(304),
        PC_PORTALS(719),
        CLAN_WAR_DATA(1147),
        LOAD_ORBS(306),
        ROOF_REMOVAL(307),
        REMAINING_XP(308),
        PROFANITY_FILTER(311),
        PRIVATE_CHAT_NOTIFICATION_TIMER(312),
        MOUSE_MOVEMENT(314);

        /**
         * The config id.
         */
        private final int id;

        /**
         * Constructs a new {@code Configuration} {@code Object}.
         *
         * @param id The config id.
         */
        private Configuration(int id) {
            this.id = id;
        }

        /**
         * Gets the id.
         *
         * @return The id.
         */
        public int getId() {
            return id;
        }
    }

    /**
     * Gets the player's saved configurations.
     *
     * @return The saved configurations array.
     */
    public int[] getSavedConfigurations() {
        return savedConfigurations;
    }
}
