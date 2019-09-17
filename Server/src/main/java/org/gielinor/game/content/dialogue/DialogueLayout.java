package org.gielinor.game.content.dialogue;

/**
 * Created by Stan van der Bend on 29/01/2018.
 * project: Gielinor-Server
 * package: org.gielinor.game.content.dialogue
 */
public enum DialogueLayout {

    /*
     * Gives variable options for a player to choose.
     */
    OPTION,

    /*
     * Gives a statement.
     */
    STATEMENT,

    /*
     * Gives a dialogue said by an npc.
     */
    NPC_STATEMENT,

    /*
     * Gives a dialogue with an item model add to it.
     */
    ITEM_STATEMENT,

    /*
     * Gives a dialogue said by a player.
     */
    PLAYER_STATEMENT;

}
