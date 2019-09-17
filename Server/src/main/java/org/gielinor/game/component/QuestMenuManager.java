package org.gielinor.game.component;

import java.util.List;

import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceMaxScrollContext;
import org.gielinor.net.packet.out.InterfaceMaxScrollPacket;

/**
 * Manages the quest menu text.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class QuestMenuManager {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The title.
     */
    private String title;

    /**
     * The {@link org.gielinor.game.component.CloseEvent}.
     */
    private CloseEvent closeEvent;

    /**
     * The lines.
     */
    private String[] lines;

    /**
     * Constructs a new {@link QuestMenuManager} with a
     * {@link org.gielinor.game.component.CloseEvent}.
     *
     * @param player
     *            The player.
     */
    public QuestMenuManager(Player player) {
        this.player = player;
    }

    /**
     * Gets the title.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            The title.
     * @return The {@code QuestMenuManager} for chaining.
     */
    public QuestMenuManager setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets the {@link org.gielinor.game.component.CloseEvent}.
     *
     * @return The {@link #closeEvent}.
     */
    public CloseEvent getCloseEvent() {
        return closeEvent;
    }

    /**
     * Sets the {@link org.gielinor.game.component.CloseEvent}.
     *
     * @param closeEvent
     *            The {@link #closeEvent}.
     * @return The {@code QuestMenuManager} for chaining.
     */
    public QuestMenuManager setCloseEvent(CloseEvent closeEvent) {
        this.closeEvent = closeEvent;
        return this;
    }

    /**
     * Gets the lines.
     *
     * @return The lines.
     */
    public String[] getLines() {
        return lines;
    }

    /**
     * Sets the lines.
     *
     * @param lines
     *            The lines.
     * @return The {@code QuestMenuManager} for chaining.
     */
    public QuestMenuManager setLines(String... lines) {
        this.lines = lines;
        return this;
    }

    /**
     * Sets the lines.
     *
     * @param lines
     *            The lines.
     * @return The {@code QuestMenuManager} for chaining.
     */
    public QuestMenuManager setLines(List<String> lines) {
        this.lines = lines.toArray(new String[lines.size()]);
        return this;
    }

    /**
     * Clears the quest menu.
     */
    public void clear() {
        for (int textId = 46754; textId < 46854; textId++) {
            player.getActionSender().sendString(textId, "");
        }
    }

    /**
     * Sends this {@link QuestMenuManager}.
     */
    public void send() {
        player.getInterfaceState().close();
        if (lines.length > 99) {
            throw new IllegalArgumentException("Too many quest lines: " + lines.length + ".");
        }
        clear();
        PacketRepository.send(InterfaceMaxScrollPacket.class,
            new InterfaceMaxScrollContext(player, 46752, (20 * lines.length)));
        if (title != null) {
            player.getActionSender().sendString(46753, title);
        }
        int index = 0;
        for (int textId = 46754; textId < (46754 + lines.length); textId++) {
            player.getActionSender().sendString(textId, lines[index]);
            index++;
        }
        player.getInterfaceState().open(new Component(46750).setCloseEvent(getCloseEvent()));
        reset();
    }

    /**
     * Resets this {@code QuestMenuInterface}.
     */
    public void reset() {
        this.title = null;
        this.closeEvent = null;
        this.lines = null;
    }

}
