package org.gielinor.game.content.dialogue.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dialogue.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public enum Dialogue {
    // TODO 317 Correct text ids
    MODEL_LEFT_1(306, DialogueType.MODEL_LEFT),
    MODEL_LEFT_2(310, DialogueType.MODEL_LEFT),
    MODEL_LEFT_3(315, DialogueType.MODEL_LEFT),
    MODEL_LEFT_4(321, DialogueType.MODEL_LEFT),
    ITEM_SELECT_1(4429, new int[]{ 2799 }, new int[]{ 1746 }, DialogueType.ITEM_SELECT, 160),
    ITEM_SELECT_2(8866, new int[]{ 8874, 8878 }, new int[]{ 8869, 8870 }, DialogueType.ITEM_SELECT, 180, 180),
    ITEM_SELECT_3(8880, new int[]{ 8889, 8893, 8897 }, new int[]{ 8883, 8884, 8885 }, DialogueType.ITEM_SELECT, 180, 180, 180),
    ITEM_SELECT_4(8899, new int[]{ 8909, 8913, 8917, 8921 }, new int[]{ 8902, 8903, 8904, 8905 }, DialogueType.ITEM_SELECT, 250, 150, 200, 250),
    ITEM_SELECT_5(8938, new int[]{ 8949, 8953, 8957, 8961, 8965 }, new int[]{ 8941, 8942, 8943, 8944, 8945 },
        DialogueType.ITEM_SELECT, 120, 150, 150, 120, 150);

    /**
     * The interface id for this {@code Dialogue}.
     */
    private final int interfaceId;

    /**
     * The ids where text cases are held.
     */
    private int[] textIds;

    /**
     * The ids where models are held.
     */
    private int[] modelIds;

    /**
     * The {@link DialogueType} for this {@code Dialogue}.
     */
    private final DialogueType dialogueType;

    /**
     * The zooms for this {@code Dialogue}, if any.
     */
    private int[] zooms;

    /**
     * Constructs a new {@link Dialogue}.
     *
     * @param interfaceId  The interface id for this {@code Dialogue}.
     * @param dialogueType The {@link DialogueType} for this {@code Dialogue}.
     */
    Dialogue(int interfaceId, DialogueType dialogueType) {
        this.interfaceId = interfaceId;
        this.dialogueType = dialogueType;
    }

    /**
     * Constructs a new {@link Dialogue} with zooms.
     *
     * @param interfaceId  The interface id for this {@code Dialogue}.
     * @param dialogueType The {@link DialogueType} for this {@code Dialogue}.
     * @param zooms        The zooms for this {@code Dialogue}.
     */
    Dialogue(int interfaceId, DialogueType dialogueType, int... zooms) {
        this.interfaceId = interfaceId;
        this.dialogueType = dialogueType;
        this.zooms = zooms;
    }

    /**
     * Constructs a new {@link Dialogue} with zooms and additional ids.
     *
     * @param interfaceId  The interface id for this {@code Dialogue}.
     * @param textIds      The ids where text cases are held.
     * @param modelIds     The ids where models are held.
     * @param dialogueType The {@link DialogueType} for this {@code Dialogue}.
     * @param zooms        The zooms for this {@code Dialogue}.
     */
    Dialogue(int interfaceId, int[] textIds, int[] modelIds, DialogueType dialogueType, int... zooms) {
        this.interfaceId = interfaceId;
        this.textIds = textIds;
        this.modelIds = modelIds;
        this.dialogueType = dialogueType;
        this.zooms = zooms;
    }

    /**
     * Gets the interface id for this {@code Dialogue}.
     *
     * @return The interface id.
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets the ids where text cases are held.
     *
     * @return The text ids.
     */
    public int[] getTextIds() {
        return textIds;
    }

    /**
     * Gets the ids where models are held.
     *
     * @return The model ids.
     */
    public int[] getModelIds() {
        return modelIds;
    }

    /**
     * Gets the zooms for this {@code Dialogue}, if any.
     *
     * @return The zooms.
     */
    public int[] getZooms() {
        return zooms;
    }

    /**
     * Gets the {@link DialogueType} for this {@code Dialogue}.
     *
     * @return The {@code DialogueType}.
     */
    public DialogueType getDialogueType() {
        return dialogueType;
    }

    /**
     * Gets a {@link Dialogue} {@link java.util.List} for the given {@code DialogueType}.
     *
     * @param dialogueType The {@code DialogueType}.
     * @return The {@link org.gielinor.game.content.dialogue.impl.Dialogue} {@link java.util.List}.
     */
    public static List<Dialogue> getDialogue(DialogueType dialogueType) {
        List<Dialogue> dialogueList = new ArrayList<>();
        for (Dialogue dialogue : Dialogue.values()) {
            if (dialogue.getDialogueType() == dialogueType) {
                dialogueList.add(dialogue);
            }
        }
        return dialogueList;
    }

}
