package org.gielinor.game.node.entity.player;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.update.flag.context.Animation;
import org.gielinor.game.world.update.flag.context.Graphics;

import java.util.function.Consumer;

/**
 * Created by Stan van der Bend on 14/11/2017.
 *
 * Consists out of a collection of {@link Consumer}s that can be useful for chaining operations.
 *
 * project: runeworld-game-server
 * package: runeworld.world.entity.external
 */
public final class Players {

    public static Consumer<Player> sendDebug(String message) {
        return player -> player.debug(message);
    }

    public static Consumer<Player> sendMessage(String message) { return player -> player.getActionSender().sendMessage(message); }

    public static Consumer<Player> forceChat(String s) {
        return player -> player.sendChat(s);
    }

    public static Consumer<Player> sendGraphic(int i) {
        return player -> player.graphics(Graphics.create(i));
    }

    public static Consumer<Player> sendAnim(int i) {
        return player -> player.animate(new Animation(i));
    }

    public static Consumer<Player> closeWindows() {
        return player -> player.getInterfaceState().close();
    }

    public static Consumer<Player> forceSpeak(String message) {
        return player -> player.sendChat(message);
    }

    public static Consumer<Player> moveTo(Location position) {
        return player -> player.teleport(position);
    }

    public static Consumer<Player> lock() {
        return player -> player.lock(Integer.MAX_VALUE);
    }

    public static Consumer<Player> unlock() {
        return Entity::unlock;
    }

    public static Consumer<Player> giveItem(int id) {
        return giveItem(id, 1);
    }

    public static Consumer<Player> giveItem(int id, int amount) {
        return giveItem(new Item(id, amount));
    }

    public static Consumer<Player> giveItem(Item item) {
        return player -> player.getInventory().add(item);
    }
}
