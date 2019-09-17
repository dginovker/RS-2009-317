package org.gielinor.game.content.bot;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.rs2.pulse.Pulse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a new bot script.
 * <p>
 * TODO random event handlers
 */
public abstract class Bot {

    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    /**
     * The player.
     */
    private Player player;

    /**
     * The {@link Method} instance.
     */
    private Method method;

    /**
     * The {@link Walking} instance.
     */
    private Walking walking;

    /**
     * The {@link BotMessageHandler} instance.
     */
    private BotMessageHandler botMessageHandler;

    /**
     * Whether or not this bot is paused.
     */
    protected boolean paused;

    /**
     * Whether or not this bot should end.
     */
    private boolean toEnd;

    /**
     * Creates the {@link Bot} instance for the player given.
     *
     * @param player The player.
     */
    public Bot(Player player) {
        this.player = player;
        this.method = new Method(player);
        this.walking = new Walking(player);
        this.botMessageHandler = new BotMessageHandler(player);
    }

    /**
     * The action on the bot start.
     *
     * @return Whether or not the bot was started.
     */
    public boolean onStart() {
        player.setRunningBot(this);
        World.submit(getPulse());
        return true;
    }

    /**
     * The pulse to run.
     *
     * @return The pulse.
     */
    public Pulse getPulse() {
        return new Pulse(1, player) {

            boolean randomEvent = false;
            Location originalLocation = getPlayer().getLocation();

            @Override
            public boolean pulse() {
                if (toEnd) {
                    return true;
                }
                if (isPaused()) {
                    return true;
                }
                if (randomEvent) {
                    stop();
                    return false;
                }
                if (paused && getPlayer().getLocation().getDistance(originalLocation) < 5) {
                    unpause();
                    return false;
                }
                if (getPlayer().getAntiMacroHandler().hasEvent()) {
                    stop();
                    return false;
                }
                return false;
            }
        };
    }

    /**
     * The action on the bot finish.
     */
    public abstract void onFinish();

    /**
     * The author of this bot.
     *
     * @return The author.
     */
    public abstract String getAuthor();

    /**
     * The version of this bot.
     *
     * @return The version.
     */
    public abstract double getVersion();

    /**
     * The category this bot is in.
     *
     * @return The category.
     */
    public abstract String getCategory();

    /**
     * The name of this bot.
     *
     * @return The version.
     */

    public abstract String getScriptName();

    /**
     * The description of this bot.
     *
     * @return The version.
     */
    public abstract String getDescription();

    /**
     * Pauses the script.
     */
    public void pause() {
        log.debug("Pausing script: {}", getScriptName());
        this.paused = true;
    }

    /**
     * Unpauses the script.
     */
    public void unpause() {
        log.debug("Unpausing script: {}.", getScriptName());
        this.paused = false;
    }

    /**
     * Checks if the script is paused.
     *
     * @return <code>True</code> if the bot is paused.
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Ends this script.
     */
    public void end() {
        log.debug("Ending script: {}.", getScriptName());
        this.toEnd = true;
        player.setRunningBot(null);
    }

    /**
     * Gets whether or not this script should end.
     *
     * @return <code>True</code> if it should end.
     */
    public boolean isToEnd() {
        return player == null || this.toEnd;
    }

    /**
     * Whether or not the script requires additional entries.
     *
     * @return <code>True</code> if so.
     */
    public abstract boolean additional();

    /**
     * Sets additional entries.
     *
     * @param additional The additional entries.
     */
    public abstract void setAdditional(Object[] additional);

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     */
    public void setPlayer(Player player) {
        this.player = player;
        this.method = new Method(player);
        this.walking = new Walking(player);
        this.botMessageHandler = new BotMessageHandler(player);
    }

    /**
     * Gets the {@link Method} instance.
     *
     * @return The method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Gets the {@link Walking} instance.
     *
     * @return The walking.
     */
    public Walking getWalking() {
        return walking;
    }

    /**
     * Gets the {@link BotMessageHandler} instance.
     *
     * @return The bot message handler.
     */
    public BotMessageHandler getBotMessageHandler() {
        return botMessageHandler;
    }

}
