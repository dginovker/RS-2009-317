package org.gielinor.game.content.dialogue;

import org.gielinor.game.node.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Stan van der Bend on 28/10/2017.
 *
 * project: live
 * package: runeworld.model.dialogue
 */
public class DialogueOptionHandler {

    private Player player;

    private final List<Consumer<Player>> actions = new ArrayList<>(5);

    public void setPlayer(Player player){
        this.player = player;
    }

    public boolean handleButton(int buttonID){

        final int actionIndex = getActionIndex(actions.size(), buttonID);

        if(actionIndex > -1)
        {
            actions.get(actionIndex).accept(player);
            return true;
        }
        return false;
    }

    private int getActionIndex(int amountOfOptions, int buttonId){
        switch (amountOfOptions) {
            case 2:return buttonId-OptionSelect.TWO_OPTION_ONE.getId();
            case 3:return buttonId-OptionSelect.THREE_OPTION_ONE.getId();
            case 4:return buttonId-OptionSelect.FOUR_OPTION_ONE.getId();
            case 5:return buttonId-OptionSelect.FIVE_OPTION_ONE.getId();
        }
        return -1;
    }

    public DialogueOptionHandler addAction(int buttonIndex, Consumer<Player> consumer){
        actions.add(buttonIndex, consumer);
        return this;
    }

}
