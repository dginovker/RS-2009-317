package org.gielinor.game.content.bot.impl;

import org.gielinor.game.content.bot.Bot;
import org.gielinor.game.content.bot.BotMessageListener;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * An air runecrafting script.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class AutoRunecraft extends Bot implements BotMessageListener {

    /**
     * The walking paths.
     */
    private final Location[] walkLocations = new Location[]{
        new Location(3092, 3499, 0),
        new Location(3088, 3515, 0),
        new Location(3102, 3533, 0),
        new Location(3096, 3549, 0)
    };

    /**
     * Creates the instance.
     */
    public AutoRunecraft() {
        super(null);
    }

    /**
     * Creates the instance.
     *
     * @param player The player.
     */
    public AutoRunecraft(Player player) {
        super(player);
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
                if (getPlayer().getLocation().inArea(new ZoneBorders(3091, 3488, 3098, 3499))) {
                    getPlayer().getActionSender().sendMessage("Start in Edgeville bank!");
                    end();
                    return true;
                }
                int lastWalkIndex = 0;
                getPlayer().getPulseManager().run(new Pulse() {

                    @Override
                    public boolean pulse() {
                        getPlayer().getPulseManager().run(new MovementPulse(getPlayer(), walkLocations[lastWalkIndex], getPlayer().getWalkingQueue().isRunning()) {

                            @Override
                            public boolean pulse() {
                                return true;
                            }
                        }, "movement");
                        return false;
                    }
                });
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
        return "AutoRunecraft";
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
