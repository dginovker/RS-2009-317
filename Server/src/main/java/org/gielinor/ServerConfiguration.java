package org.gielinor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.gson.JsonObject;

/**
 * @author Erik
 */
public final class ServerConfiguration {

    static final String DEFAULT_NAME = "Gielinor";
    static final int DEFAULT_PORT = 43594;
    static final int DEFAULT_WORLD_ID = 1;

    private final int port;
    private final int worldId;
    private final String name;
    private final String logbackConfigurationFile;
    private final String discordBotToken;
    private final boolean developmentEnabled;
    private final boolean betaEnabled;

    private ServerConfiguration(Builder builder) {
        this.port = builder.port;
        this.name = builder.name;
        this.worldId = builder.worldId;
        this.logbackConfigurationFile = builder.logbackConfigurationFile;
        this.discordBotToken = builder.discordBotToken;
        this.developmentEnabled = builder.developmentEnabled;
        this.betaEnabled = builder.betaEnabled;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public int getWorldId() {
        return worldId;
    }

    public String getLogbackConfigurationFile() {
        return logbackConfigurationFile;
    }

    public String getDiscordBotToken() {
        return discordBotToken;
    }

    public boolean isDevelopmentEnabled() {
        return developmentEnabled;
    }

    public boolean isBetaEnabled() {
        return betaEnabled;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append(ServerConfiguration.class.getSimpleName()).append("[").append(this.port).append("; ");
        if (this.developmentEnabled) builder.append("develop; ");
        else                         builder.append("live; ");
        if (betaEnabled)
            builder.append("beta; ");
        if (this.logbackConfigurationFile != null)
            builder.append("logbackConfigurationFile=\"").append(this.logbackConfigurationFile).append("\"; ");
        if (this.discordBotToken != null)
            builder.append("discordBotToken=\"").append(this.discordBotToken).append("\"; ");
        builder.append("name=\"").append(this.name).append("\"]");
        return builder.toString();
    }

    JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", this.name);
        json.addProperty("port", this.port);
        json.addProperty("discordBotToken", this.discordBotToken);
        json.addProperty("logbackConfigurationFile", this.logbackConfigurationFile);
        json.addProperty("developmentEnabled", this.developmentEnabled);
        json.addProperty("betaEnabled", this.betaEnabled);
        json.addProperty("worldId", this.worldId);
        return json;
    }

    public static class Builder {

        private int port = DEFAULT_PORT;
        private int worldId = DEFAULT_WORLD_ID;
        private String name = DEFAULT_NAME;
        private String logbackConfigurationFile = null;
        private String discordBotToken = null;
        private boolean developmentEnabled = true;
        private boolean betaEnabled = false;

        public Builder setPort(int port) {
            checkArgument(port >= 1000 && port <= 65535, "Port (%s) must be in exclusive range 1000-65535.", port);
            this.port = port;
            return this;
        }

        public Builder setWorldId(int worldId) {
            checkArgument(worldId >= 1, "World ID (%s) must be positive.", worldId);
            this.worldId  =worldId;
            return this;
        }

        public Builder setName(String name) {
            this.name = checkNotNull(name, "name");
            return this;
        }

        public Builder setLogbackConfigurationFile(String logbackConfigurationFile) {
            if (logbackConfigurationFile != null && !logbackConfigurationFile.endsWith(".xml"))
                throw new IllegalArgumentException("Logback configuration must be either Groovy or XML.");
            // Null allowed and indicates no override. Any file specified must be XML.
            this.logbackConfigurationFile = logbackConfigurationFile;
            return this;
        }

        public Builder setDiscordBotToken(String discordBotToken) {
            this.discordBotToken = discordBotToken; // null allowed; default
            return this;
        }

        public Builder enableLiveMode() {
            this.developmentEnabled = false;
            return this;
        }

        public Builder enableBetaMode() {
            this.betaEnabled = true;
            return this;
        }

        public ServerConfiguration build() {
            return new ServerConfiguration(this);
        }

    }

}
