package org.gielinor.game.system.command.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gielinor.game.content.dialogue.FacialExpression;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Rights;
import org.gielinor.game.system.command.Command;
import org.gielinor.game.world.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sends a dialogue entered.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class DialogueCommand extends Command {

    private static final Logger log = LoggerFactory.getLogger(DialogueCommand.class);

    @Override
    public Rights getRights() {
        return Rights.REGULAR_PLAYER;
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

    @Override
    public String[] getCommands() {
        return new String[]{ "dia", "dialogue" };
    }

    @Override
    public void execute(Player player, String[] args) {
        boolean npc = true;
        if (args.length == 2) {
            npc = false;
        }
        FacialExpression facialExpression = FacialExpression.NORMAL;
        if (args.length >= 2) {
            npc = false;
            facialExpression = FacialExpression.valueOf(args[2]);
        }
        String[] stringArr;
        try (BufferedReader in = new BufferedReader(new FileReader("dialogue-text.txt"))) {
            String text;
            List<String> list = new ArrayList<>();
            while ((text = in.readLine()) != null) {
                list.add(text.replaceAll("\",", "").replaceAll("\"", ""));
            }
            stringArr = list.toArray(new String[list.size()]);
        } catch (IOException ex) {
            log.error("Failed to execute dialogue command from [{}].", player.getUsername(), ex);
            return;
        }
        player.getDialogueInterpreter().sendDialogues(npc ? Repository.findNPC(0) : player,
            facialExpression, stringArr);
    }

}
