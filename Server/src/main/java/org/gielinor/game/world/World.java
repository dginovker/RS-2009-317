package org.gielinor.game.world;

import bot.DiscordBot;
import com.google.common.collect.Table;
import org.gielinor.ServerConfiguration;
import org.gielinor.cache.Cache;
import org.gielinor.cache.InvalidCacheException;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.content.periodicity.PeriodicityPulseManager;
import org.gielinor.content.statistics.boards.StatisticBoards;
import org.gielinor.database.PlayerLists;
import org.gielinor.game.content.eco.grandexchange.GrandExchangeDatabase;
import org.gielinor.game.content.global.shop.ShopParser;
import org.gielinor.game.node.entity.combat.equipment.RangeWeapon;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.ai.AIPBuilder;
import org.gielinor.game.node.entity.player.ai.AIPlayer;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.system.SystemState;
import org.gielinor.game.system.communication.ClanCommunication;
import org.gielinor.game.system.script.ScriptManager;
import org.gielinor.game.world.callback.CallbackHub;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.repository.DisconnectionQueue;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.mqueue.MessageQueue;
import org.gielinor.mqueue.message.Message;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.parser.ParserSequence;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.plugin.PluginManager;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.ServerAnnouncementPulse;
import org.gielinor.rs2.task.TaskExecutor;
import org.gielinor.rs2.util.ConfigurationParser;
import org.gielinor.spring.service.AccountService;
import org.gielinor.worker.MajorUpdateWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Holds data global to the game WORLD.
 *
 * @author Emperor
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class World {

    private static final Logger log = LoggerFactory.getLogger(World.class);

    /**
     * World instance.
     */
    private static final World WORLD = new World();

    /**
     * Represents the {@link MajorUpdateWorker} of this {@link World}.
     */
    private static final MajorUpdateWorker MAJOR_UPDATE_WORKER = new MajorUpdateWorker();

    /**
     * The lock object.
     */
    private static final Lock LOCK = new ReentrantLock();

    /**
     * The pulse tasks list.
     */
    private static final List<Pulse> TASKS = new ArrayList<>();

    static List<AIPlayer> aiPlayers = new ArrayList<>();

    /**
     * The current amount of (600ms) cycles elapsed.
     */
    private static int ticks;

    /**
     * The message queue.
     */
    private static MessageQueue messageQueue;

    /**
     * The server announcement pulse.
     */
    public ServerAnnouncementPulse serverAnnouncementPulse;

    /**
     * The 317 cache block.
     */
    private Cache cache;

    private static ServerConfiguration configuration;

    /**
     * The {@link org.springframework.context.ApplicationContext}.
     */
    private ApplicationContext applicationContext;

    /**
     * The {@link org.gielinor.spring.service.AccountService} for loading and
     * saving players.
     */
    private AccountService accountService;

    /**
     * Constructs a new {@code GameWorld} {@code Object}.
     */
    private World() {
        setApplicationContext(new ClassPathXmlApplicationContext(Constants.BEANS_CONFIGURATIONS));
    }

    /**
     * Submits a new message.
     *
     * @param message
     *            The message to submit.
     */
    public static void submit(Message message) {
        messageQueue.pushTask(message);
    }

    /**
     * Method used to submit a pulse to the {@link #TASKS} list. the
     * {@link #TASKS} will loop through all <code>Pulse</code> and call
     * {@link Pulse#pulse()}.
     *
     * @param pulse
     *            the pulse to pulse.
     */
    public static void submit(Pulse pulse) {
        LOCK.lock();

        try {
            TASKS.add(pulse);
        } finally {
            LOCK.unlock();
        }
    }

    public static void sendWorldMessage(String message) {
        sendWorldMessage("FF8C38", message, 5);
    }

    public static void sendWorldMessage(String color, String message, int crownId) {
        for (Player onlinePlayer : Repository.getPlayers()) {
            if (onlinePlayer == null || !onlinePlayer.isActive()) {
                continue;
            }
            onlinePlayer.getActionSender().sendMessage("<col=" + color + "><shad=000>" + (crownId == -1 ? "" : "<img=" + crownId + ">") + message);
        }
    }

    public static void setConfiguration(ServerConfiguration configuration) {
        if (World.configuration != null)
            throw new IllegalStateException("configuration is already set!");
        World.configuration = Objects.requireNonNull(configuration, "configuration");
    }

    public static ServerConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * to control everything about the message
     */
    public static void sendCustomWorldMessage(String message) {
        for (Player onlinePlayer : Repository.getPlayers()) {
            if (onlinePlayer == null || !onlinePlayer.isActive()) {
                continue;
            }
            onlinePlayer.getActionSender().sendMessage(message);
        }
    }
    public static void sendAdminMessage(String message) {
        for (Player onlinePlayer : Repository.getPlayers()) {
            if (onlinePlayer == null || !onlinePlayer.isActive() || !onlinePlayer.getRights().isAdministrator())
                continue;
            onlinePlayer.getActionSender().sendMessage(message);
        }
    }
    /**
     * Method used to pulse all <code>Pulse</code> values within the
     * {@link #TASKS} list.
     */
    public static void pulse() {
        LOCK.lock();
        List<Pulse> pulses;
        List<Pulse> finished = new ArrayList<>();

        try {
            pulses = new ArrayList<>(TASKS);
        } finally {
            LOCK.unlock();
        }

        for (Pulse pulse : pulses) {
            if (pulse == null) {
                continue;
            }
            try {
                long nanoStartTime = System.nanoTime();
                boolean remove = pulse.update(); // Calls pulse()
                long nanoFinishTime = System.nanoTime();
                long milliTimeTaken = TimeUnit.NANOSECONDS.toMillis(nanoFinishTime - nanoStartTime);
                // Mark any tasks that need removing here, and we do it after processing all.
                if (remove) finished.add(pulse);
                // IMPORTANT: Leaving this warning in because it exists already, but NO pulse should
                //            ever be this slow, since 600 millis is the total allocated time for each
                //            world cycle, so a single pulse taking that long would be .... bad.
                if (milliTimeTaken >= 600 /* millis */) {
                    log.warn("{} - incredibly slow pulse took {}ms.",
                             pulse.getClass().getName(), milliTimeTaken);
                }
            } catch (Throwable t) {
                log.error("{} - pulse threw an exception.", pulse.getClass().getName(), t);
                pulse.stop();
                finished.add(pulse);
            }
        }

        // Remove any tasks marked for removal (if any at all).
        if (!finished.isEmpty()) {
            LOCK.lock();
            try {
                TASKS.removeAll(finished);
            } finally {
                LOCK.unlock();
            }
        }

        ticks++;

        if (!getConfiguration().isDevelopmentEnabled()) {
            if (ticks % 50 == 0) {
                TaskExecutor.execute(() -> {
                    try {
                        Repository.getPlayers()
                            .stream()
                            .filter(Objects::nonNull)
                            .filter(player -> !player.isArtificial() && player.isPlaying())
                            .forEach(DisconnectionQueue::save);
                    } catch (Throwable t) {
                        log.error("Failed during world save.", t);
                    }
                });
            }
        }
    }

    /**
     * Gets the WORLD instance.
     *
     * @return The WORLD instance.
     */
    public static World getWorld() {
        return WORLD;
    }

    /**
     * Called when the server shuts down.
     */
    public static void shutdown() {
        SystemManager.flag(SystemState.TERMINATED);
    }

    /**
     * Gets the major update worker.
     *
     * @return The major update worker.
     */
    public static MajorUpdateWorker getMajorUpdateWorker() {
        return MAJOR_UPDATE_WORKER;
    }

    /**
     * Gets the ticks.
     *
     * @return The ticks.
     */
    public static int getTicks() {
        return ticks;
    }

    /**
     * Handles an exception in any of the pools.
     *
     * @param t
     *            The exception.
     */
    public static void handleError(Throwable t) {
        log.error("An error occured in an executor service! Stopping server immediately.", t);
        System.exit(1);
    }

    /**
     * Loads server configuration.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    private void loadConfiguration() throws IOException {
        try (FileInputStream fis = new FileInputStream(Constants.DATA_CONFGIURATION_PATH)) {
            ConfigurationParser p = new ConfigurationParser(fis);
            Map<String, String> mappings = p.getMappings();
            Constants.SERVER_NAME = mappings.getOrDefault("serverName", "Server");

            if (mappings.containsKey("serverPort")) {
                Constants.PORT = Integer.parseInt(mappings.get("serverPort"));
            }

            if (mappings.containsKey("worldId")) {
                Constants.WORLD_ID = Integer.parseInt(mappings.get("worldId"));
            }

            if (mappings.containsKey("combatExpModifier")) {
                Constants.COMBAT_EXP_MODIFIER = Double.valueOf(mappings.get("combatExpModifier"));
            } else {
                Constants.COMBAT_EXP_MODIFIER = 1.0;
            }

            if (mappings.containsKey("nonCombatExpModifier")) {
                Constants.NON_COMBAT_EXP_MODIFIER = Double.valueOf(mappings.get("nonCombatExpModifier"));
            } else {
                Constants.NON_COMBAT_EXP_MODIFIER = 1.0;
            }

            if (mappings.containsKey("accountService")) {
                Constants.ACCOUNT_SERVICE = String.valueOf(mappings.get("accountService"));
                accountService = ((AccountService) getApplicationContext().getBean(Constants.ACCOUNT_SERVICE));
            }

            Table<String, String, String> complexMappings = p.getComplexMappings();

            if (complexMappings.containsRow("packetHandlers")) {
                Map<Class<? extends IncomingPacket>, IncomingPacket> cachedHandlers = new HashMap<>();

                for (Map.Entry<String, String> packetHandler : complexMappings.row("packetHandlers").entrySet()) {
                    String className = packetHandler.getValue();
                    int opcode = Integer.parseInt(packetHandler.getKey());

                    try {
                        @SuppressWarnings("unchecked")
                        Class<? extends IncomingPacket> handlerClass = (Class<? extends IncomingPacket>) Class.forName(className);
                        IncomingPacket handler = cachedHandlers.get(handlerClass);

                        // Some handlers are reused, so we temporarily cache and re-use instances
                        // for any handlers of the same type.
                        if (handler == null) {
                            cachedHandlers.put(handlerClass, handler = handlerClass.newInstance());
                        }

                        PacketRepository.bind(opcode, handler);
                    } catch (ClassNotFoundException ex) {
                        log.error("{} - defined class ({}) can not be found.",
                                  opcode, className, ex);
                    } catch (ClassCastException ex) {
                        log.error("{} - defined class ({}) is not of required type: {}.",
                                  opcode, className, IncomingPacket.class.getSimpleName(), ex);
                    } catch (Throwable t) {
                        log.error("{} - Packet handler could not be bound: {}", opcode, className, t);
                    }
                }
            }
        }
    }

    /**
     * Prompts the {@link World} to begin it's initialization.
     *
     * @throws Throwable
     *             when the exception occurs.
     */
    public void init() throws Throwable {
        this.loadConfiguration();

        if (configuration.getDiscordBotToken() != null) {
            DiscordBot.getBot().start();
            log.info("Discord bot started.");
        }

        // DiscordBot.getBot().sendMessage(DiscordConstants.SERVER_MESSAGES_CHANNEL_ID, "Server starting...");
        log.info("Starting {} (revision: {}).", Constants.SERVER_NAME, Constants.REVISION);

        try {
            log.info("Loading game cache...");
            cache = new Cache(new File(Constants.CACHE_DIRECTORY));
            log.info("Finished loading game cache.");
        } catch (InvalidCacheException ex) {
            log.error("Failed to load cache.", ex);
        }

        ObjectDefinition.unpackConfig();
        GrandExchangeDatabase.init();
        ParserSequence.prepare();
        ParserSequence.parse();
        RangeWeapon.initialize();
        ScriptManager.load();
        PluginManager.init();
        ParserSequence.post();
        CallbackHub.call();
        ClanCommunication.load();
        PeriodicityPulseManager.Companion.init();
        StatisticBoards.INSTANCE.load();
        PlayerLists.loadPlayersOnlineToday();
        ShopParser.Companion.loadShops();

        messageQueue = new MessageQueue();
        messageQueue.start();
        SystemManager.flag(getConfiguration().isDevelopmentEnabled() ? SystemState.PRIVATE : SystemState.ACTIVE);

        // TODO Adds random AIPs every 8 minutes for activity
        if (Constants.PORT != 43594) {
            submit(new Pulse(800) {

                @SuppressWarnings("unused")
                int newPlayers;
                final SecureRandom secureRandom = new SecureRandom();

                @Override
                public boolean pulse() {
                    if (aiPlayers.size() > (secureRandom.nextInt(10) + 2)) {
                        if (secureRandom.nextInt(7) == 2) {
                            int remove = secureRandom.nextInt(aiPlayers.size() - 1);
                            aiPlayers.get(remove).clear();
                            aiPlayers.remove(remove);
                            newPlayers--;
                            return false;
                        }
                        return false;
                    }

                    if (secureRandom.nextInt(7) != 2) {
                        return false;
                    }

                    if (secureRandom.nextInt(7) == 2 && aiPlayers.size() > 4) {
                        int remove = secureRandom.nextInt(aiPlayers.size() - 1);
                        aiPlayers.get(remove).clear();
                        aiPlayers.remove(remove);
                        newPlayers--;
                        return false;
                    }

                    AIPlayer aiPlayer = AIPBuilder.makeUnique(null, "1", new Location(2592, 4709, 0));
                    aiPlayers.add(aiPlayer);
                    Repository.getPlayers().add(aiPlayer);
                    aiPlayer.init();
                    newPlayers++;
                    return false;
                }
            });
        }

        submit(serverAnnouncementPulse = new ServerAnnouncementPulse());
        System.gc();
    }

    /**
     * Gets the {@link org.springframework.context.ApplicationContext}.
     *
     * @return The application context.
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Sets the {@link org.springframework.context.ApplicationContext}.
     *
     * @param applicationContext
     *            The application context.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets the {@link org.gielinor.spring.service.AccountService} to use for
     * loading and saving players.
     *
     * @return The account service.
     */
    public AccountService getAccountService() {
        return accountService;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }


}
