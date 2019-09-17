package org.gielinor.game.content.global.quest;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.gielinor.game.content.global.quest.impl.CurseOfTheUndead;
import org.gielinor.game.content.global.quest.impl.GertrudesCat;
import org.gielinor.game.content.global.quest.impl.TheLostKingdom;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.InterfaceColourContext;
import org.gielinor.net.packet.out.InterfaceColour;
import org.gielinor.parser.player.SavingModule;
import org.gielinor.utilities.buffer.ByteBufferUtils;

/**
 * Represents a repository of instanced player quests.
 *
 * @author 'Vexia
 */
public final class QuestRepository implements SavingModule {

    /**
     * Represents the mapping of instanced quests.
     */
    private final Map<String, Quest> QUEST = new HashMap<>();

    /**
     * Represents the player instance.
     */
    private final Player player;

    /**
     * Represents the synchronized quest points.
     */
    private int points = 0;

    /**
     * Constructs a new {@code QuestRepository} {@code Object}.
     *
     * @param player the player.
     */
    public QuestRepository(final Player player) {
        this.player = player;
        QUEST.put(CurseOfTheUndead.NAME, new CurseOfTheUndead(player));
        QUEST.put(GertrudesCat.NAME, new GertrudesCat(player));
        QUEST.put(TheLostKingdom.NAME, new TheLostKingdom(player));
    }

    /**
     * Updates the {@link #QUEST} values and the information and states.
     *
     * @param player the player.
     */
    public void update(Player player) {
        player.getActionSender().sendString(29155, "Quest Points: " + points);
        for (Entry<String, Quest> quest : QUEST.entrySet()) {
            int colour = quest.getValue().getState() == QuestState.COMPLETED ? 0x3366 :
                quest.getValue().getState() == QuestState.STARTED ? 0x33FF66 : 0x6000;
            PacketRepository.send(InterfaceColour.class, new InterfaceColourContext(player, quest.getValue().getIndex(), colour));
        }
    }

    /**
     * Checks to see if a quest has been started.
     *
     * @param quest The quest to check.
     * @return True if the quest has been started.
     */
    public boolean isStarted(Quest quest) {
        return isStarted(quest.getName());
    }

    /**
     * Checks to see if a quest has been started.
     *
     * @param name The name of the quest to check.
     * @return True if the quest has been started.
     */
    public boolean isStarted(String name) {
        return (QUEST.get(name) == null ? Boolean.FALSE : QUEST.get(name).getState() == QuestState.STARTED);
    }

    /**
     * Checks if the quest by the given name is completed.
     *
     * @param name the name of the quest.
     * @return if the quest is completed.
     */
    public boolean isComplete(String name) {
        return isComplete(QUEST.get(name));
    }

    /**
     * Checks if the quest is complete.
     *
     * @param quest the quest.
     * @return the quest.
     */
    public boolean isComplete(Quest quest) {
        if (quest == null) {
            return true;
        }
        return quest.getState() == QuestState.COMPLETED;
    }

    /**
     * Increments the obtained points by the value.
     *
     * @param value the value.
     */
    public void incrementPoints(int value) {
        points += value;
    }

    /**
     * Checks if all quests are completed.
     *
     * @return {@code True} if so.
     */
    public boolean hasCompletedAll() {
        return getPoints() == getAvailablePoints();
    }

    /**
     * Gets the total available quest points.
     *
     * @return the points needed for a cape.
     */
    public int getAvailablePoints() {
        int points = 0;
        for (Quest quest : QUEST.values()) {
            points += quest.getQuestPoints();
        }
        return points;
    }

    /**
     * Returns the <p>Quest</p> by the given name.
     *
     * @param name the name of the quest.
     * @return the quest.
     */
    public Quest getQuest(String name) {
        return QUEST.get(name);
    }

    /**
     * Returns the <p>Quest</p> by the given id.
     *
     * @param id The id of the quest.
     * @return The quest.
     */
    public Quest forId(int id) {
        for (Quest quest : QUEST.values()) {
            if (quest.getId() == id) {
                return quest;
            }
        }
        return null;
    }

    /**
     * Gets the <b>Quest</b> by the index.
     *
     * @param index the index of the quest.
     * @return the quest by the button index.
     */
    public Quest getQuestIndex(int index) {
        for (Entry<String, Quest> quest : QUEST.entrySet()) {
            if (quest.getValue().getIndex() == index) {
                return quest.getValue();
            }
        }
        return null;
    }

    /**
     * Method used to sync the quest points.
     */
    public void syncPoints() {
        int points = 0;
        for (Quest quest : QUEST.values()) {
            if (quest.getState() == QuestState.COMPLETED) {
                points += quest.getQuestPoints();
            }
        }
        setPoints(points);
    }

    /**
     * Gets the points.
     *
     * @return the points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the points.
     *
     * @param points the points to set.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * Gets the quests.
     *
     * @return the quest.
     */
    public Map<String, Quest> getQuests() {
        return QUEST;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }


    @Override
    public void save(ByteBuffer byteBuffer) {
        byteBuffer.put((byte) 1);
        byteBuffer.putInt(points);
        for (Entry<String, Quest> quest : getQuests().entrySet()) {
            byteBuffer.put((byte) 2);
            ByteBufferUtils.putString(quest.getKey(), byteBuffer);
            byteBuffer.putInt(quest.getValue().getStage());
            int state = quest.getValue().getState() == QuestState.COMPLETED ? 2 : quest.getValue().getState() == QuestState.STARTED ? 1 : 0;
            byteBuffer.putInt(state);
        }
        byteBuffer.put((byte) 0);
    }


    @Override
    public void parse(ByteBuffer byteBuffer) {
        int opcode;
        while ((opcode = byteBuffer.get() & 0xFF) != 0) {
            switch (opcode) {
                case 1:
                    points = byteBuffer.getInt();
                    break;
                case 2:
                    final String name = ByteBufferUtils.getString(byteBuffer);
                    int stage = byteBuffer.getInt();
                    int state = byteBuffer.getInt();
                    final QuestState questState = state == 2 ? QuestState.COMPLETED : state == 1 ? QuestState.STARTED : QuestState.NOT_STARTED;
                    final Quest quest = getQuest(name);
                    if (quest == null) {
                        System.err.println("Could not find quest: " + name);
                        continue;
                    }
                    getQuests().remove(quest);
                    quest.setStage(stage);
                    quest.setState(questState);
                    getQuests().put(name, quest);
                    break;
            }
        }
        syncPoints();
    }

}
