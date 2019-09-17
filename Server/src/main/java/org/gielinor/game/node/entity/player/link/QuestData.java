package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;

import org.gielinor.parser.player.SavingModule;

/**
 * Represents the quest data to save.
 *
 * @author 'Vexia
 */
public final class QuestData implements SavingModule {

    /**
     * Represents fields to be inserted into the database.
     */
    public static final String[] FIELDS = new String[]{
        "pidn", "killed_tree_spirit"
    };
    /**
     * Whether or not the player has killed the Tree Spirit.
     */
    private boolean killedTreeSpirit;

    /**
     * Constructs a new {@code QuestData} {@code Object}.
     */
    public QuestData() {

    }

    @Override
    public void save(ByteBuffer buffer) {
        SavedData.save(buffer, killedTreeSpirit, 1);
        buffer.put((byte) 0);
    }

    @Override
    public void parse(ByteBuffer buffer) {
        int opcode;
        while ((opcode = buffer.get()) != 0) {
            switch (opcode) {
                case 1:
                    killedTreeSpirit = SavedData.getBoolean(buffer.get());
                    break;
            }
        }
    }

    /**
     * Gets whether or not the player has killed the Tree Spirit.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasKilledTreeSpirit() {
        return killedTreeSpirit;
    }

    /**
     * Gets whether or not the player has killed the Tree Spirit.
     *
     * @param killedTreeSpirit Whether or not they have killed the tree spirit.
     */
    public void setKilledTreeSpirit(boolean killedTreeSpirit) {
        this.killedTreeSpirit = killedTreeSpirit;
    }
}
