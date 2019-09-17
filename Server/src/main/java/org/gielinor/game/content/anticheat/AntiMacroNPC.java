package org.gielinor.game.content.anticheat;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.combat.CombatStyle;
import org.gielinor.game.node.entity.npc.AbstractNPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.map.path.Pathfinder;
import org.gielinor.game.world.update.flag.context.Graphics;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Handles an anti macro npc.
 *
 * @author Vexia
 */
public abstract class AntiMacroNPC extends AbstractNPC {

    /**
     * The player.
     */
    protected final Player player;

    /**
     * The anti macro event.
     */
    protected final AntiMacroEvent event;

    /**
     * The quotes the npc will say(null if none)
     */
    private String[] quotes;

    /**
     * The counter representing speech cycles.
     */
    private int count;

    /**
     * The time until the next speech.
     */
    private int nextSpeech;

    /**
     * If the players time is up.
     */
    protected boolean timeUp;

    /**
     * Constructs a new {@code AntiMacroNPC} {@code Object}.
     *
     * @param id       the id.
     * @param location the location.
     * @param player   the player.
     */
    public AntiMacroNPC(int id, Location location, AntiMacroEvent event, Player player, String... quotes) {
        super(id, location);
        this.event = event;
        this.player = player;
        this.quotes = quotes;
    }

    @Override
    public void init() {
        if (event == null) {
            super.clear();
            return;
        }
        location = RegionManager.getSpawnLocation(player, this);
        if (location == null) {
            clear();
            event.terminate();
            return;
        }
        super.init();
        startFollowing();
    }

    @Override
    public void handleTickActions() {
        if (!getLocks().isMovementLocked()) {
            if (getDialoguePlayer() == null || !getDialoguePlayer().isActive() || !getDialoguePlayer().getInterfaceState().hasChatbox()) {
                setDialoguePlayer(null);
            }
        }
        if (!player.isActive() || !getLocation().withinDistance(player.getLocation(), 10)) {
            handlePlayerLeave();
        }
        if (!getPulseManager().hasPulseRunning()) {
            startFollowing();
        }
        if (quotes != null) {
            if (nextSpeech < World.getTicks() && this.getDialoguePlayer() == null && !this.getLocks().isMovementLocked()) {
                if (count > quotes.length - 1) {
                    return;
                }
                sendChat(quotes[count].replace("@name", player.getUsername()).replace("@gender", event.getGenderPrefix()).replace("@gL", event.getGenderPrefix()));
                nextSpeech = (int) (World.getTicks() + (20 / 0.5));
                if (++count >= quotes.length) {
                    setTimeUp(true);
                    handleTimeUp();
                    return;
                }
            }
        }
    }

    /**
     * Called when the player is gone.
     */
    public void handlePlayerLeave() {
        clear();
    }

    /**
     * Called when the quotes are finished.
     */
    public void handleTimeUp() {
        teleport();
    }

    @Override
    public boolean isAttackable(Entity entity, CombatStyle style) {
        if (entity instanceof Player && entity != player) {
            ((Player) entity).getActionSender().sendMessage("Nothing interesting happens.");
            return false;
        }
        return super.isAttackable(entity, style);
    }

    @Override
    public void onRegionInactivity() {
        super.onRegionInactivity();
        clear();
    }

    @Override
    public void clear() {
        super.clear();
        if (event != null) {
            event.terminate();
        }
    }

    @Override
    public AbstractNPC construct(int id, Location location, Object... objects) {
        return null;
    }

    /**
     * Teleports the player away.
     */
    public void teleport() {
        player.lock();
        World.submit(new Pulse(1, player) {

            int count;

            @Override
            public boolean pulse() {
                switch (++count) {
                    case 1:
                        clear();
                        player.getProperties().setTeleportLocation(AntiMacroEvent.getRandomLocation());
                        break;
                    case 2:
                        player.unlock();
                        event.noteItems();
                        player.graphics(Graphics.create(86));
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Starts following the player.
     */
    public void startFollowing() {
        getPulseManager().run(new MovementPulse(this, player, Pathfinder.DUMB) {

            @Override
            public boolean pulse() {
                return false;
            }
        }, "movement");
        face(player);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        return super.newInstance(arg);
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the event.
     *
     * @return The event.
     */
    public AntiMacroEvent getEvent() {
        return event;
    }

    /**
     * Gets the timeUp.
     *
     * @return The timeUp.
     */
    public boolean isTimeUp() {
        return timeUp;
    }

    /**
     * Sets the timeUp.
     *
     * @param timeUp The timeUp to set.
     */
    public void setTimeUp(boolean timeUp) {
        this.timeUp = timeUp;
    }

}
