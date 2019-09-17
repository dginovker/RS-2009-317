package org.gielinor.game.content.activity.minigame;

import org.gielinor.game.content.activity.ActivityPlugin;
import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.world.map.zone.ZoneBorders;
import org.gielinor.game.world.map.zone.ZoneRestriction;
import org.gielinor.rs2.pulse.PulseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is an abstract mini-game representation.
 * It contains (optional) behaviour that provides structure for the game.
 *
 * Created by Stan van der Bend on 24/11/2017.
 *
 * project: runeworld-game-server
 * package: runeworld.world.global.content.minigames
 */
public abstract class Minigame extends ActivityPlugin{

    private List<Entity> participants = new ArrayList<>();

    /**
     * Constructs a new {@code ActivityPlugin} {@code Object}.
     *
     * @param name         The name.
     * @param instanced    If the activity is instanced.
     * @param multicombat  If the activity is multicombat.
     * @param safe         If the activity is safe (the player does not lose his/her items).
     * @param restrictions
     */
    Minigame(String name, boolean instanced, boolean multicombat, boolean safe, ZoneRestriction... restrictions) {
        super(name, instanced, multicombat, safe, restrictions);
    }

    @Override public void configure() {
        borders().ifPresent(this::register);
    }

    @Override
    public boolean death(Entity e, Entity killer) {

        return super.death(e, killer);
    }

    /**
     * Executes when entering the game.
     *
     * @param player the entering player.
     */
    protected abstract void enter(Player player);

    /**
     * Executes upon player leaving the game.
     *
     * @param player the leaving player.
     */
    protected abstract void leave(Player player);

    /**
     * Adds the given {@link Entity} to the {@link Minigame#participants}.
     *
     * @param Entity the new participant.
     */
    protected void addParticipant(Entity Entity){
        participants.add(Entity);
    }

    /**
     * Is the given {@link Entity} present in the {@link Minigame#participants}.
     *
     * @param Entity the potential participant.
     *
     * @return {@code true} when the {@link Entity} is present and {@code false} if not.
     */
    public boolean contains(Entity Entity) {
        return participants.contains(Entity);
    }

    /**
     * Provide structure and define the general behaviour of this {@link Minigame} instance.
     */
    public abstract PulseBuilder<Minigame> buildGameTask();

    /**
     * Provide optional {@link ZoneBorders} to be registered the {@link Entity}.
     *
     * @return {@link Optional#empty()} if no borders are provided.
     */
    public abstract Optional<ZoneBorders[]> borders();

    /**
     * The provided identifier will be used for hashing of this {@link Minigame} instance.
     *
     * @return a {@link String} representation of this {@link Minigame}.
     */
    public abstract String identifier();

    /**
     * The participants contains all involved {@link Entity}s, both the antagonists and the protagonists.
     *
     * @return this {@link Minigame#participants}.
     */
    protected List<Entity> getParticipants() {
        return participants;
    }

    /**
     * The {@link Minigame#participants} filtered and mapped to return
     *
     * @return a {@link List<Player>} derived from the {@link Minigame#participants}.
     */
    public List<Player> getParticipatingPlayers() {
        return participants.stream()
                .filter(entity -> entity instanceof Player)
                .map(Entity::asPlayer)
                .collect(Collectors.toList());
    }
}
