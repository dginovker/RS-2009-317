package com.runescape.cache.media.inter.impl;

import com.runescape.Constants;
import com.runescape.cache.media.ImageLoader;
import com.runescape.cache.media.RSComponent;
import com.runescape.cache.media.SpriteRepository;
import com.runescape.cache.media.SpriteSet;
import com.runescape.cache.media.inter.InterfaceConfiguration;
import com.runescape.cache.media.inter.InterfacePlugin;
import com.runescape.media.font.GameFont;

/**
 * Represents the bank {@link com.runescape.cache.media.inter.InterfacePlugin}.
 *
 * @author Gielinor
 */
public class BankInterfacePlugin implements InterfacePlugin {
    /**
     * The main banking interface id.
     */
    public static final int BANK_INTERFACE_ID = 32842;

    public RSComponent loadInterface2(GameFont[] rsFonts) {
        // RSComponent.forId(5064).actions = new String[]{"Deposit-1", "Deposit-5", "Deposit-10", null, "Deposit-X", "Deposit-All"};
        RSComponent rsComponent = new RSComponent().addInterface(5292);
        SpriteSet functionSet = new SpriteSet(ImageLoader.forName("BANK_FUNCTION_ON"), ImageLoader.forName("BANK_FUNCTION_OFF"));
        SpriteSet buttonSet = new SpriteSet(RSComponent.getImage(9, RSComponent.cacheArchive, "miscgraphics"), RSComponent.getImage(0, RSComponent.cacheArchive, "miscgraphics"));
        rsComponent.addSprite(BANK_INTERFACE_ID, ImageLoader.forName("BANK_BACK"));

        rsComponent.addHoverButton(BANK_INTERFACE_ID + 1, SpriteRepository.BIG_X, 21, 21, "Close", 0, BANK_INTERFACE_ID + 2, RSComponent.CLOSE_ACTION_TYPE);
        rsComponent.addHoveredButton(BANK_INTERFACE_ID + 2, SpriteRepository.BIG_X_HOVER, 21, 21, BANK_INTERFACE_ID + 3);

        // rsComponent.addContainer(5382, 109, 8, 44, "Withdraw-1", "Withdraw-5", "Withdraw-10", null, "Withdraw-X", "Withdraw-All", "Withdraw-All-but-1");
        rsComponent.addContainer(5382, 109, 8, 44, "Withdraw-1", "Withdraw-5", "Withdraw-10", "Withdraw-All", "Withdraw-X", null, "Withdraw-All-but-1");

        RSComponent rsComponent1 = rsComponent.addConfigButton(BANK_INTERFACE_ID + 4, buttonSet, 36, 36, null, 1, 1012, BANK_INTERFACE_ID + 4, RSComponent.BUTTON_ACTION_TYPE);
        // rsComponent1.contentType = 555;
        rsComponent1.tooltip = "Search";// TODO "Search bank"?
        rsComponent.addButton(BANK_INTERFACE_ID + 5, buttonSet, 36, 36, "Deposit Inventory", 1); // TODO Should turn red when clicked?
        rsComponent.addButton(BANK_INTERFACE_ID + 6, buttonSet, 36, 36, "Deposit worn equipment", 1);
        rsComponent.addButton(BANK_INTERFACE_ID + 7, new SpriteSet(RSComponent.getImage(0, RSComponent.cacheArchive, "miscgraphics3"),
                RSComponent.getImage(0, RSComponent.cacheArchive, "miscgraphics3")), 25, 25, "Show menu", 1);


        rsComponent.addSprite(BANK_INTERFACE_ID + 8, ImageLoader.forName("BANK_SEARCH"));
        rsComponent.addSprite(BANK_INTERFACE_ID + 9, ImageLoader.forName("BANK_DEPOSIT_INVENTORY"));
        rsComponent.addSprite(BANK_INTERFACE_ID + 10, ImageLoader.forName("BANK_DEPOSIT_EQUIPMENT"));
        rsComponent.addSprite(BANK_INTERFACE_ID + 11, ImageLoader.forName("BANK_SETTINGS"));
        rsComponent.addSprite(BANK_INTERFACE_ID + 12, ImageLoader.forName("BANK_PRIMARY_TAB"));

        rsComponent.addText(BANK_INTERFACE_ID + 53, "%1", rsFonts, 0, 0xFE9624, true, true, 0, 14, -1).scripts = new int[][]{{22, 5382, 0}}; // TODO WIDTH

        RSComponent line = rsComponent.addInterface(BANK_INTERFACE_ID + 54);
        line.type = 3;
        line.filled = true;
        line.width = 14;
        line.height = 1;
        line.textColor = 0xFE9624;
        rsComponent.addText(BANK_INTERFACE_ID + 55, "350", rsFonts, 0, 0xFE9624, true, true, 0, 14, -1); // TODO WIDTH

        rsComponent.addChild(BANK_INTERFACE_ID, 12, 2);
        rsComponent.addChild(BANK_INTERFACE_ID + 1, 472, 9);
        rsComponent.addChild(BANK_INTERFACE_ID + 2, 472, 9);

        rsComponent.addChild(BANK_INTERFACE_ID + 53, 30, 8);
        rsComponent.addChild(BANK_INTERFACE_ID + 54, 24, 19);
        rsComponent.addChild(BANK_INTERFACE_ID + 55, 30, 20);

        for (int tabComponent = 0; tabComponent < 40; tabComponent += 4) {
            rsComponent.addButton(BANK_INTERFACE_ID + 13 + tabComponent, ImageLoader.forName("BANK_TAB_2"), 39, 40,
                    (tabComponent == 0 ? null : "Collapse tab <col=FF9040>" + (tabComponent / 4)), 1);
            int[] array = {22, (tabComponent / 4), 0};
            if (tabComponent / 4 == 0) {
                array = new int[]{5, InterfaceConfiguration.CURRENT_BANK_TAB.getId(), 0};
            }
            rsComponent1 = rsComponent.addHoverConfigButton(BANK_INTERFACE_ID + 14 + tabComponent, BANK_INTERFACE_ID + 15 + tabComponent,
                    new SpriteSet(ImageLoader.forName("BANK_TAB_2"), ImageLoader.forName("BANK_TAB_EMPTY")), 39, 40, tabComponent == 0 ? "View all items" : "New tab", new int[]{1, tabComponent / 4 == 0 ? 1 : 3},
                    new int[]{(tabComponent / 4), 0}, new int[][]{{5, InterfaceConfiguration.CURRENT_BANK_TAB.getId(), 0}, array});
            rsComponent1.automaticConfig = false;
            rsComponent1.interfaceConfig = true;
            rsComponent.addHoveredConfigButton(RSComponent.forId(BANK_INTERFACE_ID + 14 + tabComponent),
                    BANK_INTERFACE_ID + 15 + tabComponent, BANK_INTERFACE_ID + 16 + tabComponent, new SpriteSet(ImageLoader.forName("BANK_TAB_2_ON"), ImageLoader.forName("BANK_TAB_2")));
            RSComponent.forId(BANK_INTERFACE_ID + 14 + tabComponent).parentId = BANK_INTERFACE_ID;
            RSComponent.forId(BANK_INTERFACE_ID + 15 + tabComponent).parentId = BANK_INTERFACE_ID;
            RSComponent.forId(BANK_INTERFACE_ID + 16 + tabComponent).parentId = BANK_INTERFACE_ID;
            rsComponent.addChild(BANK_INTERFACE_ID + 13 + tabComponent, 57 + 40 * (tabComponent / 4), 37);
            rsComponent.addChild(BANK_INTERFACE_ID + 14 + tabComponent, 57 + 40 * (tabComponent / 4), 37);
            rsComponent.addChild(BANK_INTERFACE_ID + 15 + tabComponent, 57 + 40 * (tabComponent / 4), 37);
        }

        RSComponent.forId(5385).width += 20;
        RSComponent.forId(5385).height -= 18;
        RSComponent.forId(5382).contentType = 206;

        int[] interfaces = new int[]{5386, 5387, 8130, 8131};

        for (int id : interfaces) {
            RSComponent.forId(id).setSpriteSet(functionSet);
            RSComponent.forId(id).width = functionSet.getEnabled().myWidth;
            RSComponent.forId(id).height = functionSet.getEnabled().myHeight;
        }

        rsComponent.addChild(BANK_INTERFACE_ID + 12, 59, 41);

        rsComponent.addChild(5383, 180, 12);
        rsComponent.addChild(5385, 31, 77);

        rsComponent.addChild(8131, 102, 306);
        rsComponent.addChild(8130, 17, 306);

        rsComponent.addChild(5386, 282, 306);
        rsComponent.addChild(5387, 197, 306);
        rsComponent.addChild(8132, 127, 309);
        rsComponent.addChild(8133, 45, 309);
        rsComponent.addChild(5390, 54, 291);

        rsComponent.addChild(5389, 227, 309);
        rsComponent.addChild(5391, 311, 309);
        rsComponent.addChild(5388, 248, 291);
        rsComponent.addChild(BANK_INTERFACE_ID + 4, 376, 291);
        rsComponent.addChild(BANK_INTERFACE_ID + 5, 417, 291);
        rsComponent.addChild(BANK_INTERFACE_ID + 6, 458, 291);
        rsComponent.addChild(BANK_INTERFACE_ID + 7, 463, 44);
        rsComponent.addChild(BANK_INTERFACE_ID + 8, 379, 298);
        rsComponent.addChild(BANK_INTERFACE_ID + 9, 420, 298);
        rsComponent.addChild(BANK_INTERFACE_ID + 10, 461, 298);
        rsComponent.addChild(BANK_INTERFACE_ID + 11, 463, 44);
        rsComponent = RSComponent.forId(5382);
        rsComponent.height = 120;
        rsComponent.inventory = new int[rsComponent.width * rsComponent.height];
        rsComponent.inventoryValue = new int[rsComponent.width * rsComponent.height];
        RSComponent.forId(5385).scrollMax = 4530;
        RSComponent.forId(5383).centerText = true;
        return rsComponent;
    }

