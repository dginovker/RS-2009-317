package org.gielinor.game.system.command.impl;

import org.gielinor.game.content.global.quest.Quest;
import org.gielinor.game.content.global.quest.QuestState;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.system.command.CommandDescription;

/**
 * Finishes all of the player's quests.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class FinishQuestsCommand extends Command {

    @Override
    public Rights getRights() {
        return Rights.GIELINOR_MODERATOR;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "quests", "resetquests" };
    }

    @Override
    public void init() {
        CommandDescription.add(new CommandDescription("quests", "Completes all quest diaries", getRights(), null));
        CommandDescription.add(new CommandDescription("resetquests", "Resets all quest diaries", getRights(), null));
    }

    @Override
    public void execute(Player player, String[] args) {
        for (Quest quest : player.getQuestRepository().getQuests().values()) {
            if (args != null) {
                if (args[0].equalsIgnoreCase("resetquests")) {
                    quest.setStage(0);
                    quest.setState(QuestState.NOT_STARTED);
                    continue;
                }
                quest.setStage(100);
                quest.setState(QuestState.COMPLETED);
            } else {
                quest.setStage(100);
                quest.setState(QuestState.COMPLETED);
            }
        }
        player.getQuestRepository().update(player);
    }

}
