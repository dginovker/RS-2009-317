package org.gielinor.game.content.global.quest;

import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;

/**
 * Represents a quest.
 *
 * @author 'Vexia
 */
public abstract class Quest {

    /**
     * Represents the red string.
     */
    protected static final String RED = "<col=8A0808>";

    /**
     * Represents the blue string.
     */
    protected static final String BLUE = "<col=08088A>";

    /**
     * The constant representing the journal component.
     */
    protected final int JOURNAL_COMPONENT = 275;

    /**
     * The constant representing the quest reward component.
     */
    protected final int REWARD_COMPONENT = 12140;

    /**
     * The constant representing the quest reward name child.
     */
    protected final int REWARD_COMPONENT_NAME = 12144;

    /**
     * The constant representing the quest reward item child.
     */
    protected final int REWARD_COMPONENT_ITEM = 12145;

    /**
     * The constant representing the quest reward quest points child.
     */
    protected final int REWARD_COMPONENT_POINTS = 12147;

    /**
     * The reward component string ids.
     */
    protected final int[] REWARD_IDS = new int[]{ 12150, 12151, 12152, 12153, 12154, 12155 };

    /**
     * Represents the state of the quest.
     */
    protected QuestState state = QuestState.NOT_STARTED;

    /**
     * The player instance.
     */
    protected Player player;

    /**
     * The stage of the quest.
     */
    protected int stage = 0;

    /**
     * The index represents the button id index.
     */
    protected int index = 0;

    /**
     * The lines of this {@link org.gielinor.game.content.global.quest.Quest}.
     */
    protected List<String> LINES = new ArrayList<>();

    /**
     * Constructs a new {@code Quest} {@code Object}.
     *
     * @param player the player.
     */
    public Quest(Player player) {
        this.player = player;
    }

    /**
     * Method will update your quest stage on the interface.
     */
    public abstract void update();

    /**
     * Prepends finishing data before finish() is called.
     */
    public void completeQuest(String questName, int stage, int itemId, int zoom, CloseEvent closeEvent, String... messages) {
        player.getQuestRepository().incrementPoints(getQuestPoints());
        Component rewardComponent = new Component(REWARD_COMPONENT);
        if (closeEvent != null) {
            rewardComponent.setCloseEvent(closeEvent);
        }
        player.getInterfaceState().open(rewardComponent);
        for (int rewardId : REWARD_IDS) {
            player.getActionSender().sendString("", rewardId);
        }
        for (int i = 0; i < messages.length; i++) {
            drawReward(messages[i], i);
        }
        player.getActionSender().sendItemZoomOnInterface(itemId, zoom, REWARD_COMPONENT_ITEM);
        player.getInterfaceState().closeChatbox();
        player.getActionSender().sendString("You have completed the " + questName + " quest!", REWARD_COMPONENT_NAME);
        player.getActionSender().sendString(String.valueOf(player.getQuestRepository().getPoints()), REWARD_COMPONENT_POINTS);
        setStage(stage);
        setState(QuestState.COMPLETED);
        player.getQuestRepository().update(player);
        player.getActionSender().sendMessage("Congratulations! Quest complete!");
    }

    /**
     * Prepends finishing data before finish() is called.
     */
    public void completeQuest(String questName, int stage, int itemId, String... messages) {
        completeQuest(questName, stage, itemId, 240, null, messages);
    }

    /**
     * Prepends finishing data before finish() is called.
     */
    public void completeQuest(String questName, int stage, int itemId, CloseEvent closeEvent, String... messages) {
        completeQuest(questName, stage, itemId, 240, closeEvent, messages);
    }

    /**
     * Method will finish the quest.
     */
    public abstract void finish();

    /**
     * Gets the index id of the button id of the quest.
     *
     * @return The interface button id.
     */
    public abstract int getIndex();

    /**
     * Gets the quest name.
     *
     * @return the name of the quest.
     */
    public abstract String getName();

    /**
     * Gets the quest points reward.
     *
     * @return the quest points reward.
     */
    public abstract int getQuestPoints();

    /**
     * Gets the id of this quest.
     *
     * @return The id.
     */
    public abstract int getId();

    /**
     * Clears the screen.
     */
    public void clear() {
        LINES.clear();
        player.getQuestMenuManager().clear();
        player.getQuestMenuManager().setTitle("<col=8A0808>" + getName() + "</col>");
    }

    /**
     * Method used to write a line.
     *
     * @param message the message.
     * @param line    the line.
     */
    protected final void line(String message, int line) {
        line(message.replace("<blue>", BLUE).replace("<red>", RED), line, false);
    }

    /**
     * Draws a line on the quest journal component.
     *
     * @param message The message.
     * @param line    The line number.
     * @param crossed True if the message should be crossed out.
     */
    protected final void line(String message, final int line, final boolean crossed) {
        int lineId = 0;
        if (message.contains("<br>")) {
            for (String string : message.split("<br>")) {
                player.getActionSender().sendString498(crossed ? "<str>" + string + "</str>" : string, JOURNAL_COMPONENT, line + lineId);
                lineId++;
            }
            return;
        }
        player.getActionSender().sendString498(crossed ? "<str>" + message + "</str>" : message, JOURNAL_COMPONENT, line);
    }

    /**
     * Draws text on the quest reward component.
     *
     * @param string The string to draw.
     * @param line   The line number to draw on.
     */
    protected final void drawReward(final String string, final int line) {
        player.getActionSender().sendString(string, REWARD_IDS[line]);
    }

    /**
     * Method used to start the quest.
     */
    public void start() {
        setStage(10);
        setState(QuestState.STARTED);
        player.getQuestRepository().update(player);
    }

    /**
     * Checks if the player has requirements for the quest.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasRequirements() {
        return true;
    }

    /**
     * Checks to see if the player still has a specific item.
     *
     * @param item The item to check.
     * @return True if the player still has the item.
     */
    public final boolean hasItem(final Item item) {
        return (player.getInventory().containsItem(item) || player.getBank().containsItem(item));
    }

    /**
     * Gets the state.
     *
     * @return The state.
     */
    public QuestState getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state The state to set.
     */
    public void setState(QuestState state) {
        this.state = state;
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
     * Sets the player.
     *
     * @param player The player to set.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the stage.
     *
     * @return The stage.
     */
    public int getStage() {
        return stage;
    }

    /**
     * Sets the stage.
     *
     * @param stage The stage to set.
     */
    public void setStage(int stage) {
        this.stage = stage;
    }

    /**
     * Sets the index.
     *
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Gets the value colour.
     *
     * @return the color.
     */
    public String getBlue() {
        return BLUE;
    }

    /**
     * Gets the red colour.
     *
     * @return the color.
     */
    public String getRed() {
        return RED;
    }

    /**
     * Gets the lines of this <code>Quest</code>.
     *
     * @return The lines.
     */
    public List<String> getLines() {
        return LINES;
    }

}
