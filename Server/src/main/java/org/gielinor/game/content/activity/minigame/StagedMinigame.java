package org.gielinor.game.content.activity.minigame;

import org.gielinor.game.world.map.zone.ZoneRestriction;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Besides the inherited behaviour of {@link Minigame}, this class provides staging mechanisms.
 *
 * Created by Stan van der Bend on 24/11/2017.
 *
 * project: runeworld-game-server
 * package: runeworld.world.global.content.minigames
 */
public abstract class StagedMinigame extends Minigame {

    private Stage stage = Stage.PRE_GAME;

    /**
     * Constructs a new {@code ActivityPlugin} {@code Object}.
     *
     * @param name         The name.
     * @param instanced    If the activity is instanced.
     * @param multicombat  If the activity is multicombat.
     * @param safe         If the activity is safe (the player does not lose his/her items).
     * @param restrictions
     */
    public StagedMinigame(String name, boolean instanced, boolean multicombat, boolean safe, ZoneRestriction... restrictions) {
        super(name, instanced, multicombat, safe, restrictions);
    }

    protected Predicate<StagedMinigame> hasStarted(){
        return thisGame -> !thisGame.stage.equals(Stage.PRE_GAME);
    }

    public Consumer<StagedMinigame> set(Stage next){
        return thisGame -> thisGame.stage = next;
    }

    public Stage getStage() {
        return stage;
    }

    public enum Stage{
        PRE_GAME,
        GAME,
        POST_GAME
    }

}

