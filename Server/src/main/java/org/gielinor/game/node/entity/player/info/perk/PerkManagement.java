package org.gielinor.game.node.entity.player.info.perk;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.QuestMenuManager;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.Perk;
import org.gielinor.game.world.World;
import org.gielinor.rs2.pulse.Pulse;

/**
 * Handles {@link org.gielinor.game.node.entity.player.info.Perk} management.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class PerkManagement {

    /**
     * Represents how many {@link org.gielinor.game.node.entity.player.info.Perk}s to send per page.
     */
    private static final int PERKS_PER_PAGE = 11;

    /**
     * Handles the button clicking.
     *
     * @param player    The player.
     * @param component The {@link org.gielinor.game.component.Component}.
     * @param buttonId  The id of the button.
     * @return <code>True</code> if the button was handled.
     */
    public static boolean handle(Player player, Component component, int buttonId) {
        int page = player.getAttribute("PERKS_PAGE", 0);
        switch (buttonId) {
            case 23063:
                if (page == 0) {
                    return true;
                }
                openPage(player, page - 1, false);
                return true;
            case 23064:
                if ((page + 1) > getMaximumPages()) {
                    return true;
                }
                openPage(player, page + 1, false);
                return true;
        }
        if (buttonId >= 23066 && buttonId <= 23076) {
            return viewDescription(player, buttonId);
        }
        if (buttonId >= 23077 && buttonId <= 23087) {
            return handlePurchase(player, buttonId);
        }
        return buttonId >= 23088 && buttonId <= 23098 && handleToggle(player, buttonId);
    }

    public static boolean viewDescription(Player player, int buttonId) {
        Map<Integer, Perk> perkView = player.getAttribute("PERK_VIEW");
        if (perkView == null) {
            return false;
        }
        Perk perk = perkView.get(buttonId + 11);
        if (perk == null) {
            return false;
        }
        QuestMenuManager questMenuManager = player.getQuestMenuManager();
        questMenuManager.setTitle(perk.getFormattedName() + " Perk");
        questMenuManager.setLines(perk.getDescription());
        questMenuManager.setCloseEvent(new CloseEvent() {

            @Override
            public void close(Player player, Component component) {
                World.submit(new Pulse(1) {

                    @Override
                    public boolean pulse() {
                        openPage(player, player.getAttribute("PERKS_PAGE", 0), true);
                        return true;
                    }
                });
                questMenuManager.reset();
            }

            @Override
            public boolean canClose(Player player, Component component) {
                return true;
            }
        });
        questMenuManager.send();
        return true;
    }

    public static boolean handlePurchase(Player player, int buttonId) {
        Map<Integer, Perk> perkView = player.getAttribute("PERK_VIEW");
        if (perkView == null) {
            return false;
        }
        Perk perk = perkView.get(buttonId);
        if (perk == null) {
            return false;
        }
        if (!perk.canPurchase(player)) {
            return true;
        }
        return player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("perk-purchase"), perk);
    }

    public static boolean handleToggle(Player player, int buttonId) {
        Map<Integer, Perk> perkView = player.getAttribute("PERK_VIEW");
        if (perkView == null) {
            return false;
        }
        Perk perk = perkView.get(buttonId - 11);
        if (perk == null) {
            return false;
        }
        if (!perk.isUnlocked(player)) {
            player.getActionSender().sendMessage("You have not unlocked that perk yet.");
            return true;
        }
        if (perk.enabled(player)) {
            player.getActionSender().sendMessage("You have disabled the perk: " + perk.getFormattedName() + ".");
            player.getPerkManager().disable(perk);
            openPage(player, player.getAttribute("PERKS_PAGE", 0), false);
            return true;
        }
        player.getActionSender().sendMessage("You have enabled the perk: " + perk.getFormattedName() + ".");
        player.getPerkManager().enable(perk);
        player.getAppearance().sync();
        openPage(player, player.getAttribute("PERKS_PAGE", 0), false);
        return true;
    }

    public static int getMaximumPages() {
        int valueCount = Perk.values().length;
        if (valueCount == 0) return 0;
        return valueCount / (PERKS_PER_PAGE + 1);
    }

    public static void openPage(Player player, int page, boolean reOpen) {
        if (player.getAttribute("PERKS_PAGE", 0) != page) {
            player.setAttribute("PERKS_PAGE", page);
        }
        int perksShown = 0;
        int lineIndex = 23066;
        int currentIndex = 0;
        Map<Integer, Perk> perkView = new HashMap<>();
        player.getInterfaceState().send(page == 0 ? 50 : 51, 23063);
        player.getInterfaceState().send((page + 1) > getMaximumPages() ? 50 : 51, 23064);
        for (int childId = 0; childId < PERKS_PER_PAGE; childId++) {
            player.getInterfaceState().send(50, lineIndex + childId);
            player.getInterfaceState().send(50, ((lineIndex + childId) + 11));
            player.getInterfaceState().send(50, ((lineIndex + childId) + 22));
        }
        for (int index = 0; index < Perk.values().length; index++) {
            if (perksShown == PERKS_PER_PAGE) {
                break;
            }
            if (index > Perk.values().length) {
                break;
            }
            Perk perk = Perk.values()[index];
            if (currentIndex >= (page * PERKS_PER_PAGE)) {
                player.getActionSender().sendString(lineIndex, perk.getFormattedName());
                player.getActionSender().sendString(lineIndex + 11, perk.getCost() > 0 ? (perk.getCost() + " Gielinor tokens") : "Free");
                String toggleStatus = perk.enabled(player) ? "Disable" : "Enable";
                player.getActionSender().sendString(lineIndex + 22, toggleStatus);
                player.getInterfaceState().send(51, lineIndex);
                player.getInterfaceState().send(perk.isUnlocked(player) ? 50 : 51, lineIndex + 11);
                player.getInterfaceState().send(perk.isUnlocked(player) ? 51 : 50, lineIndex + 22);
                perkView.put(lineIndex + 11, perk);
                lineIndex++;
                perksShown++;
            }
            currentIndex++;
        }
        player.setAttribute("PERK_VIEW", perkView);
        player.getActionSender().sendString("Perks" + (page > 0 ? " ~ Page " + page : ""), 23059);
        if (reOpen) {
            player.setAttribute("PERKS_PAGE", page);
            Component perkComponent = new Component(23057);
            player.getInterfaceState().open(perkComponent);
        }
    }

}
