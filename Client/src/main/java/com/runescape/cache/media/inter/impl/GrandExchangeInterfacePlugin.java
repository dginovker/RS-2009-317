package com.runescape.cache.media.inter.impl;

import com.runescape.Game;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the {@link com.runescape.cache.media.inter.InterfacePlugin} for Grand Exchange interfaces.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class GrandExchangeInterfacePlugin implements InterfacePlugin {

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        loadMain(gameFonts);
        return loadSellingInventoryInterface(gameFonts);
    }

    /**
     * The selling inventory interface.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     * @return The component.
     */
    public RSComponent loadSellingInventoryInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25678);
        rsComponent.addToItemGroup(25679, 4, 7, 10, 4, true, true, true, "Offer");
        rsComponent.addChild(25679, 16, 8);
        return rsComponent;
    }

    /**
     * The main Grand Exchange component.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     * @return The component.
     */
    public RSComponent loadMain(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(24907);
        int index = 0;
        rsComponent.addSprite(24908, ImageLoader.forName("GRAND_EXCHANGE_BACK")).addAlphaBox(9, 10, 4, 6, 0, 80);
        rsComponent.addHoverButton(24909, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 24910, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(24910, SpriteRepository.SMALL_X_HOVER, 16, 16, 24911);
        rsComponent.addText(24912, "Grand Exchange", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 200, 16, -1);
        rsComponent.addText(24913, "Shown below is a summary of all your current offers.", gameFonts, RSComponent.SMALL, 0xCC9900, true, true, 200, 16, -1);

        // Add boxes
        for (int boxId = 24914; boxId <= 24919; boxId++) {
            rsComponent.addSprite(boxId, ImageLoader.forName("GRAND_EXCHANGE_OFFER"));
        }
        // Add disabled slot covers
        for (int boxId = 25470; boxId <= 25475; boxId++) {
            rsComponent.addSprite(boxId, ImageLoader.forName("GE_SLOT_DISABLED")).hidden = true;
        }
        // Add text
        for (int textId = 24920; textId <= 24925; textId++) {
            rsComponent.addText(textId, "Empty", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 100, 16, -1);
        }
        for (int buttonId = 24926; buttonId < 24962; buttonId++) {
            rsComponent.addHoverButton(buttonId, ImageLoader.forName("GE_BUY"), 51, 48, "Make Buy Offer", 0, buttonId + 1, RSComponent.BUTTON_ACTION_TYPE);
            rsComponent.addHoveredButton(buttonId + 1, ImageLoader.forName("GE_BUY_HOVER"), 51, 52, buttonId + 2);
            buttonId += 3;
            rsComponent.addHoverButton(buttonId, ImageLoader.forName("GE_SELL"), 51, 48, "Make Sell Offer", 0, buttonId + 1, RSComponent.BUTTON_ACTION_TYPE);
            rsComponent.addHoveredButton(buttonId + 1, ImageLoader.forName("GE_SELL_HOVER"), 51, 52, buttonId + 2);
            buttonId += 3;
        }
        // Offer submitting/submitted
        index = 0;
        for (int buttonId = 24967; buttonId <= 24993; buttonId++) {
            rsComponent.addHoveredButton(buttonId + 1, ImageLoader.forName("GRAND_EXCHANGE_OFFER_HOVER"), 140, 110, buttonId + 2);
            RSComponent rsComponent1 = rsComponent.addHoverButton(buttonId, ImageLoader.forName("GRAND_EXCHANGE_OFFER"), 140, 110,
                    "View Offer", 0, buttonId + 1,
                    RSComponent.BUTTON_ACTION_TYPE);
            rsComponent1.tooltips = new String[]{"Abort Offer", "View Offer"};
            rsComponent1.popupString = "" + buttonId;
            index++;
            buttonId += 3;
        }
        // Current pending offers
        for (int statusId = 24994; statusId < 25030/*25006 + (6 + 6 + 6 + 6)*/; statusId++) {
            rsComponent.addSprite(statusId++, ImageLoader.forName("GE_OFFER_ITEM"));
            rsComponent.addToItemGroup(statusId++, 41, 36, 0, 0, true, false, true, false);
            rsComponent.addText(statusId++, "", gameFonts, RSComponent.SMALL, 0xFFB83F, false, true, 50, 40, -1);
            rsComponent.addText(statusId++, "", gameFonts, RSComponent.SMALL, 0xFF981F, true, true, 50, 14, -1);
            rsComponent.addSprite(statusId++, new SpriteSet(ImageLoader.forName("GE_OFFER_STATUS"), ImageLoader.forName("GE_OFFER_STATUS")), -1, -1, false, 127, 16);
            rsComponent.addText(statusId, "", gameFonts, RSComponent.SMALL, 0xBDBB5B, true, true, 50, 14, -1).hidden = true;
        }

        rsComponent.addChild(24908, 13, 20);
        rsComponent.addChild(24909, 473, 30);
        rsComponent.addChild(24910, 473, 30);
        rsComponent.addChild(24912, 156, 30);
        rsComponent.addChild(24913, 156, 60);
        index = 0;
        int boxX = 30;
        int boxY = 80;
        for (int boxId = 24914; boxId <= 24919; boxId++) {
            rsComponent.addChild(boxId, boxX, boxY);
            boxX += 156;
            index++;
            if ((index % 3) == 0) {
                boxX = 30;
                boxY += 120;
            }
        }
        index = 0;
        boxX = 30;
        boxY = 80;
        for (int boxId = 25470; boxId <= 25475; boxId++) {
            rsComponent.addChild(boxId, boxX, boxY);
            boxX += 156;
            index++;
            if ((index % 3) == 0) {
                boxX = 30;
                boxY += 120;
            }
        }
        index = 0;
        int buttonX = 42;
        int buttonY = 122;
        for (int buttonId = 24926; buttonId < 24962; buttonId++) {
            // Buys
            rsComponent.addChild(buttonId, buttonX, buttonY);
            rsComponent.addChild(buttonId + 1, buttonX, buttonY - 9);
            buttonId += 3;
            // Sells
            rsComponent.addChild(buttonId, buttonX + 63, buttonY);
            rsComponent.addChild(buttonId + 1, buttonX + 63, buttonY - 9);
            buttonId += 3;
            buttonX += 156;
            index++;
            if ((index % 3) == 0) {
                buttonX = 42;
                buttonY += 120;
            }
        }

        rsComponent.addChild(24987, 342, 199);
        rsComponent.addChild(24983, 186, 199);
        rsComponent.addChild(24979, 30, 199);
        rsComponent.addChild(24975, 342, 79);
        rsComponent.addChild(24971, 186, 79);
        rsComponent.addChild(24967, 30, 79);
        rsComponent.addChild(24988, 342, 199);
        rsComponent.addChild(24980, 30, 199);
        rsComponent.addChild(24984, 186, 199);
        rsComponent.addChild(24968, 30, 79);
        rsComponent.addChild(24972, 186, 79);
        rsComponent.addChild(24976, 342, 79);

        index = 0;
        int textX = 49;
        int textY = 83;
        for (int boxId = 24920; boxId <= 24925; boxId++) {
            rsComponent.addChild(boxId, textX, textY);
            textX += 156;
            index++;
            if ((index % 3) == 0) {
                textX = 49;
                textY += 120;
            }
        }
        index = 0;
        int itemX = 36;
        int itemY = 110;
        for (int statusId = 24994; statusId < 25006 + (6 + 6 + 6 + 6); statusId++) {
            rsComponent.addChild(statusId++, itemX, itemY);
            rsComponent.addChild(statusId++, itemX + 2, itemY + 2);
            rsComponent.addChild(statusId++, itemX + 43, itemY + 2);
            rsComponent.addChild(statusId++, itemX + 39, itemY + 63);
            rsComponent.addChild(statusId++, itemX + 1, itemY + 44);
            rsComponent.addChild(statusId, itemX + 39, itemY + 49);
            itemX += 156;
            index++;
            if ((index % 3) == 0) {
                itemX = 36;
                itemY += 120;
            }
        }
        loadOfferComponent(gameFonts);
        loadItemHistoryComponent(gameFonts);
        loadItemSetComponent(gameFonts);
        loadCollectionComponent(gameFonts);
        Game.INSTANCE.spriteTimers[0][0] = 10; // Inventory sell flash
        Game.INSTANCE.spriteTimers[0][1] = 150; // Inventory sell flash
        Game.INSTANCE.spriteAlphaValues[0][1] = 3;
        return rsComponent;
    }

    /**
     * The collection Grand Exchange component.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     * @return The component.
     */
    public RSComponent loadCollectionComponent(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25531);
        rsComponent.addSprite(25532, ImageLoader.forName("GRAND_EXCHANGE_COLLECT_BACK")).addAlphaBox(9, 10, 4, 6, 0, 80);
        rsComponent.addHoverButton(25533, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25534, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25534, SpriteRepository.SMALL_X_HOVER, 16, 16, 25535);
        rsComponent.addText(25536, "Grand Exchange - Collection Box", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 16, -1);

        for (int index = 0; index < 6; index++) {
            rsComponent.addText(25537 + index, "", gameFonts, 0, 0, 0, false, false, 41, 16, -1).popupString = "Aborted";
            RSComponent rsComponent1 = rsComponent.addSprite(25543 + index, new SpriteSet(ImageLoader.forName("GE_BUY_ICON_SMALL"),
                    ImageLoader.forName("GE_BUY_ICON_SMALL")), -1, -1, false, 11, 14);
            rsComponent1.popupString = "Buy";
            rsComponent1.hidden = true;

            rsComponent1 = rsComponent.addSprite(25549 + index, new SpriteSet(ImageLoader.forName("GE_SELL_ICON_SMALL"),
                    ImageLoader.forName("GE_SELL_ICON_SMALL")), -1, -1, false, 11, 14);
            rsComponent1.popupString = "Sell";
            rsComponent1.hidden = true;

            rsComponent1 = rsComponent.addModel(25555 + index, 18, 16);
            rsComponent1.hidden = true;
        }
        int buttonId = 25561;
        for (int index = 0; index < 12; index++) {
            addButton(rsComponent, buttonId, null, -1, -1, false, null, "EMPTY", "GE_COLLECT_OFFER_HOVER", "Collect", null, 40, 36);
            rsComponent.addToItemGroup(25597 + index, 1, 1, 0, 0, false, true);
            buttonId += 3;
        }

        rsComponent.addChild(25532, 74, 54);
        rsComponent.addChild(25533, 415, 63);
        rsComponent.addChild(25534, 415, 63);
        rsComponent.addChild(25536, 157, 63);

        int xOffset = 0;
        int yOffset = 0;
        for (int index = 0; index < 6; index++) {
            rsComponent.addChild(25537 + index, 99 + xOffset, 106 + yOffset); // Status
            rsComponent.addChild(25543 + index, 147 + xOffset, 107 + yOffset); // Icon
            rsComponent.addChild(25549 + index, 147 + xOffset, 107 + yOffset); // Icon
            rsComponent.addChild(25555 + index, 165 + xOffset, 106 + yOffset); // // Item icon TODO ZOOM 55 BEST
            xOffset += 116;
            if (index == 2) {
                xOffset = 0;
                yOffset += 90;
            }
        }
        xOffset = 0;
        yOffset = 0;
        buttonId = 25561;
        for (int index = 0; index < 12; index++) {
            rsComponent.addChild(buttonId, 99 + xOffset, 126 + yOffset); // Item collect slot
            rsComponent.addChild(buttonId + 1, 99 + xOffset, 126 + yOffset); // Item collect slot
            rsComponent.addChild(25597 + index, 102 + xOffset, 128 + yOffset); // Item
            buttonId += 3;
            xOffset += ((index % 2 == 0) ? 45 : 70);
            if (index == 5) {
                xOffset = 0;
                yOffset += 90;
            }
        }
        return rsComponent;
    }

    /**
     * The item sets Grand Exchange component.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     * @return The component.
     */
    public RSComponent loadItemSetComponent(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25521);
        rsComponent.addSprite(25522, ImageLoader.forName("GRAND_EXCHANGE_SET_BACK")).addAlphaBox(9, 10, 4, 6, 0, 80);
        rsComponent.addHoverButton(25523, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25524, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25524, SpriteRepository.SMALL_X_HOVER, 16, 16, 25525);
        rsComponent.addText(25526, "Grand Exchange Item Sets", gameFonts, RSComponent.BOLD, 0xFF981F, true, true, 200, 16, -1);
        rsComponent.addText(25527, "Click on a set you wish to purchase here", gameFonts, RSComponent.SMALL, 0xC8AB4A, true, true, 200, 16, -1);
        rsComponent.addText(25528, "or click on a set in your inventory that you wish to sell.", gameFonts, RSComponent.SMALL, 0xC8AB4A, true, true, 200, 16, -1);
        rsComponent.addChild(25522, 13, 20);
        rsComponent.addChild(25523, 474, 30);
        rsComponent.addChild(25524, 474, 30);
        rsComponent.addChild(25526, 155, 30);
        rsComponent.addChild(25527, 157, 295);
        rsComponent.addChild(25528, 157, 305);
        rsComponent.addChild(25529, 29, 62);
        RSComponent scrollComponent = rsComponent.addInterface(25529);
        scrollComponent.height = 225;
        scrollComponent.width = 445;
        scrollComponent.scrollMax = 820;
        scrollComponent.addToItemGroup(25530, 10, 14, 12, 26, false, true, false, true, "Components", "Exchange");
        scrollComponent.addChild(25530, 0, 0);
        return loadItemSetInventoryInterface(gameFonts);
    }


    /**
     * The item set inventory interface.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     * @return The component.
     */
    public RSComponent loadItemSetInventoryInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25680);
        rsComponent.addToItemGroup(25681, 4, 7, 10, 4, true, true, true, null, null, "Components", "Exchange");
        rsComponent.addChild(25681, 16, 8);
        return rsComponent;
    }

    /**
     * The item back / history Grand Exchange component.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     */
    public RSComponent loadItemHistoryComponent(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25491);
        rsComponent.addSprite(25492, ImageLoader.forName("GE_HISTORY_BACK")).addAlphaBox(9, 10, 4, 6, 0, 80);
        rsComponent.addHoverButton(25493, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25494, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25494, SpriteRepository.SMALL_X_HOVER, 16, 16, 25495);
        rsComponent.addText(25496, "Item Back History", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 200, 16, -1);

        rsComponent.addText(25497, "Offer type", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 114, 16, -1);
        rsComponent.addText(25498, "Item name", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 114, 16, -1);
        rsComponent.addText(25499, "Quantity", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 114, 16, -1);
        rsComponent.addText(25500, "Price", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 114, 16, -1);
        int[] colours = {0xD19E05, 0xC39000, 0xB78700, 0xAE7B00, 0xA47100};
        int textId = 25501;
        for (int row = 0; row < 5; row++) {
            rsComponent.addText(textId++, "-", gameFonts, RSComponent.SMALL, colours[row], true, true, 114, 16, -1);
            rsComponent.addText(textId++, "-", gameFonts, RSComponent.SMALL, colours[row], true, true, 114, 16, -1);
            rsComponent.addText(textId++, "-", gameFonts, RSComponent.SMALL, colours[row], true, true, 114, 16, -1);
            rsComponent.addText(textId++, "-", gameFonts, RSComponent.SMALL, colours[row], true, true, 114, 16, -1);
        }
        rsComponent.addChild(25492, 13, 20);
        rsComponent.addChild(25493, 474, 30);
        rsComponent.addChild(25494, 474, 30);
        rsComponent.addChild(25496, 155, 30);

        rsComponent.addChild(25497, 18, 70);
        rsComponent.addChild(25498, 152, 70);
        rsComponent.addChild(25499, 276, 70);
        rsComponent.addChild(25500, 382, 70);

        textId = 25501;
        int xOffset = 15;
        int yOffset = 124;
        for (int row = 0; row < 5; row++) {
            rsComponent.addChild(textId++, xOffset, yOffset + (6 * row));
            rsComponent.addChild(textId++, xOffset + 137, yOffset + (6 * row));
            rsComponent.addChild(textId++, xOffset + 263, yOffset + (6 * row));
            rsComponent.addChild(textId++, xOffset + 367 + (row == 0 ? 1 : 0), yOffset + (6 * row));
            yOffset += (row == 0 ? 35 : row == 1 ? 30 : row == 2 ? 32 : row == 3 ? 32 : 34);
        }
        return rsComponent;
    }

    /**
     * The buy offer Grand Exchange component.
     *
     * @param gameFonts The {@link com.runescape.media.font.GameFont}.
     * @return The component.
     */
    public RSComponent loadOfferComponent(GameFont[] gameFonts) {
        RSComponent rsComponent = new RSComponent().addInterface(25042);
        rsComponent.addSprite(25043, ImageLoader.forName("GRAND_EXCHANGE_OFFER_BACK")).addAlphaBox(9, 10, 4, 6, 0, 80);
        rsComponent.addHoverButton(25044, SpriteRepository.SMALL_X, 16, 16, "Close Window", 0, 25045, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(25045, SpriteRepository.SMALL_X_HOVER, 16, 16, 25046);
        rsComponent.addText(25047, "Grand Exchange", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 200, 16, -1);

        rsComponent.addText(25048, "Buy Offer", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 100, 16, -1);
        rsComponent.addSprite(25049, new SpriteSet(ImageLoader.forName("GE_SELL_ICON"), ImageLoader.forName("GE_BUY_ICON")), 1, 326, true);

        rsComponent.addText(25050, "Choose an item to exchange", gameFonts, RSComponent.BOLD, 0xCC9900, false, true, 100, 16, -1);
        rsComponent.addText(25051, "Click the icon to the left to search for items.", gameFonts, RSComponent.SMALL, 0xC1A875, false, true, 100, 16, -1);
        rsComponent.addText(25052, "<img=22> N/A <img=23>", gameFonts, RSComponent.SMALL, 0xFFA861, true, true, 100, 16, -1);

        rsComponent.addText(25053, "", gameFonts, RSComponent.SMALL, 0, false, false, 263, 16, -1).popupString = "Current item price range (Minimum/Maximum Price)";

        rsComponent.addHoverButton(25054, ImageLoader.forName("GE_SEARCH_BOX"), 40, 36, "Choose Item", 0, 25055, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent.addHoveredButton(25055, ImageLoader.forName("GE_SEARCH_BOX_OPACITY"), 40, 36, 25056); // send hide via server
        // todo should appear after item selection

        rsComponent.addText(25057, "<img=21>N/A", gameFonts, RSComponent.SMALL, 0x999900, true, true, 100, 16, -1);
        rsComponent.addText(25058, "", gameFonts, RSComponent.SMALL, 0, false, false, 132, 15, -1).popupString = "Current item Market Price";

        addButton(rsComponent, 25466, null, -1, -1, false, null, "GE_OFFER_ITEM", "GE_OFFER_ITEM_HOVER", "Choose Item", null, 40, 36);//
        rsComponent.addText(25469, "", gameFonts, RSComponent.SMALL, 0, false, false, 40, 36, -1).popupString = "Use your inventory to select an item to sell here";
        rsComponent.addToItemGroup(25060, 41, 36, 0, 0, true, false, false, false);
        rsComponent.addText(25061, "Quantity:", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 100, 16, -1);
        rsComponent.addText(25062, "0", gameFonts, RSComponent.BOLD, 0xFFA861, true, true, 100, 16, -1);

        RSComponent rsComponent1 = rsComponent.addHoverButton(25063, ImageLoader.forName("GE_DECREASE"), 13, 13, "Decrease Quantity", 0, 25064, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.popupString = "Decrease quantity by 1";
        rsComponent.addHoveredButton(25064, ImageLoader.forName("GE_DECREASE_HOVER"), 13, 13, 25065);
        rsComponent1 = rsComponent.addHoverButton(25066, ImageLoader.forName("GE_INCREASE"), 13, 13, "Increase Quantity", 0, 25067, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.popupString = "Increase quantity by 1";
        rsComponent.addHoveredButton(25067, ImageLoader.forName("GE_INCREASE_HOVER"), 13, 13, 25068);

        addButton(rsComponent, 25068, "+1", 0xCC9800, RSComponent.SMALL, true, gameFonts, "GE_STATUS_BUTTON", "GE_STATUS_BUTTON_HOVER", "Add 1", "Add 1 to quantity", 35, 25);
        addButton(rsComponent, 25072, "+10", 0xFF9800, RSComponent.SMALL, true, gameFonts, "GE_STATUS_BUTTON", "GE_STATUS_BUTTON_HOVER", "Add 10", "Add 10 to quantity", 35, 25);
        addButton(rsComponent, 25076, "+100", 0xFF9800, RSComponent.SMALL, true, gameFonts, "GE_STATUS_BUTTON", "GE_STATUS_BUTTON_HOVER", "Add 100", "Add 100 to quantity", 35, 25);
        addButton(rsComponent, 25080, "+1k", 0xFF9800, RSComponent.SMALL, true, gameFonts, "GE_STATUS_BUTTON", "GE_STATUS_BUTTON_HOVER", "Add 1000", "Add 1,000 to quantity", 35, 25);
        addButton(rsComponent, 25084, "...", 0xFF9800, RSComponent.SMALL, true, gameFonts, "GE_STATUS_BUTTON", "GE_STATUS_BUTTON_HOVER", "Edit Quantity", "Enter custom quantity", 35, 25);

        rsComponent.addText(25088, "Price per item:", gameFonts, RSComponent.BOLD, 0xCC9900, true, true, 100, 16, -1);
        rsComponent.addText(25089, "0 gp", gameFonts, RSComponent.BOLD, 0xFFA861, true, true, 100, 16, -1);

        addButton(rsComponent, 25090, null, -1, -1, false, null, "GE_MIN_BUTTON", "GE_MIN_BUTTON_HOVER", "Offer Minimum Price", "Set price to item's Minimum Price", 35, 25);
        addButton(rsComponent, 25093, null, -1, -1, false, null, "GE_MARKET_BUTTON", "GE_MARKET_BUTTON_HOVER", "Offer Market Price", "Set price to item's Market Price", 35, 25);
        addButton(rsComponent, 25096, null, -1, -1, false, null, "GE_MAX_BUTTON", "GE_MAX_BUTTON_HOVER", "Offer Maximum Price", "Set price to item's Maximum Price", 35, 25);
        addButton(rsComponent, 25099, null, -1, -1, false, null, "GE_QUANTITY_BUTTON", "GE_QUANTITY_BUTTON_HOVER", "Edit Price", "Enter custom price", 35, 25);

        rsComponent1 = rsComponent.addHoverButton(25102, ImageLoader.forName("GE_DECREASE"), 13, 13, "Decrease Price", 0, 25103, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.popupString = "Decrease price by 1";
        rsComponent.addHoveredButton(25103, ImageLoader.forName("GE_DECREASE_HOVER"), 13, 13, 25104);
        rsComponent1 = rsComponent.addHoverButton(25105, ImageLoader.forName("GE_INCREASE"), 13, 13, "Increase Price", 0, 25106, RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.popupString = "Increase price by 1";
        rsComponent.addHoveredButton(25106, ImageLoader.forName("GE_INCREASE_HOVER"), 13, 13, 25107);

        rsComponent.addText(25108, "0 gp", gameFonts, RSComponent.BOLD, 0xEBE573, true, true, 100, 16, -1);
        //
        rsComponent1 = rsComponent.addText(25109, "", "", gameFonts, RSComponent.SMALL, 0, false, false, 148, 20, -1, 326, 1);
        rsComponent1.popupString = "Maximum total cost of of purchase";
        rsComponent1.enabledPopupString = "Minimum total value of sale";

        addButton(rsComponent, 25110, null, -1, -1, false, null, "GE_BACK_BUTTON", "GE_BACK_BUTTON_HOVER", "Back", "Back to offer summary", 30, 23);

        addButton(rsComponent, 25113, null, -1, -1, false, null, "GE_CONFIRM_BUTTON", "GE_CONFIRM_BUTTON_HOVER", "Confirm Offer", "Confirm this offer and return to the summary view", 120, 69);

        rsComponent.addChild(25043, 13, 20);
        rsComponent.addChild(25044, 473, 30);
        rsComponent.addChild(25045, 473, 30);
        rsComponent.addChild(25047, 156, 30);
        rsComponent.addChild(25048, 72, 66);
        rsComponent.addChild(25049, 178, 67);
        rsComponent.addChild(25050, 210, 66);
        rsComponent.addChild(25051, 210, 94);
        rsComponent.addChild(25052, 285, 133);
        rsComponent.addChild(25053, 202, 128);
        rsComponent.addChild(25054, 102, 92);//104, 93);
        rsComponent.addChild(25055, 102, 92);
        rsComponent.addChild(25057, 71, 133);
        rsComponent.addChild(25058, 57, 132);
        rsComponent.addChild(25466, 102, 92);
        rsComponent.addChild(25467, 102, 92);
        rsComponent.addChild(25469, 102, 92);
        rsComponent.addChild(25060, 104, 93);
        rsComponent.addChild(25061, 100, 157);
        rsComponent.addChild(25062, 100, 180);
        rsComponent.addChild(25063, 55, 181);
        rsComponent.addChild(25064, 55, 181);
        rsComponent.addChild(25066, 235, 181);
        rsComponent.addChild(25067, 235, 181);
        rsComponent.addChild(25069, 52, 205);
        rsComponent.addChild(25070, 52, 205);
        rsComponent.addChild(25068, 52, 211);
        rsComponent.addChild(25073, 93, 205);
        rsComponent.addChild(25074, 93, 205);
        rsComponent.addChild(25072, 93, 211);
        rsComponent.addChild(25077, 134, 205);
        rsComponent.addChild(25078, 134, 205);
        rsComponent.addChild(25076, 134, 211);
        rsComponent.addChild(25081, 175, 205);
        rsComponent.addChild(25082, 175, 205);
        rsComponent.addChild(25080, 175, 211);
        rsComponent.addChild(25085, 216, 205);
        rsComponent.addChild(25086, 216, 205);
        rsComponent.addChild(25084, 216, 211);
        rsComponent.addChild(25088, 313, 157);
        rsComponent.addChild(25089, 313, 180);
        rsComponent.addChild(25090, 267, 205);
        rsComponent.addChild(25091, 267, 205);
        rsComponent.addChild(25093, 320, 205);
        rsComponent.addChild(25094, 320, 205);
        rsComponent.addChild(25096, 373, 205);
        rsComponent.addChild(25097, 373, 205);
        rsComponent.addChild(25099, 426, 205);
        rsComponent.addChild(25100, 426, 205);
        rsComponent.addChild(25102, 270, 181);
        rsComponent.addChild(25103, 270, 181);
        rsComponent.addChild(25105, 443, 181);
        rsComponent.addChild(25106, 443, 181);
        rsComponent.addChild(25108, 210, 241);
        rsComponent.addChild(25109, 185, 238);
        rsComponent.addChild(25110, 28, 281);
        rsComponent.addChild(25111, 28, 281);
        rsComponent.addChild(25113, 200, 270);
        rsComponent.addChild(25114, 200, 270);

        // View offer screen addition
        rsComponent.addText(25476, "You have sold a total of <col=CC9900>0<col=C1A875> so far", gameFonts, RSComponent.SMALL, 0xC1A875, true, true, 162, 28, -1);
        rsComponent.addText(25477, "for a total price of <col=CC9900>0<col=C1A875> gp.", gameFonts, RSComponent.SMALL, 0xC1A875, true, true, 162, 28, -1);
        addButton(rsComponent, 25478, null, -1, -1, false, null, "GE_OFFER_ABORT_BUTTON", "GE_OFFER_ABORT_BUTTON_HOVER", "Abort Offer", "Abort offer", 20, 20);

        rsComponent.addSprite(25481, ImageLoader.forName("GE_COLLECT_OFFER"));

        addButton(rsComponent, 25482, null, -1, -1, false, null, "EMPTY", "GE_COLLECT_OFFER_HOVER", "Collect", null, 40, 36);
        addButton(rsComponent, 25485, null, -1, -1, false, null, "EMPTY", "GE_COLLECT_OFFER_HOVER", "Collect", null, 40, 36);
        rsComponent.addToItemGroup(25488, 1, 1, 0, 0, true, false, true, false);
        rsComponent.addToItemGroup(25489, 1, 1, 0, 0, true, false, true, false);
        rsComponent.addSprite(25490, ImageLoader.forName("GE_OFFER_STATUS_LONG"), -1, -1, false, 300, 15).popupString = "In progress";

        rsComponent.addChild(25476, 139, 268);
        rsComponent.addChild(25477, 138, 283);

        rsComponent.addChild(25478, 350, 272);
        rsComponent.addChild(25479, 350, 272);

        rsComponent.addChild(25481, 380, 262);

        rsComponent.addChild(25482, 395, 275);
        rsComponent.addChild(25483, 395, 275);

        rsComponent.addChild(25485, 444, 275);
        rsComponent.addChild(25486, 444, 275);

        rsComponent.addChild(25488, 397, 278);
        rsComponent.addChild(25489, 446, 278);

        rsComponent.addChild(25490, 70, 299);
        // end view offer screen
        return rsComponent;
    }

    private RSComponent addButton(RSComponent rsComponent, int id, String disabledMessage, int colour, int fontIndex, boolean center, GameFont[] gameFonts, String imageName, String hoverImageName, String tooltip, String popupString, int width, int height) {
        boolean hasText = disabledMessage != null;
        if (hasText) {
            rsComponent.addText(id, disabledMessage, gameFonts, fontIndex, colour, center, true, width, height, -1);
        }
        RSComponent rsComponent1 = rsComponent.addHoverButton(id + (hasText ? 1 : 0), ImageLoader.forName(imageName), width, height, tooltip, 0, id + (hasText ? 2 : 1), RSComponent.BUTTON_ACTION_TYPE);
        rsComponent1.popupString = popupString;
        rsComponent.addHoveredButton(id + (hasText ? 2 : 1), ImageLoader.forName(hoverImageName), width, height, id + +(hasText ? 3 : 2));
        return rsComponent;
    }
}
