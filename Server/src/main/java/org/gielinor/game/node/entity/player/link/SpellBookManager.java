package org.gielinor.game.node.entity.player.link;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.parser.player.SavingModule;

/**
 * Represents a managing class of a players spell book.
 *
 * @author 'Vexia
 */
public class SpellBookManager implements SavingModule {

    /**
     * Represents the current interface if of the spellbook.
     */
    private int spellBook = SpellBook.MODERN.getInterfaceId();

    /**
     * Constructs a new {@code SpellBookManager} {@code Object}.
     */
    public SpellBookManager() {
        /**
         * empty.
         */
    }

    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.putInt(spellBook);
    }

    @Override
    public void parse(ByteBuffer byteBuffer) {
        if (byteBuffer.hasRemaining()) {
            setSpellBook(SpellBook.forInterface(byteBuffer.getInt()));
        }
    }

    /**
     * Sets the spell book.
     *
     * @param book
     */
    public void setSpellBook(SpellBook book) {
        this.spellBook = book.getInterfaceId();
    }

    /**
     * Gets the spell book.
     *
     * @return the spellBook
     */
    public int getSpellBook() {
        return spellBook;
    }

    /**
     * Represents a characters spellbook.
     *
     * @author 'Vexia
     * @author Emperor
     */
    public static enum SpellBook {

        MODERN(1151, 1829),
        ANCIENT(12855, 1689),
        LUNAR(29999);

        /**
         * The interface id of this spell book.
         */
        private final int interfaceId;
        /**
         * The autocast interface id.
         */
        private int autocastId;
        /**
         * The spells mapping.
         */
        private final Map<Integer, MagicSpell> spells = new HashMap<>();

        /**
         * Creates the spell book.
         *
         * @param interfaceId The spellbook interface id.
         * @param autocastId  The autocast interface id.
         */
        private SpellBook(int interfaceId, int autocastId) {
            this.interfaceId = interfaceId;
            this.autocastId = autocastId;
        }

        /**
         * Creates the spell book.
         *
         * @param interfaceId The spellbook interface id.
         */
        private SpellBook(int interfaceId) {
            this(interfaceId, -1);
        }

        /**
         * @return The interfaceId.
         */
        public int getInterfaceId() {
            return interfaceId;
        }

        /**
         * Gets the autocast interface id.
         *
         * @return The autocast interface id.
         */
        public int getAutocastId() {
            return autocastId;
        }

        /**
         * Method used to get the book.
         *
         * @param id the id.
         */
        public static SpellBook forInterface(final int id) {
            for (SpellBook book : SpellBook.values()) {
                if (book.interfaceId == id) {
                    return book;
                }
            }
            return MODERN;
        }

        /**
         * Registers a new spell.
         *
         * @param buttonId The button id.
         * @param spell    The spell.
         */
        public void register(int buttonId, MagicSpell spell) {
            spell.setSpellId(buttonId);
            spells.put(buttonId, spell);
        }

        /**
         * Gets a spell from the spellbook.
         *
         * @param buttonId The button id.
         * @return The spell.
         */
        public MagicSpell getSpell(int buttonId) {
            return spells.get(buttonId);
        }
    }

}