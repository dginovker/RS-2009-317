package org.gielinor.game.content.skill.member.farming.pot;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.pulse.Pulse;

/**
 * A managing class for the growth of seedlings.
 *
 * @author 'Vexia
 */
public final class SeedlingManager implements SavingModule {

    /**
     * Represents the list of active seedlings.
     */
    private final List<Item> seedlings = new ArrayList<>();

    /**
     * Represents the seedling pulse.
     */
    private SeedlingPulse pulse = new SeedlingPulse();

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Constructs a new {@code SeedlingManager} {@code Object}.
     *
     * @param player the player.
     */
    public SeedlingManager(final Player player) {
        this.player = player;
    }

    /**
     * Parses the player's seedlings.
     */
    public void parse() {
        getSeedling(player.getInventory());
        getSeedling(player.getBank());
        World.submit(pulse);
    }

    /**
     * Method used to add a seedling to the manager.
     *
     * @param seedling the seedling.
     */
    public void addSeedling(final Item seedling) {
        seedling.setCharge(1001);
        seedlings.add(seedling);
        if (!pulse.isRunning()) {
            World.submit((pulse = new SeedlingPulse()));
        }
    }

    /**
     * Gets the active seedlings from a container.
     *
     * @param container the container.
     */
    private void getSeedling(Container container) {
        Saplings sap = null;
        for (Item item : container.toArray()) {
            if (item == null) {
                continue;
            }
            if (item.getCharge() < 1001) {
                continue;
            }
            sap = Saplings.forSeedling(item);
            if (sap != null) {
                seedlings.add(item);
            }
        }
    }

    /**
     * Gets the seedlings.
     *
     * @return The seedlings.
     */
    public List<Item> getSeedlings() {
        return seedlings;
    }

    /**
     * Represents the seedling growth pulse.
     *
     * @author 'Vexia
     */
    public final class SeedlingPulse extends Pulse {

        /**
         * Constructs a new {@code SeedlingPulse} {@code Object}.
         */
        public SeedlingPulse() {
            super(10, player);
        }

        @Override
        public boolean pulse() {
            final boolean invy = updateSeedling(player.getInventory());
            final boolean bank = updateSeedling(player.getBank());
            return !invy && !bank;
        }

        /**
         * Method used to update the seedlings.
         *
         * @param container the container.
         */
        private boolean updateSeedling(Container container) {
            Saplings sapling = null;
            boolean updated = false;
            for (Item seedling : container.toArray()) {
                if (seedling == null) {
                    continue;
                }
                if (seedling.getCharge() < 1001) {
                    continue;
                }
                sapling = Saplings.forSeedling(seedling);
                if (sapling == null) {
                    continue;
                }
                updated = true;
                int minute = 1005 - seedling.getCharge();
                if (minute == 0) {
                    seedlings.remove(seedling);
                    container.remove(new Item(seedling.getId(), 1));
                    container.add(sapling.getSapling());
                } else {
                    seedling.setCharge(seedling.getCharge() + 1);
                }
            }
            return updated;
        }
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        getSeedling(player.getInventory());
        getSeedling(player.getBank());
        if (seedlings.size() > 0) {
            World.submit(pulse);
        }
    }
}
