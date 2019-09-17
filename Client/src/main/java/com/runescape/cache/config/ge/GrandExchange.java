package com.runescape.cache.config.ge;

import com.runescape.Game;
import com.runescape.cache.def.item.ItemDefinition;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.Sprite;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.media.Raster;
import com.runescape.media.Scrollbar;
import com.runescape.media.font.GameFont;
import com.runescape.util.MapUtil;
import com.runescape.util.StringUtility;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Handles Grand Exchange operations.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class GrandExchange {
    /**
     * The main interface id.
     */
    public static final int GRAND_EXCHANGE_MAIN_INTERFACE_ID = 24907;
    /**
     * The offer interface id.
     */
    public static final int GRAND_EXCHANGE_OFFER_INTERFACE_ID = 25042;
    /**
     * The collection box interface id.
     */
    public static final int GRAND_EXCHANGE_COLLECTION_BOX_INTERFACE_ID = 25531;
    /**
     * The maximum item search results.
     */
    public static final int MAXIMUM_SEARCH_RESULTS = 200;
    /**
     * The chat box search height.
     */
    public static final int SEARCH_HEIGHT = 108;
    /**
     * The status quantity button ids.
     */
    private static final int[] STATUS_BUTTON_IDS = {25068, 25072, 25076, 25080};
    /**
     * The status quantity text ids.
     */
    private static final String[][][] STATUS_BUTTON_TEXT_IDS = new String[][][]{
            { // Buy
                    {"+1", "Add 1", "Add 1 to quantity"},
                    {"+10", "Add 10", "Add 10 to quantity"},
                    {"+100", "Add 100", "Add 100 to quantity"},
                    {"+1k", "Add 1000", "Add 1,000 to quantity"}
            },
            { // Sell
                    {"1", "Sell 1", "Sell 1"},
                    {"10", "Sell 10", "Sell 10"},
                    {"100", "Sell 100", "Sell 100"},
                    {"ALL", "Sell All", "Sell all"}
            }
    };
    /**
     * The offer status tooltips.
     */
    private static final String[] TOOLTIPS = new String[]{"Abort Offer", "View Offer"};
    /**
     * The completed offer status tooltips.
     */
    private static final String[] COMPLETED_TOOLTIPS = new String[]{null, "View Offer"};
    /**
     * The pending sprite.
     */
    public final Sprite pending;
    /**
     * The pending sprite.
     */
    public final Sprite pendingLong;
    /**
     * The pending sprite.
     */
    public final Sprite pendingShort;
    /**
     * The {@link com.runescape.Game} instance.
     */
    private final Game game;
    /**
     * Represents a {@link com.runescape.cache.config.ge.GrandExchangeOffer}.
     */
    private final GrandExchangeOffer[] grandExchangeOffers;
    /**
     * The completed sprite.
     */
    private final Sprite completed;
    /**
     * The completed sprite.
     */
    private final Sprite completedLong;
    /**
     * The completed sprite.
     */
    private final Sprite completedShort;
    /**
     * The aborted offer sprite.
     */
    private final Sprite aborted;
    /**
     * The aborted offer sprite.
     */
    private final Sprite abortedLong;
    /**
     * The aborted offer sprite.
     */
    private final Sprite abortedShort;
    public boolean buttonClicked;
    public int totalItemResults;
    public boolean typing;
    public long searchTimer;
    /**
     * The current scroll position.
     */
    public int scrollPosition;
    /**
     * The max scroll length.
     */
    public int scrollMax;
    public int grandExchangeSelection = -1;
    public int grandExchangeItemIndex = -1;
    private Map<Integer, String> itemResults = new HashMap<>();

    /**
     * Constructs the <code>GrandExchange</code> class.
     *
     * @param game    The {@link com.runescape.Game} instance.
     * @param pending The pending sprites.
     * @param aborted The aborted sprite.
     */
    public GrandExchange(Game game, Sprite completed, Sprite pending, Sprite aborted, Sprite completedLong, Sprite pendingLong, Sprite abortedLong, Sprite completedShort, Sprite pendingShort, Sprite abortedShort) {
        this.game = game;
        this.grandExchangeOffers = new GrandExchangeOffer[6];
        for (int index = 0; index < 6; index++) {
            grandExchangeOffers[index] = new GrandExchangeOffer();
        }
        this.completed = completed;
        this.pending = pending;
        this.aborted = aborted;
        this.completedLong = completedLong;
        this.pendingLong = pendingLong;
        this.abortedLong = abortedLong;
        this.completedShort = completedShort;
        this.pendingShort = pendingShort;
        this.abortedShort = abortedShort;
    }

    /**
     * Resets an offer.
     *
     * @param index The offer index.
     */
    public void reset(int index) {
        GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[index] = new GrandExchangeOffer();
        GrandExchangeSlot grandExchangeSlot = GrandExchangeSlot.forId(index + 1);
        if (grandExchangeOffer.getOfferState() == null) { // Show Buy/Sell
            hideMainComponents(grandExchangeSlot, grandExchangeOffer.getOfferState(), false, true, true);
            grandExchangeSlot.clear();
        }
    }

    /**
     * Resets all offers.
     */
    public void reset() {
        clearResults();
        for (int index = 0; index < 6; index++) {
            GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[index] = new GrandExchangeOffer();
            GrandExchangeSlot grandExchangeSlot = GrandExchangeSlot.forId(index + 1);
            if (grandExchangeOffer.getOfferState() == null) { // Show Buy/Sell
                hideMainComponents(grandExchangeSlot, grandExchangeOffer.getOfferState(), false, true, true);
                grandExchangeSlot.clear();
            }
        }
    }

    /**
     * Updates the Grand Exchange components.
     */
    public void update(int componentId) {
        if (componentId == GRAND_EXCHANGE_MAIN_INTERFACE_ID) {
            updateMainComponent();
        } else if (componentId == GRAND_EXCHANGE_OFFER_INTERFACE_ID &&
                game.getInterfaceConfiguration(InterfaceConfiguration.GRAND_EXCHANGE_VIEW_SCREEN)) {
            updateOfferComponent();
        } else if (componentId == GRAND_EXCHANGE_COLLECTION_BOX_INTERFACE_ID) {
            updateCollectionBoxComponent();
        }
    }

    /**
     * Updates the  Grand Exchange collection box component.
     */
    public void updateCollectionBoxComponent() {
        int statusX = 100;
        int statusY = 107;
        int frameHeight = Game.getFrameHeight();
        int frameWidth = Game.getFrameWidth();
        boolean fixed = Game.isFixed();
        for (int index = 0; index < grandExchangeOffers.length; index++) {
            GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[index];
            OfferState offerState = grandExchangeOffer.getOfferState();
            if (!fixed) {
                statusX = (statusX + ((frameWidth / 2) - 356));
                statusY = (statusY + ((frameHeight / 2) - 230));
            }
            RSComponent rsComponent1 = RSComponent.forId(25537 + index);
            RSComponent rsComponent = RSComponent.forId(25555 + index);
            if (rsComponent.mediaType == 4) {
                rsComponent1.popupString = isCompleted(offerState) ? "Completed" : isAborted(offerState) ?
                        "Aborted" : (isBuyingState(offerState) || isSellingState(offerState)) ? "Pending" : null;
                ItemDefinition itemDefinition = ItemDefinition.forId(rsComponent.mediaID);
                if (itemDefinition != null) {
                    rsComponent.popupString = itemDefinition.name;
                }
            } else {
                rsComponent.mediaType = 0;
                rsComponent.mediaID = 0;
                rsComponent1.popupString = null;
                rsComponent.popupString = null;
            }
            int bought = grandExchangeOffer.getCompleted();
            int buying = grandExchangeOffer.getAmount();
            if (isAborted(offerState)) {
                abortedShort.drawSprite(statusX, statusY);
            } else if (isCompleted(offerState)) {
                completedShort.drawSprite(statusX, statusY);
            } else if (isBuyingState(offerState) || isSellingState(offerState)) {
                int offerPercentage = ((bought * 42) / buying);
                if (offerPercentage >= 40) {
                    offerPercentage = 39;
                }
                if (offerPercentage > 0) {
                    pendingShort.cropImage(0, 0, offerPercentage, 0).drawSprite(statusX, statusY);
                }
            }
            statusX += 116;
            if (index == 2) {
                statusX = 100;
                statusY += 90;
            }
        }
        int collectionBox = 25597;
        int collectionBoxId = 25561;
        for (int index = 0; index < 12; index++) {
            RSComponent rsComponent = RSComponent.forId(collectionBox); // Item in slot
            RSComponent rsComponent1 = RSComponent.forId(collectionBoxId + (collectionBox - 25597));
            if (rsComponent.inventory.length == 1 && rsComponent.inventory[0] > 0) {
                ItemDefinition itemDefinition = ItemDefinition.forId(rsComponent.inventory[0] - 1);
                boolean noted = itemDefinition.noteTemplateId == 799;
                boolean hasNote = !noted && itemDefinition.noteItemId != -1 && !itemDefinition.stackingType;
                String s = rsComponent.inventoryValue[0] > 1 ? "s" : "";
                rsComponent1.tooltip = null;
                rsComponent1.tooltips = new String[]{
                        (itemDefinition.stackingType ? "Collect" : "Collect-item" + s) + " <col=FF9040>" + itemDefinition.name,
                        hasNote ? "Collect-note" + s + " <col=FF9040>" + itemDefinition.name : null,
                        "Bank <col=FF9040>" + itemDefinition.name
                };
            } else {
                rsComponent1.tooltip = "Collect";
                rsComponent1.tooltips = null;
            }
            collectionBoxId += 2;
            collectionBox++;
        }
    }

    /**
     * Updates the Grand Exchange offer component.
     */
    public void updateOfferComponent() {
        int statusX = 71;
        int statusY = 300;
        GrandExchangeSlot grandExchangeSlot = GrandExchangeSlot.forId(game.getInterfaceConfig(InterfaceConfiguration.GRAND_EXCHANGE_VIEW_SCREEN));
        GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[grandExchangeSlot.ordinal()];
        OfferState offerState = grandExchangeOffer.getOfferState();
        int frameHeight = Game.getFrameHeight();
        int frameWidth = Game.getFrameWidth();
        boolean fixed = Game.isFixed();
        if (!fixed) {
            statusX = (statusX + ((frameWidth / 2) - 356));
            statusY = (statusY + ((frameHeight / 2) - 230));
        }
        int bought = grandExchangeOffer.getCompleted();
        int buying = grandExchangeOffer.getAmount();
        RSComponent.forId(25490).popupString = isAborted(offerState) ? "Aborted" : isCompleted(offerState) ? "Completed" : "In progress";
        // Collection boxes
        int collectionBox = 25488;
        int collectionBoxId = 25482;
        for (int index = 0; index < 2; index++) {
            RSComponent rsComponent = RSComponent.forId(collectionBox); // Item in slot
            RSComponent rsComponent1 = RSComponent.forId(collectionBoxId + (collectionBox - 25488));
            if (rsComponent.inventory.length == 1 && rsComponent.inventory[0] > 0) {
                ItemDefinition itemDefinition = ItemDefinition.forId(rsComponent.inventory[0] - 1);
                boolean noted = itemDefinition.noteTemplateId == 799;
                boolean hasNote = !noted && itemDefinition.noteItemId != -1 && !itemDefinition.stackingType;
                String s = rsComponent.inventoryValue[0] > 1 ? "s" : "";
                rsComponent1.tooltip = null;
                rsComponent1.tooltips = new String[]{
                        (itemDefinition.stackingType ? "Collect" : "Collect-item" + s) + " <col=FF9040>" + itemDefinition.name,
                        hasNote ? "Collect-note" + s + " <col=FF9040>" + itemDefinition.name : null,
                        "Bank <col=FF9040>" + itemDefinition.name
                };
            } else {
                rsComponent1.tooltip = "Collect";
                rsComponent1.tooltips = null;
            }
            collectionBoxId += 2;
            collectionBox++;
        }

        if (isAborted(offerState)) {
            abortedLong.drawSprite(statusX, statusY);
        } else if (isCompleted(offerState)) {
            completedLong.drawSprite(statusX, statusY);
        } else if (isBuyingState(offerState) || isSellingState(offerState)) {
            int offerPercentage = ((bought * 301) / buying);
            if (offerPercentage >= 299) {
                offerPercentage = 298;
            }
            if (offerPercentage > 0) {
                pendingLong.cropImage(0, 0, offerPercentage, 0).drawSprite(statusX, statusY);
            }
        }
    }

    /**
     * Updates the main Grand Exchange component.
     */
    public void updateMainComponent() { // TODO We should update systematically?
        int statusX = 38;
        int statusY = 155;
        for (int index = 0; index < 6; index++) {
            boolean cont = false;
            GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[index];
            GrandExchangeSlot grandExchangeSlot = GrandExchangeSlot.forId(index + 1);
            if (grandExchangeOffer.getOfferState() == null) { // Show Buy/Sell
                hideMainComponents(grandExchangeSlot, grandExchangeOffer.getOfferState(), false, true, true);
                grandExchangeSlot.clear();
                cont = true;
            }
            if (!cont) {
                hideMainComponents(grandExchangeSlot, grandExchangeOffer.getOfferState(), true, false, false);
                drawPercentage(grandExchangeSlot, statusX, statusY);
            }
            statusX += 156;
            if (index == 2) {
                statusX = 38;
                statusY += 120;
            }
        }
    }

    /**
     * Hides components on the main Grand Exchange interface.
     *
     * @param grandExchangeSlot The {@link com.runescape.cache.config.ge.GrandExchangeSlot}.
     * @param offerState        The {@link com.runescape.cache.config.ge.OfferState}.
     * @param buttons           If the buy and sell buttons are hidden.
     * @param status            If the status' are hidden.
     * @param offers            If the offers are hidden.
     */
    private void hideMainComponents(GrandExchangeSlot grandExchangeSlot, OfferState offerState, boolean buttons, boolean status, boolean offers) {
        RSComponent.forId(24920 + grandExchangeSlot.ordinal()).disabledMessage = offerState == null ? "Empty" :
                (isBuyingState(offerState)) ? "Buy" :
                        (isSellingState(offerState)) ? "Sell" : "Empty";
        for (int buttonId : grandExchangeSlot.getButtonIds()) {
            RSComponent rsComponent = RSComponent.forId(buttonId);
            if (rsComponent == null) {
                continue;
            }
            rsComponent.hidden = rsComponent.drawSprite || buttons;
        }
        for (int statusId : grandExchangeSlot.getStatusIds()) {
            RSComponent rsComponent = RSComponent.forId(statusId);
            if (rsComponent == null) {
                continue;
            }
            rsComponent.hidden = grandExchangeSlot.isDisabled() || status;
        }
        for (int offerId : grandExchangeSlot.getOfferIds()) {
            RSComponent rsComponent = RSComponent.forId(offerId);
            if (rsComponent == null) {
                continue;
            }
            rsComponent.hidden = grandExchangeSlot.isDisabled() || offers;
        }
    }

    /**
     * Draws the percentage for an offer.
     *
     * @param grandExchangeSlot The {@link com.runescape.cache.config.ge.GrandExchangeSlot}.
     */
    public void drawPercentage(GrandExchangeSlot grandExchangeSlot, int statusX, int statusY) {
        GrandExchangeOffer grandExchangeOffer = grandExchangeOffers[grandExchangeSlot.ordinal()];
        OfferState offerState = grandExchangeOffer.getOfferState();
        int frameHeight = Game.getFrameHeight();
        int frameWidth = Game.getFrameWidth();
        boolean fixed = Game.isFixed();
        if (!fixed) {
            statusX = (statusX + ((frameWidth / 2) - 356));
            statusY = (statusY + ((frameHeight / 2) - 230));
        }
        int bought = grandExchangeOffer.getCompleted();
        int buying = grandExchangeOffer.getAmount();
        String offerType = offerState == null ? "null" :
                (offerState == OfferState.ABORTED_BUY || offerState == OfferState.BUYING) ? "Buying" :
                        (offerState == OfferState.ABORTED_SELL || offerState == OfferState.SELLING) ? "Selling" : "null";
        if (offerState != null && isCompleted(offerState)) {
            offerType = "Completed";
        }
        if (offerState != null && isAborted(offerState)) {
            offerType = "Aborted";
        }
        ItemDefinition itemDefinition = ItemDefinition.forId(grandExchangeOffer.getItemId());
        GameFont smallFont = game.smallFont;
        String itemName = itemDefinition.name;
        int nameWidth = smallFont.getTextWidth(itemName);
        String finalName = "";
        if (nameWidth > 12) {
            String[] names = itemName.split(" ");
            for (String name : names) {
                finalName += name + " ";
                if (finalName.length() >= 13) {
                    finalName += "\\n";
                    break;
                }
            }
            int length = finalName.length();
            if (length < itemName.length()) {
                itemName = finalName + itemName.substring(length - 2, itemName.length());
            }
        }
        RSComponent.forId(grandExchangeSlot.getItemNameId()).disabledMessage = itemName;
        RSComponent.forId(grandExchangeSlot.getItemNameId()).enabledMessage = itemName;
        RSComponent.forId(grandExchangeSlot.getItemId()).inventory[0] = itemDefinition.id + 1;
        RSComponent.forId(grandExchangeSlot.getItemId()).inventoryValue[0] = grandExchangeOffer.getAmount();
        RSComponent.forId(grandExchangeSlot.getStatusId()).popupString = isAborted(offerState) ? "Aborted" : isCompleted(offerState) ? "Completed" :
                (offerType + ": " + itemDefinition.name + "\\n" + bought + " / " + buying);
        RSComponent.forId(grandExchangeSlot.getStatusId()).tooltips = isCompleted(offerState) ? COMPLETED_TOOLTIPS : TOOLTIPS;
        RSComponent.forId(grandExchangeSlot.getValueId()).disabledMessage = NumberFormat.getNumberInstance(Locale.US).format(grandExchangeOffer.getOffered()) + " coins";
        if (isAborted(offerState)) {
            aborted.drawSprite(statusX, statusY);
        } else if (isCompleted(offerState)) {
            completed.drawSprite(statusX, statusY);
        } else if (isBuyingState(offerState) || isSellingState(offerState)) {
            int offerPercentage = ((bought * 124) / buying);
            if (offerPercentage >= 122) {
                offerPercentage = 121;
            }
            if (offerPercentage > 0) {
                pending.cropImage(0, 0, offerPercentage, 0).drawSprite(statusX, statusY);
            }
        }
    }

    public void findItem(String itemName) {
        if (searchTimer > 0) {
            searchTimer--;
            return;
        }
        searchTimer = 10;
        if (itemName == null || itemName.length() == 0) {
            totalItemResults = 0;
            scrollMax = 0;
            scrollPosition = 0;
            return;
        }
        String searchName = itemName;
        String searchParts[] = new String[100];
        int totalResults = 0;
        do {
            int k = searchName.indexOf(" ");
            if (k == -1) {
                break;
            }
            String part = searchName.substring(0, k).trim();
            if (part.length() > 0) {
                searchParts[totalResults++] = part.toLowerCase();
            }
            searchName = searchName.substring(k + 1);
        } while (true);
        searchName = searchName.trim();
        if (searchName.length() > 0) {
            searchParts[totalResults++] = searchName.toLowerCase();
        }
        totalItemResults = 0;
        itemResults.clear();
        label0:
        for (int id = 0; id < ItemDefinition.getItemCount(); id++) {
            ItemDefinition itemDefinition = ItemDefinition.forId(id);
            if (itemDefinition.noteTemplateId != -1 || itemDefinition.lentTemplateId != -1
                    || itemDefinition.name == null || Objects.equals(itemDefinition.name, "Picture")
                    || itemDefinition.noteItemId == 18786 || Objects.equals(itemDefinition.name, "null") || itemDefinition.name.toLowerCase().contains("coins")
                    || itemDefinition.value <= 0 || itemDefinition.noteTemplateId != -1
                    || !itemDefinition.tradable || itemDefinition.blacklisted) {
                continue;
            }
            String result = itemDefinition.name.toLowerCase();
            for (int idx = 0; idx < totalResults; idx++) {
                if (!result.contains(searchParts[idx])) {
                    continue label0;
                }
            }
            if (totalItemResults >= (MAXIMUM_SEARCH_RESULTS + 1)) {
                break;
            }
            itemResults.put(id, result);
            totalItemResults++;
        }
        scrollMax = totalItemResults < 8 ? 107 : totalItemResults * 14;
        itemResults = MapUtil.sortByValue(itemResults);
    }

    public void buildItemSearch(int mouseY) {
        if (typing) {
            grandExchangeItemIndex = -1;
            scrollPosition = 0;
            typing = false;
        }
        int yOffset = 0;
        for (int itemIndex = 0; itemIndex < MAXIMUM_SEARCH_RESULTS; itemIndex++) {
            if (game.amountOrNameInput.length() == 0) {
                return;
            } else if (totalItemResults == 0) {
                return;
            }
            if (game.amountOrNameInput.isEmpty() || Objects.equals(game.amountOrNameInput, "")) {
                return;
            }
            if (itemIndex > totalItemResults || itemIndex >= itemResults.size()) {
                break;
            }
            int textY = (21 + yOffset * 14) - scrollPosition;
            if (mouseY > textY - 14 && mouseY <= textY && game.mouseX > 74
                    && game.mouseX < 495) {
                game.menuActionName[game.menuActionRow] = null;
                game.menuActionId[game.menuActionRow] = 1251;
                game.menuActionRow++;
            }
            yOffset++;
        }
    }

    public void clearResults() {
        game.amountOrNameInput = "";
        game.inputState = 0;
        Game.inputTaken = true;
        totalItemResults = 0;
        scrollMax = 0;
        scrollPosition = 0;
        itemResults.clear();
    }

    public void displayItemSearch() {
        int yPosOffset = (!Game.isFixed()) ? Game.getFrameHeight() - 165 : 0;
        int xPosOffset = 0;
        try {
            buttonClicked = true;
            if (buttonClicked && game.inputState == 3) {
                if (game.amountOrNameInput != "") {
                    findItem(game.amountOrNameInput);
                }
                Raster.setDrawingArea(121 + yPosOffset, 8, 512, 7);
                ImageLoader.forName("GE_ITEM_RESULT").drawSprite(17, 15 + yPosOffset);
                if (totalItemResults > MAXIMUM_SEARCH_RESULTS) {
                    game.smallFont.drawCenteredString("Too many results. Please refine your search.", 282, 67 + yPosOffset, 0xA05A00);
                    itemResults.clear();
                    scrollMax = 0;
                    scrollPosition = 0;
                } else {
                    for (int resultIndex = 0; resultIndex < totalItemResults; resultIndex++) {
                        int x = game.mouseX;
                        int y = game.mouseY;
                        final int yPos = 21 + resultIndex * 14 - scrollPosition;
                        if (yPos > 0 && yPos < 210) {
                            String itemResultName = (String) itemResults.values().toArray()[resultIndex];
                            for (int i = 0; i <= 20; i++) {
                                if (itemResultName.contains("<img=" + i + ">")) {
                                    itemResultName = itemResultName.replaceAll("<img=" + i + ">", "");
                                }
                            }

                            game.regularFont.drawBasicString(StringUtility.uppercaseFirst(itemResultName), 71, (yPos - 9) + yPosOffset + (totalItemResults < 8 ? 6 : 6), 0xA05A00, -1, false);
                            if ((x > 68 && x < 495 && y > ((Game.isFixed()) ? 332 : Game.getFrameHeight() - 171)
                                    + yPos - 13 + (totalItemResults < 8 ? 6 : 6) && y < ((Game.isFixed()) ? 332
                                    : Game.getFrameHeight() - 171) + yPos + 2 + (totalItemResults < 8 ? 6 : 6))) {
                                Raster.fillRectangle2(0x000000, yPos - 19
                                                + yPosOffset + (totalItemResults < 8 ? 6 : 6),
                                        428, 13, 25, 68);
                                Sprite itemResultImage = ItemDefinition.getSprite((int) itemResults.keySet().toArray()[resultIndex], 1, 0);
                                if (itemResultImage != null) {
                                    itemResultImage.drawSprite(20, 18 + yPosOffset);
                                }
                                grandExchangeSelection = (int) itemResults.keySet().toArray()[resultIndex];
                            }
//                            else if (grandExchangeItemIndex != -1) {
//                                Raster.fillRectangle2(0x000000, yPos - 19 + yPosOffset + (totalItemResults < 8 ? 6 : 6), 428, 13, 25, 68);
//                                Sprite itemResultImage = ItemDefinition.getSprite(itemResultIDs[grandExchangeItemIndex], 1, 0);
//                                if (itemResultImage != null) {
//                                    itemResultImage.drawSprite(20, 18 + yPosOffset);
//                                }
//                                grandExchangeSelection = itemResultIDs[grandExchangeItemIndex];
//                            }
                        }
                    }
                }
                Raster.drawPixels(113, 7 + yPosOffset, 66, 0x897C64, 1);
                Raster.drawPixels(113, 7 + yPosOffset, 67, 0x978A72, 1);
                Raster.defaultDrawingAreaSize();
                Scrollbar.draw(game, SEARCH_HEIGHT, scrollPosition, 8 + yPosOffset, 496 + xPosOffset, scrollMax < 107 ? 107 : scrollMax, false, false);
                boolean showMatches = true;
                showMatches = true;
                if (game.amountOrNameInput.length() == 0) {
                    game.boldFont.drawCenteredString("Grand Exchange Item Search", 283, 32 + yPosOffset, 0xA05A00, false);
                    game.smallFont.drawCenteredString("To search for an item, start by typing part of its name.", 281, 70 + yPosOffset, 0xA05A00, false);
                    game.smallFont.drawCenteredString("Then, simply select the item you want from the results on display.", 282, 85 + yPosOffset, 0xA05A00, false);
                    showMatches = false;
                }
                if (totalItemResults == 0 && showMatches) {
                    game.smallFont.drawCenteredString("No matching items found", 280, 67 + yPosOffset, 0xA05A00);
                }
                Raster.fillRectangle2(0x807660, 121 + yPosOffset, 506, 15, 120, 7);
                ImageLoader.forName("GE_SEARCH_BACK").drawSprite(7, 114 + yPosOffset);
                game.regularFont.drawBasicString(game.amountOrNameInput + "*", 34 + xPosOffset, 130 + yPosOffset, 0xFFFFFF, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the search box scrolling.
     *
     * @param notches The notches to scroll.
     */
    public void handleScrolling(int notches) {
        if (scrollMax > SEARCH_HEIGHT) {
            int scrollPos = scrollPosition;
            scrollPos += notches * 30;
            if (scrollPos < 0) {
                scrollPos = 0;
            }
            if (scrollPos > scrollMax - 107) {
                scrollPos = scrollMax - 107;
            }
            if (scrollPosition != scrollPos) {
                scrollPosition = scrollPos;
                Game.inputTaken = true;
            }
        }
    }

    /**
     * Updates the offer component type to buy or sell.
     *
     * @param configValue The configuration value.
     */
    public void updateOfferComponent(int configValue) {
        if (configValue > 1) {
            configValue = 0;
            game.setInterfaceConfig(InterfaceConfiguration.GRAND_EXCHANGE_OFFER_TYPE, configValue);
        }
        for (int index1 = 0; index1 < STATUS_BUTTON_TEXT_IDS[configValue].length; index1++) {
            int componentId = STATUS_BUTTON_IDS[index1];
            RSComponent.forId(componentId).disabledMessage = STATUS_BUTTON_TEXT_IDS[configValue][index1][0];
            RSComponent.forId(componentId + 1).tooltip = STATUS_BUTTON_TEXT_IDS[configValue][index1][1];
            RSComponent.forId(componentId + 1).popupString = STATUS_BUTTON_TEXT_IDS[configValue][index1][2];
        }
    }

    /**
     * Gets the {@link com.runescape.cache.config.ge.GrandExchangeOffer}s.
     *
     * @return The offers.
     */
    public GrandExchangeOffer[] getGrandExchangeOffers() {
        return grandExchangeOffers;
    }

    /**
     * Gets a {@link com.runescape.cache.config.ge.GrandExchangeOffer}.
     *
     * @param index The slot index.
     * @return The offer.
     */
    public GrandExchangeOffer getGrandExchangeOffer(int index) {
        return grandExchangeOffers[index];
    }

    /**
     * Sets a {@link com.runescape.cache.config.ge.GrandExchangeOffer}.
     *
     * @param index              The slot index.
     * @param grandExchangeOffer The {@link com.runescape.cache.config.ge.GrandExchangeOffer} to set.
     */
    public void setGrandExchangeOffer(int index, GrandExchangeOffer grandExchangeOffer) {
        this.grandExchangeOffers[index] = grandExchangeOffer;
    }

    /**
     * Checks if an offer is completed.
     *
     * @param offerState The {@link com.runescape.cache.config.ge.OfferState}.
     * @return <code>True</code> if so.
     */
    private boolean isCompleted(OfferState offerState) {
        return offerState == OfferState.COMPLETED || offerState == OfferState.COMPLETED_BUY || offerState == OfferState.COMPLETED_SELL;
    }

    /**
     * Checks if an offer is aborted.
     *
     * @param offerState The {@link com.runescape.cache.config.ge.OfferState}.
     * @return <code>True</code> if so.
     */
    private boolean isAborted(OfferState offerState) {
        return offerState == OfferState.ABORTED || offerState == OfferState.ABORTED_BUY || offerState == OfferState.ABORTED_SELL;
    }

    /**
     * Checks if an offer is in a buying state.
     *
     * @param offerState The {@link com.runescape.cache.config.ge.OfferState}.
     * @return <code>True</code> if so.
     */
    private boolean isBuyingState(OfferState offerState) {
        return offerState == OfferState.BUYING || offerState == OfferState.COMPLETED_BUY || offerState == OfferState.ABORTED_BUY;
    }

    /**
     * Checks if an offer is in a selling state.
     *
     * @param offerState The {@link com.runescape.cache.config.ge.OfferState}.
     * @return <code>True</code> if so.
     */
    private boolean isSellingState(OfferState offerState) {
        return offerState == OfferState.SELLING || offerState == OfferState.COMPLETED_SELL || offerState == OfferState.ABORTED_SELL;
    }

}