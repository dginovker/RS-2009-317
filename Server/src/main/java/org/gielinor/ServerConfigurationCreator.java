package org.gielinor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author Erik
 */
public final class ServerConfigurationCreator {

    private static final Logger log = LoggerFactory.getLogger(ServerConfigurationCreator.class);

    public void start() {
        log.info("Welcome to configuration setup.");
        log.info("");
        log.info("This seems to be your first time running a server instance.");
        log.info("If not - please provide your [{}] configuration file!", ServerLauncher.CONFIGURATION_FILE_PATH);
        log.info("Either way. Let me help you get started with some questions.");
        log.info("");
        log.info("Press Enter to skip any questions to use provided defaults.");
        log.info("These settings can always be changed in the file listed above later.");
        log.info("");

        ServerConfiguration.Builder builder = new ServerConfiguration.Builder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String v;
            // First - let them choose the server name.
            if ((v = requestName(reader)).isEmpty()) {
                log.info("OK. Default name - {}.", ServerConfiguration.DEFAULT_NAME);
            } else {
                builder.setName(v);
                log.info("Alright! It will be called \"{}\" from now on.", v);
            }
            log.info("");
            // Choose a world number (continue until either skip or valid number!)
            for (;;) {
                v = requestWorldId(reader);
                if (v.isEmpty()) {
                    log.info("OK. Default world number - {}.", ServerConfiguration.DEFAULT_PORT);
                    break;
                }
                try {
                    int value = Integer.parseInt(v);
                    builder.setWorldId(value);
                    log.info("World is now known as World {}.", value);
                    break;
                } catch (IllegalArgumentException ex) { // NumberFormatException is an IllegalArgumentException.
                    // IllegalArgumentException is thrown by Builder on bad input.
                    log.warn("{} - not a valid world number. Default = {}.",
                        v, ServerConfiguration.DEFAULT_WORLD_ID);
                }
            }
            log.info("");
            // Choose a port number (continue until either skip or valid number!)
            for (;;) {
                v = requestPort(reader);
                if (v.isEmpty()) {
                    log.info("OK. Default port - {}.", ServerConfiguration.DEFAULT_PORT);
                    break;
                }
                try {
                    int value = Integer.parseInt(v);
                    builder.setPort(value);
                    log.info("Good! Very good! Connections will be listened to on *:{}.", value);
                    break;
                } catch (IllegalArgumentException ex) { // NumberFormatException is an IllegalArgumentException.
                                                        // IllegalArgumentException is thrown by Builder on bad input.
                    log.warn("{} - not a valid port number. Default = {}.",
                             v, ServerConfiguration.DEFAULT_PORT);
                }
            }
            log.info("");
            // Is this a production server?
            if ((v = requestProduction(reader)).isEmpty()) {
                log.info("Alright. I'll assume this is a development environment.");
            } else if (isTruthyResponse(v)) {
                builder.enableLiveMode();
                log.info("OK. Production environment it is!");
            } else {
                log.info("Alright. Be prepared for more technical logging then!");
            }
            log.info("");
            // Should beta exceptions apply?
            if ((v = requestBeta(reader)).isEmpty()) {
                log.info("Assuming not.");
            } else if (isTruthyResponse(v)) {
                builder.enableBetaMode();
                log.info("OK. Beta mode is enabled.");
            } else {
                log.info("Alright. No beta functionality!");
            }
            log.info("");
            // Ask for logback configuration file.
            for (;;) {
                v = requestLogbackConfigurationFile(reader);
                if (v.isEmpty()) {
                    log.info("Gotcha! No logback configuration file override selected.");
                    break;
                }
                try {
                    builder.setLogbackConfigurationFile(v);
                    log.info("Great! Will use [{}] as a logback configuration override.", v);
                    break;
                } catch (IllegalArgumentException ex) { // NumberFormatException is an IllegalArgumentException.
                    // IllegalArgumentException is thrown by Builder on bad input.
                    log.warn("Invalid [{}] - configuration must be XML!", v);
                }
            }
            log.info("");
            // Ask for discord bot token
            if ((v = requestDiscordBotToken(reader)).isEmpty()) {
                log.info("Alright. No discord integration!");
            } else {
                builder.setDiscordBotToken(v);
                log.info("Set discord bot API token to [{}].", v);
            }
            log.info("");
        } catch (IOException ex) {
            log.error("An exception occured during configuration setup!", ex);
        }

        Gson gson = new GsonBuilder()
                  .setPrettyPrinting()
                  .disableHtmlEscaping()
                  .serializeNulls()
                  .create();

        try (BufferedWriter writer = Files.newBufferedWriter(ServerLauncher.CONFIGURATION_FILE_PATH, StandardCharsets.UTF_8)) {
            ServerConfiguration configuration = builder.build();
            gson.toJson(configuration.toJson(), writer);
            log.info("Well done!");
            log.info("Result: {}.", configuration);
            log.info("Your configuration has been completed, and you may now start the server.");
        } catch (IOException ex) {
            log.error("Failed to save configuration. Please try again, or ask for help.", ex);
        }
    }

    private String requestName(BufferedReader reader) throws IOException {
        log.info("What would you like to call this server?");
        return reader.readLine();
    }

    private String requestPort(BufferedReader reader) throws IOException {
        log.info("Which port would you like to connect on (1000-65535)?");
        return reader.readLine();
    }

    private String requestWorldId(BufferedReader reader) throws IOException {
        log.info("What would you like the world number to be (default = {}).", ServerConfiguration.DEFAULT_WORLD_ID);
        return reader.readLine();
    }

    private String requestProduction(BufferedReader reader) throws IOException {
        log.info("Will this be a production environment [Y/N]?");
        return reader.readLine();
    }

    private String requestBeta(BufferedReader reader) throws IOException {
        log.info("Should beta mode be enabled - this adds certain exceptions to usable commands, etc. [Y/N]?");
        return reader.readLine();
    }

    /* Exists so that we can override configuration. */
    private String requestLogbackConfigurationFile(BufferedReader reader) throws IOException {
        log.info("Specify the relative location of your logback XML configuration override:");
        return reader.readLine();
    }

    private String requestDiscordBotToken(BufferedReader reader) throws IOException {
        log.info("Provide a discord API token to enable integration - OR press Enter to skip and disable:");
        return reader.readLine();
    }

    private boolean isTruthyResponse(String v) {
        v = v.toLowerCase();
        return v.equals("y") || v.equals("yes") || v.equals("1") || v.equals("true");
    }

}
