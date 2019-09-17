package org.gielinor.game.world.update;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.gielinor.game.node.entity.Entity;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.game.world.repository.InitializingNodeList;
import org.gielinor.game.world.repository.Repository;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;

/**
 * The entity update sequence.
 * @author Emperor
 *
 */
public final class UpdateSequence {

    /**
     * The list of active players.
     */
    private static final InitializingNodeList<Player> RENDERABLE_PLAYERS = new InitializingNodeList<>();

    /**
     * Represents the active NPCs.
     */
    private static final InitializingNodeList<NPC> RENDERABLE_NPCS = new InitializingNodeList<>();

    /**
     * The executor used.
     */
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Constructs a new {@code ParallelUpdatingSequence} {@code Object}.
     */
    public UpdateSequence() {
        /*
         * empty.
         */
    }

    /**
     * Starts the update sequence.
     * @return <code>True</code> if we should continue.
     */
    public boolean start() {
        try {
            for (Player p : Repository.getLobbyPlayers()) {
                PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(p));
            }
            for (Player p : getRenderablePlayers()) {
                try {
                    p.tick();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            for (NPC n : getRenderableNpcs()) {
                n.tick();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Runs the updating part of the sequence.
     */
    public void run() {
        final CountDownLatch latch = new CountDownLatch(getRenderablePlayers().size());
        for (final Player p : getRenderablePlayers()) {
            // if (!Repository.getPlayers().contains(p)) {
            // System.err.println("Nulled player " + p.getName() + "!");
            // }
            EXECUTOR.execute(() -> {
                try {
                    p.update();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                latch.countDown();
            });
        }
        try {
            latch.await(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends the sequence, calls the {@link Entity#reset()} method..
     */
    public void end() {
        for (Player p : getRenderablePlayers()) {
            p.reset();
        }
        for (NPC npc : getRenderableNpcs()) {
            npc.reset();
        }
        getRenderablePlayers().sync();
        getRenderableNpcs().sync();
        RegionManager.pulse();
        GroundItemManager.pulse();
    }

    /**
     * Terminates the update sequence.
     */
    public final void terminate() {
        EXECUTOR.shutdown();
    }

    /**
     * Gets the renderableNpcs.
     * @return The renderableNpcs.
     */
    public static InitializingNodeList<NPC> getRenderableNpcs() {
        return RENDERABLE_NPCS;
    }

    /**
     * Gets the renderablePlayers.
     * @return The renderablePlayers.
     */
    public static InitializingNodeList<Player> getRenderablePlayers() {
        return RENDERABLE_PLAYERS;
    }
}
