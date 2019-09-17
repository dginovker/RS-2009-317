package org.gielinor.game.content.bot.impl;

import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.bot.Bot;
import org.gielinor.game.content.bot.BotMessageListener;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.rs2.pulse.Pulse;

/**
 * A simple agility script for crossing the agility pyramid's ledge.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AutoAgility extends Bot implements BotMessageListener {

    /**
     * Whether or not the script is paused.
     */
    private boolean paused;

    /**
     * The ledges.
     */
    private final GameObject[] ledges;

    /**
     * Creates the instance.
     */
    public AutoAgility() {
        super(null);
        this.ledges = null;
    }

    /**
     * Creates the instance.
     *
     * @param player The player.
     */
    public AutoAgility(Player player) {
        super(player);
        this.ledges = new GameObject[]{ RegionManager.getObject(10888, 3, 3358, 2843), RegionManager.getObject(10889, 3, 3358, 2845) };
    }

    @Override
    public void messageReceived(String message) {

    }

    @Override
    public boolean onStart() {
        getPlayer().setRunningBot(this);
        World.submit(new Pulse(7, getPlayer()) {

            @Override
            public boolean pulse() {
                if (isToEnd()) {
                    return true;
                }
                if (paused) {
                    return false;
                }
                if (ledges[0] == null || ledges[1] == null) {
                    end();
                    return true;
                }
                GameObject ledge = getPlayer().getLocation().equals(3359, 2842, 3) ? ledges[0] : ledges[1];
                if (ledge == null) {
                    getPlayer().setRunningBot(null);
                    end();
                    return true;
                }
                ObjectDefinition.getOptionHandler(ledge.getId(), "cross").handle(getPlayer(), ledge, "cross");
                return false;
            }
        });
        return false;
    }

    @Override
    public void onFinish() {

    }

    @Override
    public String getAuthor() {
        return "Gielinor";
    }

    @Override
    public double getVersion() {
        return 1.0D;
    }

    @Override
    public String getCategory() {
        return "Agility";
    }

    @Override
    public String getScriptName() {
        return "AutoAgility";
    }

    @Override
    public String getDescription() {
        return "desc";
    }

    /**
     * Pauses the script.
     */
    @Override
    public void pause() {
        this.paused = true;
    }

    /**
     * Unpauses the script.
     */
    @Override
    public void unpause() {
        this.paused = true;
    }

    /**
     * Checks if the script is paused.
     *
     * @return Whether or not the script is paused.
     */
    @Override
    public boolean isPaused() {
        return paused;
    }

    /**
     * Whether or not the script requires additional entries.
     *
     * @return
     */
    @Override
    public boolean additional() {
        return false;
    }

    /**
     * Sets additional entries.
     *
     * @param additional The additional entries.
     */
    @Override
    public void setAdditional(Object[] additional) {
    }
}
