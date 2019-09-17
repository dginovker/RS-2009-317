package org.gielinor;

import bot.DiscordBot;
import bot.DiscordConstants;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.gielinor.game.system.SystemManager;
import org.gielinor.game.world.World;
import org.gielinor.net.NioReactor;
import org.gielinor.net.world.WorldList;
import org.gielinor.rs2.config.Constants;
import org.gielinor.rs2.pulse.impl.ServerAnnouncementPulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.LogManager;

import ch.qos.logback.classic.LoggerContext;

/**
 * @author Erik
 */
public final class ServerLauncher {

    static final Path CONFIGURATION_FILE_PATH = Paths.get("config.json");

    /** The time, in milliseconds, at which the server initialized. */
    public static final long START_EPOCH_MILLIS = System.currentTimeMillis();

    private static final Logger log = LoggerFactory.getLogger(ServerLauncher.class);

    /* Set up JUL-to-SLF4J bridge; some libraries use JUL. */
    static {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        // This is needed so that all logging calls are forwarded to SLF4J.
        java.util.logging.Logger.getGlobal().setLevel(Level.FINEST);
    }



    public static void main(String[] args) {
        ServerConfiguration config;
        try {
            if (Files.exists(CONFIGURATION_FILE_PATH)) {
                try {
                    config = ServerLauncher.readConfiguration();
                } catch (Exception ex) {
                    log.error("Bad configuration [{}]", CONFIGURATION_FILE_PATH, ex);
                    System.exit(1);
                    return;
                }
            } else {
                // Help create a valid configuration.
                new ServerConfigurationCreator().start();
                System.exit(1);
                return;
            }
        } catch (SecurityException ex) {
            log.error("Security is disrupting configuration read!", ex);
            System.exit(1);
            return;
        }

        // Override logback, if necessary!
        // This allows you to decide the exact config from which
        // logback should reload (if scan enabled!) without depending
        // on the VM property flag.
        if (config.getLogbackConfigurationFile() != null) {
            try {
                URL url = Paths.get(config.getLogbackConfigurationFile()).toUri().toURL();
                reloadLogback(url);
            } catch (MalformedURLException ex) {
                log.error("Bad logback configuration file: {}", config.getLogbackConfigurationFile(), ex);
                System.exit(1);
                return;
            }
        }

        log.info("Loading {}...", config.getName());
        log.info("Server configuration: {}.", config);

        try {
            World.setConfiguration(config);
            World.getWorld().init();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (!Constants.KILL_SERVER) {
                    SystemManager.getTerminator().terminate();
                    DiscordBot.getBot().getApi().disconnect();
                }
            }));
            NioReactor.configure(Constants.PORT).start();
            Thread.setDefaultUncaughtExceptionHandler((thread, t1) -> World.handleError(t1));
            ServerAnnouncementPulse.populateAnnouncements();
            WorldList.getWorldService().initializeWorlds();
            long initializationTimeMillis = System.currentTimeMillis() - START_EPOCH_MILLIS;
            DecimalFormat df = new DecimalFormat("#0.000");
            log.info("Took {} sec from class initialization to action-ready.", df.format(initializationTimeMillis / 1000D));
            DiscordBot.getBot().sendMessage(DiscordConstants.SERVER_MESSAGES_CHANNEL_ID, config.getName() + " is now up and running!");
        } catch (Throwable t) {
            log.error("Error during start-up.", t);
            System.exit(1);
        }
    }



    private static ServerConfiguration readConfiguration() {
        ServerConfiguration.Builder builder = new ServerConfiguration.Builder();
        JsonObject jsonConfig;
        try (BufferedReader reader = Files.newBufferedReader(CONFIGURATION_FILE_PATH, StandardCharsets.UTF_8)) {
            jsonConfig = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException ex) {
            log.error("Unable to read configuration file [{}].", CONFIGURATION_FILE_PATH, ex);
            System.exit(1);
            throw new AssertionError(); // because compiler doesn't understand System.exit(1) call ends it.
        }
        if (jsonConfig.has("name"))
            builder.setName(jsonConfig.get("name").getAsString());
        if (jsonConfig.has("port"))
            builder.setPort(jsonConfig.get("port").getAsInt());
        if (jsonConfig.has("developmentEnabled") && !jsonConfig.get("developmentEnabled").getAsBoolean())
            builder.enableLiveMode();
        if (jsonConfig.has("betaEnabled") && jsonConfig.get("betaEnabled").getAsBoolean())
            builder.enableBetaMode();
        if (jsonConfig.has("worldId")) {
            builder.setWorldId(jsonConfig.get("worldId").getAsInt());
        }
        if (jsonConfig.has("logbackConfigurationFile")) { // Nullable
            JsonElement value = jsonConfig.get("logbackConfigurationFile");
            if (value.isJsonPrimitive()) { // Not null
                builder.setLogbackConfigurationFile(value.getAsString());
            }
        }
        if (jsonConfig.has("discordBotToken")) { // Nullable
            JsonElement value = jsonConfig.get("discordBotToken");
            if (value.isJsonPrimitive()) { // Not null
                builder.setDiscordBotToken(value.getAsString());
            }
        }
        return builder.build();
    }



    private static void reloadLogback(URL configurationFileUrl) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(configurationFileUrl);
        } catch (JoranException ignored) {
            // Handled by StatusPrinter
        }

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }


    private ServerLauncher() {
        throw new InstantiationError();
    }

}
