package org.gielinor.game.node.entity.player.ai;

import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.node.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles artificial intelligent player controls.
 * @author Emperor
 *
 */
public final class AIPControlDialogue extends DialoguePlugin {

    private static final Logger log = LoggerFactory.getLogger(AIPControlDialogue.class);

    /**
     * The AIP to control.
     */
    private AIPlayer aip;

    /**
     * Constructs a new {@code AIPControlDialogue} {@code Object}.
     */
    public AIPControlDialogue() {
    }

    /**
     * Constructs a new {@code AIPControlDialogue} {@code Object}.
     * @param player The player.
     */
    public AIPControlDialogue(Player player) {
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new AIPControlDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        if (!player.getDetails().getRights().isAdministrator()) {
            return false;
        }
        aip = (AIPlayer) args[0];
        String select = "Select";
        if (player.getAttribute("aip_select") == aip) {
            select = "Deselect";
        }
        interpreter.sendOptions("AIP#" + aip.getUid() + " controls", select, "Settings", "Follow", "Stand-by", "Clear");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, OptionSelect optionSelect) {
        switch (interfaceId) {
            case 2492:
                switch (optionSelect) {
                    case FIVE_OPTION_ONE:
                        if (player.getAttribute("aip_select") == aip) {
                            player.removeAttribute("aip_select");
                            break;
                        }
                        player.setAttribute("aip_select", aip);
                        break;
                    case FIVE_OPTION_TWO:
                        interpreter.sendOptions("AIP#" + aip.getUid() + " settings", "Toggle run", "Toggle retaliate", "Toggle special attack");
                        return true;
                    case FIVE_OPTION_THREE:
                        aip.follow(player);
                        player.removeAttribute("aip_select");
                        break;
                    case FIVE_OPTION_FOUR:
                        aip.getPulseManager().clear();
                        player.removeAttribute("aip_select");
                        break;
                    case FIVE_OPTION_FIVE:
                        AIPlayer.deregister(aip.getUid());
                        player.removeAttribute("aip_select");
                        break;
                }
                close();
                return true;
            case 2469:
                switch (optionSelect) {
                    case THREE_OPTION_ONE:
                        aip.getSettings().toggleRun();
                        break;
                    case THREE_OPTION_TWO:
                        aip.getSettings().toggleRetaliating();
                        break;
                    case THREE_OPTION_THREE:
                        aip.getSettings().toggleSpecialBar();
                        break;
                }
                close();
                return true;
        }
        log.info("[{}] interfaceId={}; option: {}.", interfaceId, optionSelect);
        return false;
    }

    @Override
    public int[] getIds() {
        return new int[0];
    }

}
