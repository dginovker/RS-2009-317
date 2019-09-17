package org.gielinor.game.content.skill.member.farming;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gielinor.game.content.skill.member.farming.compost.CompostManager;
import org.gielinor.game.content.skill.member.farming.pot.SeedlingManager;
import org.gielinor.game.content.skill.member.farming.wrapper.PatchWrapper;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;

/**
 * Represents a managing class used for farming.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class FarmingManager implements SavingModule {

    /**
     * Represents the store of your farming equipment.
     */
    private final FarmingEquipment equipment = new FarmingEquipment();

    /**
     * Represents the managing class of composts.
     */
    private final CompostManager compostManager = new CompostManager();

    /**
     * Represents the farming patch representations if active.
     */
    private final List<PatchWrapper> patches = new ArrayList<>();

    /**
     * Represents the managing class of seedlings.
     */
    private final SeedlingManager seedlingManager;

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Constructs a new {@code FarmingManager} {@code Object}.
     *
     * @param player the player.
     */
    public FarmingManager(final Player player) {
        this.player = player;
        this.seedlingManager = new SeedlingManager(player);
    }

    /**
     * Method used to cycle the life of patches.
     */
    public void cycle() {
        final Iterator<PatchWrapper> iterator = patches.iterator();
        PatchWrapper wrapper;
        while (iterator.hasNext()) {
            wrapper = iterator.next();
            if (wrapper.isDefault()) {
                continue;
            }
            wrapper.getCycle().cycle(player);
        }
    }

    /**
     * Gets the patch wrapper by the id.
     *
     * @param wrapperId   the id.
     * @param newInstance if a new one should be created.
     * @return the wrapper.
     */
    public PatchWrapper getPatchWrapper(final int wrapperId, boolean newInstance) {
        for (PatchWrapper patch : patches) {
            if (patch.getWrapperId() == wrapperId) {
                return patch;
            }
        }
        if (!newInstance) {
            return null;
        }
        final PatchWrapper patch = new PatchWrapper(player, wrapperId);
        patches.add(patch);
        return patch;
    }

    /**
     * Gets the patch wrapper by the id.
     *
     * @param wrapperId the id.
     * @return the wrapper.
     */
    public PatchWrapper getPatchWrapper(final int wrapperId) {
        return getPatchWrapper(wrapperId, true);
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
     * Gets the equipment.
     *
     * @return The equipment.
     */
    public FarmingEquipment getEquipment() {
        return equipment;
    }

    /**
     * Gets the patches.
     *
     * @return The patches.
     */
    public List<PatchWrapper> getPatches() {
        return patches;
    }

    /**
     * Gets the compostManager.
     *
     * @return The compostManager.
     */
    public CompostManager getCompostManager() {
        return compostManager;
    }

    /**
     * Gets the seedlingManager.
     *
     * @return The seedlingManager.
     */
    public SeedlingManager getSeedlingManager() {
        return seedlingManager;
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        if (equipment.getContainer().itemCount() != 0) {
            equipment.save(byteBuffer.put((byte) 1));
        }
        if (compostManager.getBins().size() != 0) {
            compostManager.save(byteBuffer.put((byte) 2));
        }
        if (patches.size() != 0) {
            byteBuffer.put((byte) 3);
            byteBuffer.putInt(patches.size());
            for (PatchWrapper wrapper : patches) {
                wrapper.save(byteBuffer);
            }
        }
        if (seedlingManager.getSeedlings().size() > 0) {
            seedlingManager.save(byteBuffer.put((byte) 4));
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    equipment.parse(byteBuffer);
                    break;
                case 2:
                    compostManager.parse(byteBuffer);
                    break;
                case 3:
                    int size = byteBuffer.getInt();
                    PatchWrapper wrapper;
                    for (int i = 0; i < size; i++) {
                        wrapper = new PatchWrapper(player, byteBuffer.getInt());
                        wrapper.parse(byteBuffer);
                        patches.add(wrapper);
                    }
                    break;
                case 4:
                    seedlingManager.parse(byteBuffer);
                    break;
            }
        }
    }
}
