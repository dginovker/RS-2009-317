package com.runescape.cache.def.impl;

import com.runescape.cache.def.item.ItemDefinition;

/**
 * Represents a repository for an {@link ItemDefinition}.
 *
 * @author <a href="http://Gielinor.org">Gielinor</a>
 */
public class ItemRepository {

    /**
     * Gets an item by id.
     *
     * @param itemId The item id.
     * @return The {@link ItemDefinition}.
     */
    public static ItemDefinition forId(int itemId, ItemDefinition itemDefinition) {
        switch (itemId) {
            case 14762:
                itemDefinition.name = "Toxic blowpipe";
                itemDefinition.modelZoom = 1158;
                itemDefinition.modelRotationY = 768;
                itemDefinition.modelRotationX = 189;
                itemDefinition.modelPositionX = -7;
                itemDefinition.modelPositionY = 4;
                itemDefinition.options = new String[]{null, "Wield", "Check", "Unload", "Uncharge"};
                itemDefinition.interfaceModelId = 19219;
                itemDefinition.maleModelId = 14403;
                itemDefinition.femaleModelId = 14403;
                itemDefinition.osrs = true;
                break;

            case 14761:
                itemDefinition.name = "Oddskull";
                itemDefinition.interfaceModelId = 45726;
                itemDefinition.modelZoom = 716;
                itemDefinition.modelRotationY = 148;
                itemDefinition.maleModelId = 45725;
                itemDefinition.maleModelId = 45725;
                return itemDefinition;

            case 14753:
                itemDefinition.name = "Super combat potion(4)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{21396};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2789;
                itemDefinition.lightIntensity = 15;
                itemDefinition.lightMagnitude = 75;
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = false;
                return itemDefinition;

            case 14754:
                itemDefinition.name = "Super combat potion(4)";
                itemDefinition.interfaceModelId = 2789;
                itemDefinition = convertNote(itemDefinition, 14753);
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = true;
                return itemDefinition;

            case 14755:
                itemDefinition.name = "Super combat potion(3)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{21396};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2697;
                itemDefinition.lightIntensity = 15;
                itemDefinition.lightMagnitude = 75;
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = false;
                return itemDefinition;

            case 14756:
                itemDefinition.name = "Super combat potion(3)";
                itemDefinition.interfaceModelId = 2697;
                itemDefinition = convertNote(itemDefinition, 14755);
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = true;
                return itemDefinition;

            case 14757:
                itemDefinition.name = "Super combat potion(2)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{21396};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2384;
                itemDefinition.lightIntensity = 15;
                itemDefinition.lightMagnitude = 75;
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = false;
                return itemDefinition;

            case 14758:
                itemDefinition.name = "Super combat potion(2)";
                itemDefinition.interfaceModelId = 2384;
                itemDefinition = convertNote(itemDefinition, 14757);
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = true;
                return itemDefinition;

            case 14759:
                itemDefinition.name = "Super combat potion(1)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{21396};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2621;
                itemDefinition.lightIntensity = 15;
                itemDefinition.lightMagnitude = 75;
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = false;
                return itemDefinition;

            case 14760:
                itemDefinition.name = "Super combat potion(1)";
                itemDefinition.interfaceModelId = 2621;
                itemDefinition = convertNote(itemDefinition, 14759);
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = true;
                return itemDefinition;

            case 11951:
                itemDefinition.options = new String[]{null, "Wield", "Build", null, "Drop"};
                return itemDefinition;

            case 14499:
            case 14497:
            case 14501:
            case 14525:
            case 14494:
            case 14492:
            case 14490:
            case 14527:
            case 14479:
            case 14529:
            case 14531:
                itemDefinition.tradable = true;
                itemDefinition.blacklisted = false;
                return itemDefinition;

            case 4178:
                itemDefinition.tradable = false;
                return itemDefinition;

            case 11953:
                ItemDefinition.copy(2403, itemDefinition);
                itemDefinition.name = "Varrock Palace Deed";
                itemDefinition.options = new String[]{null, null, null, null, "Destroy"};
                return itemDefinition;

            case 11954:
                ItemDefinition.copy(7684, itemDefinition);
                itemDefinition.name = "TzHaar Dart";
                itemDefinition.options = new String[]{null, null, null, null, "Destroy"};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14708:
                ItemDefinition.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Ahrim)";
                itemDefinition.modifiedColors = new int[]{82};
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14709:
                ItemDefinition.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Dharok)";
                itemDefinition.modifiedColors = new int[]{82};
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14710:
                ItemDefinition.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Guthan)";
                itemDefinition.modifiedColors = new int[]{82};
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14711:
                ItemDefinition.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Karil)";
                itemDefinition.modifiedColors = new int[]{82};
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14712:
                ItemDefinition.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Torag)";
                itemDefinition.modifiedColors = new int[]{82};
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14713:
                ItemDefinition.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Verac)";
                itemDefinition.modifiedColors = new int[]{82};
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14714:
                ItemDefinition.copy(4836, itemDefinition);
                itemDefinition.name = "Strange potion";
                itemDefinition.originalColors = new int[]{51136};
                itemDefinition.tradable = false;
                return itemDefinition;

            case 14721:
                ItemDefinition.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(1)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{5679};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2621;
                itemDefinition.tradable = true;
                break;

            case 14722:
                itemDefinition.name = "Stamina potion(1)";
                itemDefinition.interfaceModelId = 2621;
                itemDefinition = convertNote(itemDefinition, 14721);
                itemDefinition.tradable = true;
                return itemDefinition;

            case 14723:
                ItemDefinition.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(2)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{5679};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2384;
                itemDefinition.tradable = true;
                break;

            case 14724:
                itemDefinition.name = "Stamina potion(2)";
                itemDefinition.interfaceModelId = 2384;
                itemDefinition = convertNote(itemDefinition, 14723);
                itemDefinition.tradable = true;
                return itemDefinition;

            case 14725:
                ItemDefinition.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(3)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{5679};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2697;
                itemDefinition.tradable = true;
                return itemDefinition;

            case 14726:
                itemDefinition.name = "Stamina potion(3)";
                itemDefinition.interfaceModelId = 2697;
                itemDefinition = convertNote(itemDefinition, 14725);
                itemDefinition.tradable = true;
                return itemDefinition;

            case 14727:
                ItemDefinition.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(4)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{5679};
                itemDefinition.modifiedColors = new int[]{61};
                itemDefinition.options = new String[]{"Drink", null, null, "Empty", "Drop"};
                itemDefinition.interfaceModelId = 2789;
                itemDefinition.tradable = true;
                return itemDefinition;

            case 14728:
                itemDefinition.name = "Stamina potion(4)";
                itemDefinition.interfaceModelId = 2789;
                itemDefinition = convertNote(itemDefinition, 14727);
                itemDefinition.tradable = true;
                return itemDefinition;

            case 14752:
                ItemDefinition.copy(3710, itemDefinition);
                itemDefinition.name = "Referral ticket";
                itemDefinition.options = new String[]{"Referrals", "Refer player", "Requests", null, "Destroy"};
                itemDefinition.value = 1;
                return itemDefinition;

            case 27668:
                itemDefinition.name = "Ban Hammer";
                itemDefinition.options = new String[]{null, "Wield", "Remote Control", "Dox Players", "Destroy"};
                return itemDefinition;

        }
        return itemDefinition;
    }

    /**
     * Converts an item to a note.
     *
     * @param itemDefinition The definition of the item to convert.
     * @param unnotedId      The id of the unnoted item.
     * @return The noted item definition.
     */
    static ItemDefinition convertNote(ItemDefinition itemDefinition, int unnotedId) {
        itemDefinition.modelZoom = 760;
        itemDefinition.modelRotationY = 552;
        itemDefinition.modelRotationX = 28;
        itemDefinition.modelPositionY = 2;
        itemDefinition.options = new String[]{null, null, null, null, "Drop"};
        itemDefinition.interfaceModelId = 2429;
        itemDefinition.noteItemId = unnotedId;
        itemDefinition.noteTemplateId = 799;
        itemDefinition.stackingType = true;
        return itemDefinition;
    }
}