    @Override
    public RSComponent loadInterface(GameFont[] gameFonts) {
        return Constants.BANK_TABS ? loadInterface2(gameFonts) : fixInterface(gameFonts);
    }

    public RSComponent fixInterface(GameFont[] gameFonts) {
        RSComponent rsComponent = RSComponent.forId(5382);
        rsComponent.height = 119;
        rsComponent.inventory = new int[rsComponent.width * rsComponent.height];
        rsComponent.inventoryValue = new int[rsComponent.width * rsComponent.height];
        RSComponent.forId(5385).scrollMax = 4530;
        RSComponent.forId(5383).centerText = true;
        rsComponent = RSComponent.forId(5292);
        rsComponent.addButton(32847, ImageLoader.forName("BANK_DEPOSIT_INVENTORY_HOVER"), 36, 36, "Deposit inventory", 1); // TODO Should turn red when clicked?
        rsComponent.addButton(32848, ImageLoader.forName("BANK_DEPOSIT_EQUIPMENT_HOVER"), 36, 36, "Deposit worn equipment", 1);
        rsComponent.addChild(32847, 220, 294);
        rsComponent.addChild(32848, 258, 294);
        rsComponent.addText(BANK_INTERFACE_ID + 53, "%1", gameFonts, 0, 0xFE9624, true, true, 0, 14, -1).scripts = new int[][]{{22, 5382, 0}}; // TODO WIDTH
        RSComponent line = rsComponent.addInterface(BANK_INTERFACE_ID + 54);
        line.type = 3;
        line.filled = true;
        line.width = 14;
        line.height = 1;
        line.textColor = 0xFE9624;
        rsComponent.addText(BANK_INTERFACE_ID + 55, "350", gameFonts, 0, 0xFE9624, true, true, 0, 14, -1); // TODO WIDTH
        rsComponent.addChild(BANK_INTERFACE_ID + 53, 30, 8 + 18);
        rsComponent.addChild(BANK_INTERFACE_ID + 54, 24, 19 + 18);
        rsComponent.addChild(BANK_INTERFACE_ID + 55, 30, 20 + 18);
        return rsComponent;
    }
//    public RSComponent fixInterface(GameFont[] gameFonts) {
//        SpriteSet functionSet = new SpriteSet(ImageLoader.forName("BANK_FUNCTION_ON"), ImageLoader.forName("BANK_FUNCTION_OFF"));
//        SpriteSet buttonSet = new SpriteSet(RSComponent.getImage(9, RSComponent.cacheArchive, "miscgraphics"), RSComponent.getImage(0, RSComponent.cacheArchive, "miscgraphics"));
//
//        RSComponent rsComponent = RSComponent.forId(5382);
//        rsComponent.height = 119;
//        rsComponent.inventory = new int[rsComponent.width * rsComponent.height];
//        rsComponent.inventoryValue = new int[rsComponent.width * rsComponent.height];
//        RSComponent.forId(5385).scrollMax = 4530;
//        RSComponent.forId(5383).centerText = true;
//        rsComponent = RSComponent.forId(5292);
////        rsComponent.addButton(32847, ImageLoader.forName("BANK_DEPOSIT_INVENTORY_HOVER"), 36, 36, "Deposit inventory", 1); // TODO Should turn red when clicked?
////        rsComponent.addButton(32848, ImageLoader.forName("BANK_DEPOSIT_EQUIPMENT_HOVER"), 36, 36, "Deposit worn equipment", 1);
////        //rsComponent.addButton(32846, ImageLoader.forName("BANK_SEARCH_HOVER"), 36, 36, "Search bank", 1); // TODO Should turn red when clicked?
////        rsComponent.addChild(32847, 220, 294);
////        rsComponent.addChild(32848, 258, 294);
////        // rsComponent.addChild(32846, 458, 294);
//        rsComponent.addText(BANK_INTERFACE_ID + 53, "%1", gameFonts, 0, 0xFE9624, true, true, 0, 14, -1).scripts = new int[][]{{22, 5382, 0}}; // TODO WIDTH
//
//        RSComponent line = rsComponent.addInterface(BANK_INTERFACE_ID + 54);
//        line.type = 3;
//        line.filled = true;
//        line.width = 14;
//        line.height = 1;
//        line.textColor = 0xFE9624;
//        rsComponent.addText(BANK_INTERFACE_ID + 55, "350", gameFonts, 0, 0xFE9624, true, true, 0, 14, -1); // TODO WIDTH
//        rsComponent.addChild(BANK_INTERFACE_ID + 53, 30, 8 + 18);
//        rsComponent.addChild(BANK_INTERFACE_ID + 54, 24, 19 + 18);
//        rsComponent.addChild(BANK_INTERFACE_ID + 55, 30, 20 + 18);
//        int[] interfaces = new int[]{5386, 5387, 8130, 8131};
//
//        for (int id : interfaces) {
//            RSComponent.forId(id).setSpriteSet(functionSet);
//            RSComponent.forId(id).width = functionSet.getEnabled().myWidth;
//            RSComponent.forId(id).height = functionSet.getEnabled().myHeight;
//        }
//        rsComponent.repositionChild(8131, 102, 306 - 8);
//        rsComponent.repositionChild(8130, 17, 306 - 8);
//        rsComponent.repositionChild(5386, 282, 306 - 8);
//        rsComponent.repositionChild(5387, 197, 306 - 8);
//
//        rsComponent.repositionChild(8132, 122, 309 - 8); // Insert
//        rsComponent.repositionChild(8133, 39, 309 - 8); // Swap
//        rsComponent.repositionChild(5390, 54, 291 - 8); // Rearrange mode
//
//        rsComponent.repositionChild(5389, 227, 309 - 8); // Item
//        rsComponent.repositionChild(5391, 311, 309 - 8); // Note
//        rsComponent.repositionChild(5388, 248, 291 - 8); // Withdraw as
//        return rsComponent;
//    }

}
