package org.gielinor.game.content.skill.member.farming.compost;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.parser.player.SavingModule;


/**
 * Represents the managing class of compost bins.
 *
 * @author 'Vexia
 * @version 1.0
 */
public final class CompostManager implements SavingModule {

    /**
     * Represents the list of compost bins.
     */
    private final List<CompostBin> bins = new ArrayList<>();

    /**
     * Constructs a new {@code CompostBinManager} {@code Object}.
     */
    public CompostManager() {
        /**
         * empty.
         */
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) bins.size());
        for (CompostBin bin : bins) {
            byteBuffer.putInt(bin.getWrapperId());
            if (bin.getTimeStamp() != 0L) {
                byteBuffer.put((byte) 1);
                byteBuffer.putLong(bin.getTimeStamp());
            }
            if (bin.getContainer().itemCount() != 0) {
                byteBuffer.put((byte) 2);
                bin.getContainer().save(byteBuffer);
            }
            byteBuffer.put((byte) 0);
        }
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        int size = byteBuffer.get();
        int opcode;
        for (int i = 0; i < size; i++) {
            CompostBin bin = new CompostBin(byteBuffer.getInt());
            while ((opcode = byteBuffer.get() & 0xFF) != 0) {
                switch (opcode) {
                    case 1:
                        bin.setTimeStamp(byteBuffer.getLong());
                        break;
                    case 2:
                        bin.getContainer().parse(byteBuffer);
                        break;
                }
            }
            bins.add(bin);
        }
    }

    /**
     * Method used to fill a compost.
     *
     * @param player the player.
     * @param object the object.
     * @param option the option.
     * @param delay  the delay
     */
    public void fill(final Player player, final Item item, final GameObject object, String option, int delay) {
        CompostBin bin = getBin(object);
        if (option.equals("bin")) {
            bin.fillBin(player, item, delay);
        } else {
            bin.fillBucket(player, delay);
        }
    }

    /**
     * Gets the compost bin by the object id.
     *
     * @param objectId The object id.
     * @return The bin.
     */
    public CompostBin getBin(final int objectId) {
        for (CompostBin bin : getBins()) {
            if (bin.getWrapperId() == objectId) {
                return bin;
            }
        }
        return null;
    }

    /**
     * Gets the compost bin by the object wrapper.
     *
     * @param object the object.
     * @return the wrapper.
     */
    public CompostBin getBin(final GameObject object) {
        final GameObject wrapper = object.getWrapper();
        for (CompostBin bin : getBins()) {
            if (bin.getWrapperId() == wrapper.getId()) {
                return bin;
            }
        }
        CompostBin bin = new CompostBin(object.getWrapper().getId());
        bins.add(bin);
        return bin;
    }

    /**
     * Gets the bins.
     *
     * @return The bins.
     */
    public List<CompostBin> getBins() {
        return bins;
    }

}
