package org.gielinor.game.content.dialogue;

import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.plugin.PluginManifest;
import org.gielinor.rs2.plugin.PluginType;

/**
 * Represents a dialogue plugin.
 *
 * @author Emperor
 */
@PluginManifest(type = PluginType.DIALOGUE)
public abstract class DialoguePlugin implements Plugin<Player> {

    /**
     * The player.
     */
    protected Player player;

    /**
     * The dialogue interpreter.
     */
    protected DialogueInterpreter interpreter;

    /**
     * Two options interface.
     */
    protected final int TWO_OPTIONS = 228;

    /**
     * Three options interface.
     */
    protected final int THREE_OPTIONS = 230;

    /**
     * Four options interface.
     */
    protected final int FOUR_OPTIONS = 232;

    /**
     * Five options interface.
     */
    protected final int FIVE_OPTIONS = 234;

    /**
     * The first button option.
     */
    protected final int OPTION_1 = 1;

    /**
     * The second button option.
     */
    protected final int OPTION_2 = 2;

    /**
     * The third button option.
     */
    protected final int OPTION_3 = 3;

    /**
     * The fourth button option.
     */
    protected final int OPTION_4 = 4;

    /**
     * The fifth button option.
     */
    protected final int OPTION_5 = 5;

    /**
     * Represents the red string.
     */
    protected static final String RED = "<col=8A0808>";

    /**
     * Represents the blue string.
     */
    protected static final String BLUE = "<col=08088A>";

    /**
     * The stage for ending a dialogue.
     */
    protected final int END = (1 << 16 | 100571);

    /**
     * The NPC the player is talking with.
     */
    protected NPC npc;

    /**
     * The current dialogue stage.
     */
    protected int stage;

    /**
     * If the dialogue is finished.
     */
    protected boolean finished;

    /**
     * Constructs a new {@code DialoguePlugin} {@code Object}.
     */
    public DialoguePlugin() {
        /*
         * empty.
         */
    }

    /**
     * Constructs a new {@code DialoguePlugin} {@code Object}.
     *
     * @param player The player.
     */
    public DialoguePlugin(Player player) {
        this.player = player;
        this.interpreter = player.getDialogueInterpreter();
    }

    /**
     * Initializes this plugin.
     */
    public void init() {
        for (int id : getIds()) {
            DialogueInterpreter.add(id, this);
        }
    }

    /**
     * Closes <b>(but does not end)</b> the dialogue.
     *
     * @return <code>True</code> if the dialogue succesfully closed.
     */
    public boolean close() {
        player.getInterfaceState().closeChatbox();
        finished = true;
        return true;
    }

    /**
     * Ends the dialogue.
     */
    public void end() {
        if (interpreter != null) {
            interpreter.close();
        }
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public abstract DialoguePlugin newInstance(Player player);

    /**
     * Opens the dialogue.
     *
     * @param args The arguments.
     * @return <code>True</code> if the dialogue plugin succesfully opened.
     */
    public abstract boolean open(Object... args);

    /**
     * Handles the progress of this dialogue..
     *
     * @return {@code True} if the dialogue has started.
     */
    public boolean handle(int interfaceId, int buttonId) {
        return false;
    }

    /**
     * Handles the progress of this dialogue with an {@code OptionSelect}.
     *
     * @param interfaceId  The id of the interface.
     * @param optionSelect The {@link org.gielinor.game.content.dialogue.OptionSelect}.
     * @return <code>True</code> if the dialogue has started.
     */
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        return false;
    }

    /**
     * Gets the ids of the NPCs using this dialogue plugin.
     *
     * @return The array of NPC ids.
     */
    public abstract int[] getIds();

    /**
     * Method wrapper to send an npc dial.
     *
     * @return The component.
     */
    public Component npc(final String... messages) {
        if (npc == null) {
            return interpreter.sendDialogues(getIds()[0], FacialExpression.NO_EXPRESSION, messages);
        }
        return interpreter.sendDialogues(npc, null, messages);
    }

    /**
     * Method wrapper to send an npc dialogue.
     *
     * @param id       The id.
     * @param messages The messages.
     * @return The component.
     */
    public Component npc(int id, final String... messages) {
        return interpreter.sendDialogues(id, FacialExpression.NO_EXPRESSION, messages);
    }

    /**
     * Method wrapper to send a player dial.
     *
     * @return the component.
     */
    public Component player(final String... messages) {
        return interpreter.sendDialogues(player, FacialExpression.NORMAL, messages);
    }

    /**
     * Method used to send options.
     *
     * @param options the options.
     */
    public void options(final String... options) {
        interpreter.sendOptions("Select an Option", options);
    }

    /**
     * Method used to send options.
     *
     * @param title the title of the option dialogue
     * @param options the options.
     */
    public void optionsWithCustomTitle(final String title, final String... options) {
        interpreter.sendOptions(title, options);
    }

    /**
     * Checks if the dialogue plugin is finished.
     *
     * @return <code>True</code> if so.
     */
    public boolean isFinished() {
        return finished;
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
