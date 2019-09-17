package org.gielinor.game.content.skill.member.farming.wrapper;

import java.nio.ByteBuffer;

import org.gielinor.cache.def.impl.ConfigFileDefinition;
import org.gielinor.cache.def.impl.ObjectDefinition;
import org.gielinor.game.content.skill.member.farming.FarmingNode;
import org.gielinor.game.content.skill.member.farming.FarmingPatch;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.parser.player.SavingModule;

/**
 * A representation of a farming patch.
 *
 * @author 'Vexia
 */
public final class PatchWrapper implements SavingModule {

    /**
     * Represents the patch cycle handler.
     */
    private final PatchCycle cycle;

    /**
     * Represents the patch interactor handler.
     */
    private final PatchInteractor interactor;

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Represents the farming patch type.
     */
    private final FarmingPatch patch;

    /**
     * Represents the farming node on the patch.
     */
    private FarmingNode node;

    /**
     * Represents the wrapper id of the object.
     */
    private final int wrapperId;

    /**
     * Constructs a new {@code PatchWrapper} {@code Object}.
     *
     * @param wrapperId the wrapper id.
     */
    public PatchWrapper(final Player player, final int wrapperId) {
        this.player = player;
        this.wrapperId = wrapperId;
        this.patch = FarmingPatch.forObject(wrapperId);
        this.cycle = new PatchCycle(this);
        this.interactor = new PatchInteractor(this);
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.putInt(wrapperId);
        cycle.save(byteBuffer.put((byte) 1));
        if (node != null) {
            byteBuffer.put((byte) 2);
            byteBuffer.putInt(patch.getNodePosition(node));
        }
        byteBuffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    cycle.parse(byteBuffer);
                    break;
                case 2:
                    int ind = byteBuffer.getInt();
                    node = patch.getNodes()[ind];
                    break;
            }
        }
    }

    /**
     * Checks if the patch is set default.
     *
     * @return <code>True</code> if so.
     */
    public boolean isDefault() {
        return getState() == 0;
    }

    /**
     * Checks if the patch has weeds.
     *
     * @return <code>True</code> if so.
     */
    public boolean isWeedy() {
        return getState() < 3 || hasScarecrow();
    }

    /**
     * Checks if the patch is empty.
     *
     * @return <code>True</code> if so.
     */
    public boolean isEmpty() {
        return getState() == 3 || (getPatch() == FarmingPatch.FLOWER && getState() == 36);
    }

    /**
     * Checks if the patch has a scarecrow.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasScarecrow() {
        return getPatch() == FarmingPatch.FLOWER && getState() > 32 && getState() < 37;
    }

    /**
     * Gets the patches name.
     *
     * @return the name.
     */
    public String getName() {
        if (patch == null) {
            return "invalid";
        }
        return patch.getName();
    }

    /**
     * Method used to add a config value.
     *
     * @param value the value.
     */
    public void addConfigValue(final int value) {
        player.getConfigManager().set(getConfigId(), (player.getConfigManager().get(getConfigId()) - getConfigValue()) + (value << getBitShift()), true);
    }

    /**
     * Method used to get the config value of this compost bin.
     *
     * @return the value set.
     */
    public int getConfigValue() {
        return getState() << getBitShift();
    }

    /**
     * Gets the wrapper config state.
     *
     * @return <code>True</code> if so.
     */
    public int getState() {
        ConfigFileDefinition def = ConfigFileDefinition.forId(ObjectDefinition.forId(wrapperId).getConfigFileId());
        if (def == null) {
            return 0;
        }
        return def.getValue(player);
    }

    /**
     * Gets the config id.
     *
     * @return the id.
     */
    public int getConfigId() {
        return ConfigFileDefinition.forId(ObjectDefinition.forId(wrapperId).getConfigFileId()).getConfigId();
    }

    /**
     * Gets the bitshift for the wrapper.
     *
     * @return the bitshift.
     */
    public int getBitShift() {
        return ConfigFileDefinition.forId(ObjectDefinition.forId(wrapperId).getConfigFileId()).getBitShift();
    }

    /**
     * Gets the patch.
     *
     * @return The patch.
     */
    public FarmingPatch getPatch() {
        return patch;
    }

    /**
     * Gets the wrapperId.
     *
     * @return The wrapperId.
     */
    public int getWrapperId() {
        return wrapperId;
    }

    /**
     * Gets the interactor.
     *
     * @return The interactor.
     */
    public PatchInteractor getInteractor() {
        return interactor;
    }

    /**
     * Gets the cycle.
     *
     * @return The cycle.
     */
    public PatchCycle getCycle() {
        return cycle;
    }

    /**
     * Gets the node.
     *
     * @return The node.
     */
    public FarmingNode getNode() {
        return node;
    }

    /**
     * Sets the node.
     *
     * @param node The node to set.
     */
    public void setNode(FarmingNode node) {
        this.node = node;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

}
