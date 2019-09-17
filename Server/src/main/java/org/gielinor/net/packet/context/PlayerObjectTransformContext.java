package org.gielinor.net.packet.context;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.Context;

/**
 * The {@link org.gielinor.net.packet.Context} for player to object transformation.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public final class PlayerObjectTransformContext implements Context {

    /**
     * The player.
     */
    private Player player;
    /**
     * The {@link org.gielinor.game.world.map.Location}.
     */
    private Location location;
    /**
     * The {@link org.gielinor.game.node.object.GameObject}.
     */
    private GameObject gameObject;
    /**
     * The delay.
     */
    private int delay;
    /**
     * The time.
     */
    private int time;

    /**
     *
     */
    public PlayerObjectTransformContext(Player player, Location location, GameObject gameObject, int delay, int time) {
        this.player = player;
        this.location = location;
        this.gameObject = gameObject;
        this.delay = delay;
        this.time = time;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     *
     * @param player The player.
     * @return This context instance.
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * The {@link org.gielinor.game.world.map.Location}.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * The {@link org.gielinor.game.node.object.GameObject}.
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * The delay.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * The time.
     */
    public int getTime() {
        return time;
    }
}