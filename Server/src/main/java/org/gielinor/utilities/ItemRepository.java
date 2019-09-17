package org.gielinor.utilities;

/**
 * Represents a repository for an {@link org.gielinor.utilities.ItemDefinitionC}.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemRepository {

    public static int[][] replacementIds = {
        { 22342, 14651 }, { 22343, 14652 }, { 22344, 14653 }, { 22345, 14654 }, { 22346, 14655 }, { 22347, 14656 },
        { 22348, 14657 }, { 22349, 14658 }, { 22350, 14659 }, { 22351, 14660 }, { 22352, 14661 }, { 22353, 14662 },
        { 22354, 14663 }, { 22355, 14664 }, { 22356, 14665 }, { 22357, 14666 }, { 22358, 14667 }, { 22359, 14668 },
        { 22360, 14669 }, { 22361, 14670 }, { 22362, 14671 }, { 22363, 14672 }, { 22364, 14673 }, { 22365, 14674 },
        { 22366, 14675 }, { 22367, 14676 }, { 22368, 14677 }, { 22369, 14678 }, { 22370, 14679 }, { 22371, 14680 },
        { 22372, 14681 }, { 22373, 14682 }, { 22374, 14683 }, { 22375, 14684 }, { 22376, 14685 }, { 22377, 14686 },
        { 22378, 14687 }, { 22379, 14688 }, { 22380, 14689 }, { 22381, 14690 }, { 22382, 14691 }, { 22383, 14692 },
        { 22384, 14693 }, { 22385, 14694 }, { 22386, 14695 }, { 22387, 14696 }, { 22388, 14697 }, { 22389, 14698 },
        { 22390, 14699 }, { 22391, 14700 }, { 22392, 14701 }, { 22393, 14702 }, { 22394, 14703 }, { 22395, 14704 },
        { 22396, 14705 }, { 22397, 14706 }, { 22398, 14707 }, { 22399, 14708 }, { 22400, 14709 }, { 22401, 14710 },
        { 22402, 14711 }, { 22403, 14712 }, { 22404, 14713 }, { 22405, 14714 }, { 22406, 14715 }, { 22407, 14716 },
        { 22408, 14717 }, { 22409, 14718 }, { 22410, 14719 }, { 22411, 14720 }
    };

    /**
     * Gets an item by id.
     *
     * @param itemId The item id.
     * @return The {@link org.gielinor.utilities.ItemDefinitionC}.
     */
    public static ItemDefinitionC forId(int itemId, ItemDefinitionC itemDefinition) {
        if (true) {
            switch (itemId) {
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
                    itemDefinition.originalColors = new int[]{ 21396 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2789;
                    itemDefinition.lightIntensity = 15;
                    itemDefinition.lightMagnitude = 75;
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = false;
                    return itemDefinition;

                case 14754:
                    itemDefinition.name = "Super combat potion(4)";
                    itemDefinition.interfaceModelId = 2789;
                    itemDefinition = convertNote(itemDefinition, 14753);
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = true;
                    return itemDefinition;

                case 14755:
                    itemDefinition.name = "Super combat potion(3)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 21396 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2697;
                    itemDefinition.lightIntensity = 15;
                    itemDefinition.lightMagnitude = 75;
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = false;
                    return itemDefinition;

                case 14756:
                    itemDefinition.name = "Super combat potion(3)";
                    itemDefinition.interfaceModelId = 2697;
                    itemDefinition = convertNote(itemDefinition, 14755);
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = true;
                    return itemDefinition;

                case 14757:
                    itemDefinition.name = "Super combat potion(2)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 21396 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2384;
                    itemDefinition.lightIntensity = 15;
                    itemDefinition.lightMagnitude = 75;
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = false;
                    return itemDefinition;

                case 14758:
                    itemDefinition.name = "Super combat potion(2)";
                    itemDefinition.interfaceModelId = 2384;
                    itemDefinition = convertNote(itemDefinition, 14757);
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = true;
                    return itemDefinition;

                case 14759:
                    itemDefinition.name = "Super combat potion(1)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 21396 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2621;
                    itemDefinition.lightIntensity = 15;
                    itemDefinition.lightMagnitude = 75;
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = false;
                    return itemDefinition;

                case 14760:
                    itemDefinition.name = "Super combat potion(1)";
                    itemDefinition.interfaceModelId = 2621;
                    itemDefinition = convertNote(itemDefinition, 14759);
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = true;
                    return itemDefinition;

                case 11951:
                    itemDefinition.options = new String[]{ null, "Wield", "Build", null, "Drop" };
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
                    itemDefinition.trade = true;
                    itemDefinition.blacklisted = false;
                    return itemDefinition;

                case 4178:
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 11953:
                    ItemDefinitionC.copy(2403, itemDefinition);
                    itemDefinition.name = "Varrock Palace Deed";
                    itemDefinition.options = new String[]{ null, null, null, null, "Destroy" };
                    return itemDefinition;

                case 11954:
                    ItemDefinitionC.copy(7684, itemDefinition);
                    itemDefinition.name = "TzHaar Dart";
                    itemDefinition.options = new String[]{ null, null, null, null, "Destroy" };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14708:
                    ItemDefinitionC.copy(592, itemDefinition);
                    itemDefinition.name = "Ashes (Ahrim)";
                    itemDefinition.modifiedColors = new int[]{ 82 };
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14709:
                    ItemDefinitionC.copy(592, itemDefinition);
                    itemDefinition.name = "Ashes (Dharok)";
                    itemDefinition.modifiedColors = new int[]{ 82 };
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14710:
                    ItemDefinitionC.copy(592, itemDefinition);
                    itemDefinition.name = "Ashes (Guthan)";
                    itemDefinition.modifiedColors = new int[]{ 82 };
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14711:
                    ItemDefinitionC.copy(592, itemDefinition);
                    itemDefinition.name = "Ashes (Karil)";
                    itemDefinition.modifiedColors = new int[]{ 82 };
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14712:
                    ItemDefinitionC.copy(592, itemDefinition);
                    itemDefinition.name = "Ashes (Torag)";
                    itemDefinition.modifiedColors = new int[]{ 82 };
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14713:
                    ItemDefinitionC.copy(592, itemDefinition);
                    itemDefinition.name = "Ashes (Verac)";
                    itemDefinition.modifiedColors = new int[]{ 82 };
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14714:
                    ItemDefinitionC.copy(4836, itemDefinition);
                    itemDefinition.name = "Strange potion";
                    itemDefinition.originalColors = new int[]{ 51136 };
                    itemDefinition.trade = false;
                    return itemDefinition;

                case 14721:
                    ItemDefinitionC.copy(2436, itemDefinition);
                    itemDefinition.name = "Stamina potion(1)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 5679 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2621;
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14722:
                    itemDefinition.name = "Stamina potion(1)";
                    itemDefinition.interfaceModelId = 2621;
                    itemDefinition = convertNote(itemDefinition, 14721);
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14723:
                    ItemDefinitionC.copy(2436, itemDefinition);
                    itemDefinition.name = "Stamina potion(2)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 5679 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2384;
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14724:
                    itemDefinition.name = "Stamina potion(2)";
                    itemDefinition.interfaceModelId = 2384;
                    itemDefinition = convertNote(itemDefinition, 14723);
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14725:
                    ItemDefinitionC.copy(2436, itemDefinition);
                    itemDefinition.name = "Stamina potion(3)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 5679 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2697;
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14726:
                    itemDefinition.name = "Stamina potion(3)";
                    itemDefinition.interfaceModelId = 2697;
                    itemDefinition = convertNote(itemDefinition, 14725);
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14727:
                    ItemDefinitionC.copy(2436, itemDefinition);
                    itemDefinition.name = "Stamina potion(4)";
                    itemDefinition.modelZoom = 550;
                    itemDefinition.modelRotationY = 84;
                    itemDefinition.modelRotationX = 1996;
                    itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                    itemDefinition.originalColors = new int[]{ 5679 };
                    itemDefinition.modifiedColors = new int[]{ 61 };
                    itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                    itemDefinition.interfaceModelId = 2789;
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14728:
                    itemDefinition.name = "Stamina potion(4)";
                    itemDefinition.interfaceModelId = 2789;
                    itemDefinition = convertNote(itemDefinition, 14727);
                    itemDefinition.trade = true;
                    return itemDefinition;

                case 14752:
                    ItemDefinitionC.copy(3710, itemDefinition);
                    itemDefinition.name = "Referral ticket";
                    itemDefinition.options = new String[]{ "Referrals", "Refer player", "Requests", null, "Destroy" };
                    itemDefinition.value = 1;
                    return itemDefinition;
            }
            return null;
        }
        // New ID: 14651

        switch (itemId) {
            case 10942:
                itemDefinition.name = "XP Token(All)";
                itemDefinition.options = new String[]{ "Claim", "Check", null, null, "Destroy" };
                itemDefinition.value = 1;
                itemDefinition.trade = false;
                return itemDefinition;

            case 10943:
                itemDefinition.name = "XP Token(Combat)";
                itemDefinition.options = new String[]{ "Claim", "Check", null, null, "Destroy" };
                itemDefinition.value = 1;
                itemDefinition.trade = false;
                return itemDefinition;

            case 10944:
                itemDefinition.name = "XP Token(Skills)";
                itemDefinition.options = new String[]{ "Claim", "Check", null, null, "Destroy" };
                itemDefinition.value = 1;
                itemDefinition.trade = false;
                return itemDefinition;

            case 11954:
                ItemDefinitionC.copy(7684, itemDefinition);
                itemDefinition.name = "TzHaar Dart";
                itemDefinition.options = new String[]{ null, null, null, null, "Destroy" };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14697:
                itemDefinition.name = "Zamorakian hasta";
                itemDefinition.interfaceModelId = 45483;
                itemDefinition = convertNote(itemDefinition, 14696);
                itemDefinition.trade = true;
                return itemDefinition;

            case 14708:
                ItemDefinitionC.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Ahrim)";
                itemDefinition.modifiedColors = new int[]{ 82 };
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14709:
                ItemDefinitionC.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Dharok)";
                itemDefinition.modifiedColors = new int[]{ 82 };
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14710:
                ItemDefinitionC.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Guthan)";
                itemDefinition.modifiedColors = new int[]{ 82 };
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14711:
                ItemDefinitionC.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Karil)";
                itemDefinition.modifiedColors = new int[]{ 82 };
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14712:
                ItemDefinitionC.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Torag)";
                itemDefinition.modifiedColors = new int[]{ 82 };
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14713:
                ItemDefinitionC.copy(592, itemDefinition);
                itemDefinition.name = "Ashes (Verac)";
                itemDefinition.modifiedColors = new int[]{ 82 };
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14714:
                ItemDefinitionC.copy(4836, itemDefinition);
                itemDefinition.name = "Strange potion";
                itemDefinition.originalColors = new int[]{ 51136 };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14720:
                itemDefinition.id = 14720;
                itemDefinition.interfaceModelId = 45651;
                itemDefinition.name = "TzRek-Jad";
                itemDefinition.modelZoom = 1910;
                itemDefinition.modelRotationX = 1860;
                itemDefinition.modelRotationY = 138;
                itemDefinition.modelPositionX = 5;
                itemDefinition.modelPositionY = -1;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.trade = false;
                return itemDefinition;

            case 14721:
                ItemDefinitionC.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(1)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{ 5679 };
                itemDefinition.modifiedColors = new int[]{ 61 };
                itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                itemDefinition.interfaceModelId = 2621;
                itemDefinition.trade = true;
                return itemDefinition;

            case 14722:
                itemDefinition.name = "Stamina potion(1)";
                itemDefinition.interfaceModelId = 2621;
                itemDefinition = convertNote(itemDefinition, 14721);
                itemDefinition.trade = true;
                return itemDefinition;

            case 14723:
                ItemDefinitionC.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(2)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{ 5679 };
                itemDefinition.modifiedColors = new int[]{ 61 };
                itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                itemDefinition.interfaceModelId = 2384;
                itemDefinition.trade = true;
                return itemDefinition;

            case 14724:
                itemDefinition.name = "Stamina potion(2)";
                itemDefinition.interfaceModelId = 2384;
                itemDefinition = convertNote(itemDefinition, 14723);
                itemDefinition.trade = true;
                return itemDefinition;

            case 14725:
                ItemDefinitionC.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(3)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{ 5679 };
                itemDefinition.modifiedColors = new int[]{ 61 };
                itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                itemDefinition.interfaceModelId = 2697;
                itemDefinition.trade = true;
                return itemDefinition;

            case 14726:
                itemDefinition.name = "Stamina potion(3)";
                itemDefinition.interfaceModelId = 2697;
                itemDefinition = convertNote(itemDefinition, 14725);
                itemDefinition.trade = true;
                return itemDefinition;

            case 14727:
                ItemDefinitionC.copy(2436, itemDefinition);
                itemDefinition.name = "Stamina potion(4)";
                itemDefinition.modelZoom = 550;
                itemDefinition.modelRotationY = 84;
                itemDefinition.modelRotationX = 1996;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{ 5679 };
                itemDefinition.modifiedColors = new int[]{ 61 };
                itemDefinition.options = new String[]{ "Drink", null, null, "Empty", "Drop" };
                itemDefinition.interfaceModelId = 2789;
                itemDefinition.trade = true;
                return itemDefinition;

            case 14728:
                itemDefinition.name = "Stamina potion(4)";
                itemDefinition.interfaceModelId = 2789;
                itemDefinition = convertNote(itemDefinition, 14727);
                itemDefinition.trade = true;
                return itemDefinition;

            case 14729:
                itemDefinition.name = "Mark of grace";
                itemDefinition.modelZoom = 1600;
                itemDefinition.modelRotationY = 588;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = -5;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 2;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 45653;
                return itemDefinition;

            case 14730:
                itemDefinition.name = "Graceful hood";
                itemDefinition.modelZoom = 848;
                itemDefinition.modelRotationY = 639;
                itemDefinition.modelRotationX = 24;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 5;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45654;
                itemDefinition.maleModelId = 45655;
                itemDefinition.femaleModelId = 45656;
                itemDefinition.equipped_model_male_dialogue_1 = 45688;
                itemDefinition.equipped_model_female_dialogue_1 = 45687;
                itemDefinition.equipped_model_male_dialogue_2 = 63;
                itemDefinition.equipped_model_female_dialogue_2 = 120;
                return itemDefinition;

            case 14731:
                itemDefinition.name = "Graceful hood";
                itemDefinition.modelZoom = 848;
                itemDefinition.modelRotationY = 639;
                itemDefinition.modelRotationX = 24;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 5;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45654;
                itemDefinition.maleModelId = 45655;
                itemDefinition.femaleModelId = 45656;
                return itemDefinition;

            case 14732:
                itemDefinition.name = "Graceful cape";
                itemDefinition.modelZoom = 2320;
                itemDefinition.modelRotationY = 564;
                itemDefinition.modelRotationX = 827;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 2;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 2;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45657;
                itemDefinition.maleModelId = 45658;
                itemDefinition.femaleModelId = 45659;
                return itemDefinition;
            case 14733:
                itemDefinition.name = "Graceful cape";
                itemDefinition.modelZoom = 2320;
                itemDefinition.modelRotationY = 564;
                itemDefinition.modelRotationX = 827;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 2;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 2;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45657;
                itemDefinition.maleModelId = 45658;
                itemDefinition.femaleModelId = 45659;
                return itemDefinition;

            case 14734:
                itemDefinition.name = "Graceful top";
                itemDefinition.modelZoom = 1232;
                itemDefinition.modelRotationY = 666;
                itemDefinition.modelRotationX = 9;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45660;
                itemDefinition.maleModelId = 45661;
                itemDefinition.maleArmModelId = 45662;
                itemDefinition.femaleModelId = 45663;
                itemDefinition.femaleArmModelId = 45664;
                return itemDefinition;
            case 14735:
                itemDefinition.name = "Graceful top";
                itemDefinition.modelZoom = 1232;
                itemDefinition.modelRotationY = 666;
                itemDefinition.modelRotationX = 9;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45660;
                itemDefinition.maleModelId = 45661;
                itemDefinition.maleArmModelId = 45662;
                itemDefinition.femaleModelId = 45663;
                itemDefinition.femaleArmModelId = 45664;
                return itemDefinition;

            case 14736:
                itemDefinition.name = "Graceful legs";
                itemDefinition.modelZoom = 1744;
                itemDefinition.modelRotationY = 525;
                itemDefinition.modelRotationX = 171;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 10;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 10;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45665;
                itemDefinition.maleModelId = 45666;
                itemDefinition.femaleModelId = 45667;
                return itemDefinition;
            case 14737:
                itemDefinition.name = "Graceful legs";
                itemDefinition.modelZoom = 1744;
                itemDefinition.modelRotationY = 525;
                itemDefinition.modelRotationX = 171;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 10;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 10;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45665;
                itemDefinition.maleModelId = 45666;
                itemDefinition.femaleModelId = 45667;
                return itemDefinition;

            case 14738:
                itemDefinition.name = "Graceful gloves";
                itemDefinition.modelZoom = 592;
                itemDefinition.modelRotationY = 636;
                itemDefinition.modelRotationX = 2015;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 3;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 3;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45668;
                itemDefinition.maleModelId = 45669;
                itemDefinition.femaleModelId = 45670;
                return itemDefinition;
            case 14739:
                itemDefinition.name = "Graceful gloves";
                itemDefinition.modelZoom = 592;
                itemDefinition.modelRotationY = 636;
                itemDefinition.modelRotationX = 2015;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 3;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 3;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45668;
                itemDefinition.maleModelId = 45669;
                itemDefinition.femaleModelId = 45670;
                return itemDefinition;

            case 14740:
                itemDefinition.name = "Graceful boots";
                itemDefinition.modelZoom = 976;
                itemDefinition.modelRotationY = 78;
                itemDefinition.modelRotationX = 1781;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 3;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45671;
                itemDefinition.maleModelId = 45672;
                itemDefinition.femaleModelId = 45673;
                return itemDefinition;

            case 14741:
                itemDefinition.name = "Graceful boots";
                itemDefinition.modelZoom = 976;
                itemDefinition.modelRotationY = 78;
                itemDefinition.modelRotationX = 1781;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 3;
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Drop" };
                itemDefinition.interfaceModelId = 45671;
                itemDefinition.maleModelId = 45672;
                itemDefinition.femaleModelId = 45673;
                return itemDefinition;

            case 14742:
                itemDefinition.name = "Amylase crystal";
                itemDefinition.modelZoom = 1230;
                itemDefinition.modelRotationX = 212;
                itemDefinition.modelRotationY = 148;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 7;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 45674;
                itemDefinition.stackingType = true;
                itemDefinition.stackIds = new int[]{ 14745, 14743, 14746, 14744, 0, 0, 0, 0, 0, 0 };
                itemDefinition.stackAmounts = new int[]{ 2, 3, 4, 5, 0, 0, 0, 0, 0, 0 };
                itemDefinition.trade = true;
                return itemDefinition;

            case 14743:
                itemDefinition.name = "null";
                itemDefinition.modelZoom = 1230;
                itemDefinition.modelRotationX = 212;
                itemDefinition.modelRotationY = 148;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 7;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 45675;
                itemDefinition.stackingType = true;
                return itemDefinition;

            case 14744:
                itemDefinition.name = "null";
                itemDefinition.modelZoom = 1230;
                itemDefinition.modelRotationX = 212;
                itemDefinition.modelRotationY = 148;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 7;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 45676;
                itemDefinition.stackingType = true;
                return itemDefinition;

            case 14745:
                itemDefinition.name = "null";
                itemDefinition.modelZoom = 1230;
                itemDefinition.modelRotationX = 212;
                itemDefinition.modelRotationY = 148;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 7;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 45677;
                itemDefinition.stackingType = true;
                return itemDefinition;

            case 14746:
                itemDefinition.name = "null";
                itemDefinition.modelZoom = 1230;
                itemDefinition.modelRotationX = 212;
                itemDefinition.modelRotationY = 148;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 7;
                itemDefinition.options = new String[]{ null, null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 45678;
                itemDefinition.stackingType = true;
                return itemDefinition;

            case 14747:
                itemDefinition.name = "Amylase pack";
                itemDefinition.modelRotationY = 124;
                itemDefinition.modelRotationX = 1848;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -1;
                itemDefinition.originalColors = new int[]{ 5805, 3619, 5805 };
                itemDefinition.modifiedColors = new int[]{ 5584, 5592, 5444 };
                itemDefinition.options = new String[]{ "Open", null, null, null, "Drop" };
                itemDefinition.interfaceModelId = 15032;
                return itemDefinition;

            case 14748:
                itemDefinition.name = "Black h'ween mask";
                itemDefinition.modelZoom = 730;
                itemDefinition.modelRotationY = 516;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = -10;
                itemDefinition.originalColors = new int[]{ 8, 9152 };
                itemDefinition.modifiedColors = new int[]{ 926, 0 };
                itemDefinition.options = new String[]{ null, "Wear", null, null, "Destroy" };
                itemDefinition.interfaceModelId = 2438;
                itemDefinition.maleModelId = 3188;
                itemDefinition.femaleModelId = 3192;
                itemDefinition.equipped_model_male_dialogue_1 = 1755;
                itemDefinition.equipped_model_female_dialogue_1 = 3187;
                itemDefinition.value = 7;
                return itemDefinition;

            case 14749:
                itemDefinition.name = "Black h'ween mask";
                itemDefinition.interfaceModelId = 2438;
                itemDefinition.value = 1;
                itemDefinition = convertNote(itemDefinition, 14748);
                return itemDefinition;

            case 14750:
                itemDefinition.name = "Dragon defender";
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = 8;
                itemDefinition.modelOffsetYOriginal = itemDefinition.modelPositionY = 8;
                itemDefinition.femaleModelTranslationY = 6;
                itemDefinition.value = 68007;
                itemDefinition.members = true;
                itemDefinition.maleModelId = 45689;
                itemDefinition.femaleModelId = 45690;
                itemDefinition.interfaceModelId = 45691;
                itemDefinition.modelZoom = 589;
                itemDefinition.options = new String[]{ null, "Wield", null, null, null };
                itemDefinition.modelRotationY = 498;
                itemDefinition.modelRotationX = 256;
                itemDefinition.modelRotationZ = 2047;
                return itemDefinition;

            case 14751:
                itemDefinition.name = "Rune pouch";
                itemDefinition.modelZoom = 350;
                itemDefinition.modelRotationY = 512;
                itemDefinition.modelRotationX = 475;
                itemDefinition.modelOffsetXOriginal = itemDefinition.modelPositionX = -4;
                itemDefinition.options = new String[]{ "Open", null, null, "Empty", "Destroy" };
                itemDefinition.interfaceModelId = 45692;
                itemDefinition.value = 10000;
                return itemDefinition;

            case 14752:
                ItemDefinitionC.copy(3710, itemDefinition);
                itemDefinition.name = "Referral ticket";
                itemDefinition.options = new String[]{ "Referrals", "Refer player", "Requests", null, "Destroy" };
                itemDefinition.value = 1;
                return itemDefinition;
        }
        return null;
    }

    /**
     * Converts an item to a note.
     *
     * @param ItemDefinitionC The definition of the item to convert.
     * @param unnotedId       The id of the unnoted item.
     * @return The noted item definition.
     */
    static ItemDefinitionC convertNote(ItemDefinitionC ItemDefinitionC, int unnotedId) {
        ItemDefinitionC.modelZoom = 760;
        ItemDefinitionC.modelRotationY = 552;
        ItemDefinitionC.modelRotationX = 28;
        ItemDefinitionC.modelPositionY = 2;
        ItemDefinitionC.options = new String[]{ null, null, null, null, "Drop" };
        ItemDefinitionC.interfaceModelId = 2429;
        ItemDefinitionC.noteItemId = unnotedId;
        ItemDefinitionC.noteTemplateId = 799;
        ItemDefinitionC.stackingType = true;
        return ItemDefinitionC;
    }
}
