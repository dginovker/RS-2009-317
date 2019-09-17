package org.gielinor.game.node.entity.player.info.title;

import java.util.HashMap;
import java.util.Map;

import org.gielinor.game.component.Component;
import org.gielinor.game.content.dialogue.DialogueInterpreter;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.info.LoyaltyTitle;
import org.gielinor.utilities.string.TextUtils;

/**
 * Handles {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle} management.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class LoyaltyTitleManagement {

    /**
     * Represents how many {@link org.gielinor.game.node.entity.player.info.LoyaltyTitle}s to send per page.
     */
    private static final int TITLES_PER_PAGE = 11;

    /**
     * Handles the button clicking.
     *
     * @param player    The player.
     * @param component The {@link org.gielinor.game.component.Component}.
     * @param buttonId  The id of the button.
     * @return <code>True</code> if the button was handled.
     */
    public static boolean handle(Player player, Component component, int buttonId) {
        switch (component.getId()) {
            /**
             * Category selection.
             */
            case 23035:
                TitleCategory titleCategory = null;
                switch (buttonId) {
                    /**
                     * Regular.
                     */
                    case 23041:
                        titleCategory = TitleCategory.REGULAR;
                        break;
                    /**
                     * Supreme.
                     */
                    case 23045:
                        titleCategory = TitleCategory.COINS;
                        break;
                    /**
                     * Voting.
                     */
                    case 23049:
                        titleCategory = TitleCategory.VOTING;
                        break;
                    /**
                     * Misc.
                     */
                    case 23053:
                        titleCategory = TitleCategory.MISCELLANEOUS;
                        break;
                }
                if (titleCategory == null) {
                    return false;
                }
                player.setAttribute("TITLE_CATEGORY", titleCategory);
                openPage(player, 0, true);
                return true;
            /**
             * Title management.
             */
            case 23057:
                int page = player.getAttribute("TITLES_PAGE", 0);
                switch (buttonId) {
                    case 23063:
                        if (page == 0) {
                            return true;
                        }
                        openPage(player, page - 1, false);
                        return true;
                    case 23064:
                        if ((page + 1) > getMaximumPages(player)) {
                            return true;
                        }
                        openPage(player, page + 1, false);
                        return true;
                }
                if (buttonId >= 23077 && buttonId <= 23087) {
                    return handlePurchase(player, buttonId);
                }
                if (buttonId >= 23088 && buttonId <= 23098) {
                    return handleToggle(player, buttonId);
                }
                break;
        }
        return false;
    }

    public static boolean handlePurchase(Player player, int buttonId) {
        Map<Integer, LoyaltyTitle> titleView = player.getAttribute("TITLE_VIEW");
        if (titleView == null) {
            return false;
        }
        LoyaltyTitle loyaltyTitle = titleView.get(buttonId);
        if (loyaltyTitle == null) {
            return false;
        }
        if (!loyaltyTitle.canPurchase(player)) {
            return true;
        }
        return player.getDialogueInterpreter().open(DialogueInterpreter.getDialogueKey("loyalty-title-purchase"), loyaltyTitle);
    }

    public static boolean handleToggle(Player player, int buttonId) {
        Map<Integer, LoyaltyTitle> titleView = player.getAttribute("TITLE_VIEW");
        if (titleView == null) {
            return false;
        }
        LoyaltyTitle loyaltyTitle = titleView.get(buttonId - 11);
        if (loyaltyTitle == null) {
            return false;
        }
        if (!loyaltyTitle.isUnlocked(player)) {
            player.getActionSender().sendMessage("You have not unlocked that loyalty title.");
            return true;
        }
        if (!loyaltyTitle.hasRequirements(player, true)) {
            return true;
        }
        if (player.getTitleManager().hasTitle() && player.getTitleManager().getLoyaltyTitle().getId() == loyaltyTitle.getId()) {
            player.getActionSender().sendMessage("You have cleared your loyalty title.");
            player.getTitleManager().setLoyaltyTitle(null);
            player.getAppearance().sync();
            openPage(player, player.getAttribute("TITLES_PAGE", 0), false);
            return true;
        }
        player.getActionSender().sendMessage("You have changed your loyalty title.");
        loyaltyTitle.set(player);
        player.getAppearance().sync();
        openPage(player, player.getAttribute("TITLES_PAGE", 0), false);
        return true;
    }

    public static int getMaximumPages(Player player) {
        TitleCategory titleCategory = player.getAttribute("TITLE_CATEGORY", TitleCategory.REGULAR);
        int totalTitles = 0;

        for (LoyaltyTitle loyaltyTitle : LoyaltyTitle.values()) {
            if (loyaltyTitle.getTitleCategory() == null) {
                continue;
            }
            if (loyaltyTitle.getTitleCategory() == titleCategory) {
                totalTitles++;
            }
        }

        int titleCount = totalTitles;
        int pageCount = 0;

        while (titleCount > TITLES_PER_PAGE && titleCount > -1) {
            titleCount -= TITLES_PER_PAGE;
            pageCount++;
        }

        return pageCount;
    }

    public static void openPage(Player player, int page, boolean reOpen) {
        if (player.getAttribute("TITLES_PAGE", 0) != page) {
            player.setAttribute("TITLES_PAGE", page);
        }
        TitleCategory titleCategory = player.getAttribute("TITLE_CATEGORY", TitleCategory.REGULAR);
        int titlesShown = 0;
        int lineIndex = 23066;
        int currentIndex = 0;
        Map<Integer, LoyaltyTitle> titleView = new HashMap<>();
        player.getInterfaceState().send(page == 0 ? 50 : 51, 23063);
        player.getInterfaceState().send((page + 1) > getMaximumPages(player) ? 50 : 51, 23064);
        for (int childId = 0; childId < TITLES_PER_PAGE; childId++) {
            player.getInterfaceState().send(50, lineIndex + childId);
            player.getInterfaceState().send(50, ((lineIndex + childId) + 11));
            player.getInterfaceState().send(50, ((lineIndex + childId) + 22));
        }
        for (int index = 0; index < LoyaltyTitle.values().length; index++) {
            if (titlesShown == TITLES_PER_PAGE) {
                break;
            }
            if (index > LoyaltyTitle.values().length) {
                break;
            }
            LoyaltyTitle loyaltyTitle = LoyaltyTitle.values()[index];
            if (loyaltyTitle.getTitleCategory() == null ||
                loyaltyTitle.getTitleCategory() != titleCategory) {
                continue;
            }
            if (!loyaltyTitle.getShowInShop(player)) {
                continue;
            }
            if (currentIndex >= (page * TITLES_PER_PAGE)) {
                String colour = loyaltyTitle.getColour();
                String username = "<col=FFFFFF>" + TextUtils.formatDisplayName(player.getUsername()) + "</col>";
                String titleName = loyaltyTitle.get(player);
                String title = loyaltyTitle.isSuffix() ? (username + " " + ("<col=" + colour + ">" + titleName + "</col>")) : (("<col=" + colour + ">" + titleName + "</col>") + " " + username);
                player.getActionSender().sendString(lineIndex, title);
                player.getActionSender().sendString(lineIndex + 11, loyaltyTitle.getCost() > 0 ? (loyaltyTitle.getCostString()) : "Free");
                String toggleStatus = "Enable";
                if (player.getTitleManager().hasTitle() && player.getTitleManager().getLoyaltyTitle().getId() == loyaltyTitle.getId()) {
                    toggleStatus = "Disable";
                }
                player.getActionSender().sendString(lineIndex + 22, toggleStatus);
                player.getInterfaceState().send(51, lineIndex);
                player.getInterfaceState().send(player.getTitleManager().getUnlockedTitles().contains(loyaltyTitle) ? 50 : 51, lineIndex + 11);
                player.getInterfaceState().send(player.getTitleManager().getUnlockedTitles().contains(loyaltyTitle) ? 51 : 50, lineIndex + 22);
                titleView.put(lineIndex + 11, loyaltyTitle);
                lineIndex++;
                titlesShown++;
            }
            currentIndex++;
        }
        player.setAttribute("TITLE_VIEW", titleView);
        player.getActionSender().sendString("Player Titles" + (page > 0 ? " ~ Page " + page : "") + " ~ " +
            TextUtils.formatDisplayName(titleCategory.name()), 23059);
        if (reOpen) {
            Component titlesComponent = new Component(23057);
            player.getInterfaceState().open(titlesComponent);
        }
    }

    public static void openPersonalTitles(Player player) {
        if (player.getTitleManager().getUnlockedTitles().stream().filter(title -> title.getTitleCategory() == TitleCategory.SPECIAL).count() <= 0) {
            player.getActionSender().sendMessage("You do not have any personal titles!");
            return;
        }
        player.setAttribute("TITLE_CATEGORY", TitleCategory.SPECIAL);
        openPage(player, 0, true);
    }

}
