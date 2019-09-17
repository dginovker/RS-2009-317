package org.gielinor.net.packet.in;

import java.util.List;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.dialogue.DialogueAction;
import org.gielinor.game.content.dialogue.DialoguePlugin;
import org.gielinor.game.content.dialogue.OptionSelect;
import org.gielinor.game.content.skill.free.smithing.SmithingConstants;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.entity.combat.equipment.AutocastSpell;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.node.entity.player.link.music.MusicEntry;
import org.gielinor.game.node.item.Item;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.BankContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.activity.duelarena.DuelRule;
import plugin.activity.duelarena.DuelSession;
import plugin.interaction.inter.SkillTabInterface;

/**
 * Handles clicking on most buttons in the interface.
 *
 * @author Graham Edgecombe
 */
public class ActionButtonPacketHandler implements IncomingPacket {

    private static final Logger log = LoggerFactory.getLogger(ActionButtonPacketHandler.class);

    /**
     * The action button packet opcode.
     */
    private static final int ACTION_BUTTON_OPCODE = 185;
    /**
     * The secondary action button packet opcode.
     */
    private static final int SECONDARY_ACTION_BUTTON_OPCODE = 221;
    /**
     * The third action button packet opcode.
     */
    private static final int THIRD_ACTION_BUTTON_OPCODE = 169;
    /**
     * The wield item packet opcode.
     */
    private static final int WIELD_ITEM_OPCODE = 41;
    /**
     * The third click item packet opcode.
     */
    private static final int THIRD_CLICK_ITEM_OPCODE = 145;

    /**
     * Gets the correct interface ID for the player's opened component.
     *
     * @param player  The player.
     * @param childId The id of the child clicked.
     * @return The correct interface id.
     */
    public static int getInterfaceId(Player player, int childId) {
        ComponentDefinition componentDefinition = ComponentDefinition.forId(childId);
        int interfaceId = componentDefinition.getParentId();
        for (MusicEntry musicEntry : MusicEntry.getSongs().values()) {
            if (childId == musicEntry.getButtonId()) {
                return 962;
            }
        }
        if (childId == 25705) {
            return 25706;
        }
        if (childId > 25951 && childId < 25972) {
            return 25951;
        }
        if (childId > 25904 && childId <= 25951) {
            return 25904;
        }
        if (childId == 25725) {
            return 25717;
        }
        if (childId == 25818) {
            return 25810;
        }
        if (childId == 24250) {
            return 24242;
        }
        if (childId >= 24127 && childId <= 24240) {
            return 24126;
        }
        if (childId == 18219 || (childId >= 18204 && childId <= 18219)) {
            return 18178;
        }
        if (childId >= 24794 && childId < 24883) {
            return 24794;
        }
        if (childId >= 268 && childId <= 271) {
            return 268;
        }
        if (childId == 32846 || childId == 32847 || childId == 32848 || childId == 32849 || (childId >= 32856 && childId <= 32892)) {
            return 5382;
        }
        if (childId >= 25682 && childId <= 25698) {
            return 25682;
        }
        if (childId == 13914) {
            return 2808;
        }
        if (childId > 23035 && childId <= 23056) {
            return 23035;
        }
        if (childId > 23057 && childId <= 23098) {
            return 23057;
        }
        if (childId >= 24547 && childId <= 24568) {
            return 24546;
        }
        if (childId >= 24570 && childId <= 24585) {
            return 24569;
        }
        if (childId >= 24587 && childId <= 24606) {
            return 24586;
        }
        if (childId == 15445 || childId == 15459 || childId == 15473 || childId == 15481) {
            return 13782;
        }
        if (AutocastSpell.forAncients(childId) != null) {
            return 1689;
        }
        if (AutocastSpell.forSlayer(childId) != null) {
            return 12050;
        }
        if (childId == 31041) {
            return 31040;
        }
        if (childId == 152) {
            return 904;
        }
        if (childId >= 17202 && childId <= 17241) {
            return 17200;
        }
        if (childId > 259 && childId <= 267) {
            return 259;
        }
        if (childId >= 32764 && childId <= 32841) {
            return 32762;
        }
        if (childId >= 5000 && childId <= 5005) {
            return 4959;
        }
        for (OptionSelect optionSelect : OptionSelect.values()) {
            if (optionSelect.name().startsWith("GLASS_")) {
                for (int buttonId : optionSelect.getIds()) {
                    if (childId == buttonId) {
                        return 11462;
                    }
                }
            }
        }
        if (childId == 18470 || childId == 7455) {
            return 1151;
        }
        if (childId > 22697 && childId < 22722) {
            return 22697;
        }
        if (childId >= 13717 && childId <= 13720) {
            return 1743;
        }
        if (childId >= 22654 && childId <= 22693) {
            return 22653;
        }
        if (childId > 26597 && childId <= 26705) {
            return 26597;
        }
        if (childId > 48172 && childId < 48267) {
            return 48172;
        }
        if (childId >= 48067 && childId < 48170) {
            return 48000;
        }
        for (DuelRule duelRule : DuelRule.values()) {
            if (duelRule.getButtonId() == childId) {
                return 48000;
            }
        }
        if (childId > 32100 && childId < 32316) {
            return 32100;
        }
        if (childId > 32320 && childId < 32347) {
            return 32320;
        }
        if (childId > 46900 && childId < 46915) {
            return 46900;
        }
        if (childId > 27654 && childId < 27802) {
            return 27700;
        }
        if (childId >= 27137 && childId < 27167 || childId == 27260) {
            return 27135;
        }
        if (childId >= 29157 && childId < 29170 || childId == 22694) {
            return 638;
        }
        if (childId >= 29303 && childId < 29340 || childId == 22695) {
            return 29300;
        }
        if (childId > 23235 && childId < 23335 || childId == 22696) {
            return 23235;
        }
        // Graphics settings
        if (childId > 23580 && childId <= 23596 || (childId >= 24883 && childId <= 24905)) {
            return 23580;
        }
        // Advanced options
        if (childId > 26387 && childId <= 26487) {
            // TODO 317 Change childId max
            return 26387;
        }
        if (childId >= 58002 && childId <= 58050) {
            return BankContainer.BANK_INVENTORY_INTERFACE;
        }
        // Duel remove staked items
        if (interfaceId >= 48110 && interfaceId <= 48137) {
            return 48110;
        }
        if (interfaceId >= 23335 && interfaceId < 23381) {
            return 23335;
        }
        if (player.getInterfaceState().hasChatbox()) {
            for (OptionSelect optionSelect : OptionSelect.values()) {
                if (!optionSelect.name().startsWith("SMELTING")) {
                    continue;
                }
                for (int buttonId : optionSelect.getIds()) {
                    if (childId == buttonId) {
                        return SmithingConstants.SMELTING_INTERFACE;
                    }
                }
            }
            switch (interfaceId) {
                /**
                 * One item select.
                 */
                case 2653:
                case 1743:
                    return 4429;
            }
        }
        /**
         * Guide prices interface.
         */
        if (childId >= 25627 && childId <= 25640) {
            return 25626;
        }
        switch (interfaceId) {
            /**
             * The smithing interface containers.
             */
            case 1119:
            case 1120:
            case 1121:
            case 1122:
            case 1123:
                return SmithingConstants.SMITHING_INTERFACE;

            /**
             * Auto retaliate.
             */
            case 5855:
                return 22845;

            /**
             * Character design.
             */
            case 3651:
                return 3559;

            /**
             * Quest.
             */
            case 671:
            case 9839:
            case 12752:
            case 17192:
            case 7271:
            case 29270:
                return 638;
        }
        switch (childId) {
            case 24908:
            case 24909:
            case 24910:
            case 24912:
            case 24913:
            case 24914:
            case 24915:
            case 24916:
            case 24917:
            case 24918:
            case 24919:
            case 24926:
            case 24927:
            case 24929:
            case 24930:
            case 24933:
            case 24934:
            case 24936:
            case 24937:
            case 24940:
            case 24941:
            case 24943:
            case 24944:
            case 24947:
            case 24948:
            case 24950:
            case 24951:
            case 24954:
            case 24955:
            case 24957:
            case 24958:
            case 24961:
            case 24962:
            case 24964:
            case 24965:
            case 24987:
            case 24983:
            case 24979:
            case 24975:
            case 24971:
            case 24967:
            case 24988:
            case 24980:
            case 24984:
            case 24968:
            case 24972:
            case 24976:
            case 24920:
            case 24921:
            case 24922:
            case 24923:
            case 24924:
            case 24925:
            case 24994:
            case 24995:
            case 24996:
            case 24997:
            case 24998:
            case 24999:
            case 25000:
            case 25001:
            case 25002:
            case 25003:
            case 25004:
            case 25005:
            case 25006:
            case 25007:
            case 25008:
            case 25009:
            case 25010:
            case 25011:
            case 25012:
            case 25013:
            case 25014:
            case 25015:
            case 25016:
            case 25017:
            case 25018:
            case 25019:
            case 25020:
            case 25021:
            case 25022:
            case 25023:
            case 25024:
            case 25025:
            case 25026:
            case 25027:
            case 25028:
            case 25029:
                return 24907;

            case 25393:
            case 25394:
            case 25395:
            case 25397:
            case 25398:
            case 25399:
            case 25400:
            case 25401:
            case 25402:
            case 25403:
            case 25404:
            case 25407:
            case 25408:
            case 25410:
            case 25411:
            case 25412:
            case 25413:
            case 25414:
            case 25416:
            case 25417:
            case 25419:
            case 25420:
            case 25418:
            case 25423:
            case 25424:
            case 25422:
            case 25427:
            case 25428:
            case 25426:
            case 25431:
            case 25432:
            case 25430:
            case 25435:
            case 25436:
            case 25434:
            case 25438:
            case 25439:
            case 25440:
            case 25441:
            case 25443:
            case 25444:
            case 25446:
            case 25447:
            case 25449:
            case 25450:
            case 25452:
            case 25453:
            case 25455:
            case 25456:
            case 25458:
            case 25459:
            case 25460:
            case 25461:
            case 25463:
            case 25464:
                return 25392;

            case 25043:
            case 25044:
            case 25045:
            case 25047:
            case 25048:
            case 25049:
            case 25050:
            case 25051:
            case 25052:
            case 25053:
            case 25054:
            case 25055:
            case 25057:
            case 25058:
            case 25059:
            case 25060:
            case 25061:
            case 25062:
            case 25063:
            case 25064:
            case 25066:
            case 25067:
            case 25069:
            case 25070:
            case 25068:
            case 25073:
            case 25074:
            case 25072:
            case 25077:
            case 25078:
            case 25076:
            case 25081:
            case 25082:
            case 25080:
            case 25085:
            case 25086:
            case 25084:
            case 25088:
            case 25089:
            case 25090:
            case 25091:
            case 25093:
            case 25094:
            case 25096:
            case 25097:
            case 25099:
            case 25100:
            case 25102:
            case 25103:
            case 25105:
            case 25106:
            case 25108:
            case 25109:
            case 25110:
            case 25111:
            case 25113:
            case 25114:
            case 25465:
            case 25466:
            case 25478:
            case 25482:
            case 25485:
                return 25042;

            case 25561:
            case 25564:
            case 25567:
            case 25570:
            case 25573:
            case 25576:
            case 25579:
            case 25582:
            case 25585:
            case 25588:
            case 25591:
            case 25594:
                return 25531;

            /**
             * Destroy item.
             */
            case 14175:
            case 14176:
                return 14170;
            /**
             * Equipment tab.
             */
            case 27653:
            case 27654:
            case 27651:
                return 1644;

            /**
             * Bank.
             */
            case 5386:
            case 5387:
            case 8130:
            case 8131:
                return 5382;
            /**
             * Prayer tab.
             */
            case 5609:
            case 5610:
            case 5611:
            case 19812:
            case 19814:
            case 5612:
            case 5614:
            case 5613:
            case 5615:
            case 5616:
            case 5619:
            case 5618:
            case 19818:
            case 19816:
            case 5617:
            case 5620:
            case 5621:
            case 5622:
            case 5623:
            case 19821:
            case 19825:
            case 685:
            case 684:
            case 683:
            case 19823:
            case 19827:
                return 671;
        }
        for (SkillTabInterface.SkillConfig skillConfig : SkillTabInterface.SkillConfig.values()) {
            if (skillConfig.getButtonId() == childId) {
                return 3917;
            }
        }
        if (childId >= 50001 && childId <= 50025) {
            return 50000;
        }
        if (childId >= 30000 && childId <= 30350) {
            return 29999;
        }
        return interfaceId;
    }

    String[] exclude = new String[]{
        "HairInterfacePlugin",
        "MakeOverInterface",
        "ThessaliaInterface",
        "PCRewardInterface",
        "RequestAssistInterface",
        "SkillTabInterface",
        "LoginInterfacePlugin",
        "WildernessInterfacePlugin",
        "AutocastSelectPlugin",
        "GrandExchangeInterface",
        "SawmillPlankInterface",
        "JewelleryInterface",
        "SpinningInterface",
        "VotingRewardInterface",
        "SkillInterface",
        "ElnockExchangeInterfaceHandler",
        "GlassInterface",
        "WarningInterface",
        "ShantayComponentPlugin",
        "WarningMessagePlugin",
        "ClanInterfacePlugin",
        "TutorialInterfacePlugin",
        "DropPartyInterface",
        "DropPartyOverlayInterface",
        "GravePurchaseInterface",
        "CharacterDesignInterface",
        "SummoningTabPlugin",
        "PuppyInterfacePlugin",
        "SummoningCreationPlugin",
        "FairyInterfaceHandler",
    };

    boolean exclude(String name) {
        for (String ex : exclude) {
            if (name.equalsIgnoreCase(ex)) {
                return true;
            }
        }
        return false;
    }

    public int getRealId(int componentId) {
        switch (componentId) {
            /**
             * Party drop chest interface.
             */
            case 2156:
                return 2274;


        }
        return componentId;
    }

    @Override
    public void decode(Player player, int opcode, PacketBuilder packetBuilder) {
        int[] args = getArguments(player, packetBuilder);
        if (args == null) {
            return;
        }
        player = PlayerOptionPacketHandler.getPlayer(player);
        int componentId = args[0];
        if (componentId == 1688) {
            componentId = 1644;
        }

        int childId = args[1];
        final int slot = args[2];
        int itemId = args[3];
        player.getActionSender().sendDebugPacket(opcode,
            "ActionButton", "Interface ID: " + componentId,
            "Child: " + childId,
            "Slot: " + slot,
            "Item ID: " + itemId);
        if (player.getLocks().isComponentLocked()) {
            return;
        }
        // TODO After fixing interfaces
        if (ActionButtonHandler.handle(player, componentId, childId)) {
            return;
        }
        if (itemId > -1 && slot > -1) {
            Container container = getContainer(player, componentId);
            if (container != null && handleItemInteraction(player, packetBuilder.opcode(), itemId, slot, container)) {
                return;
            }
        }
        if (player.getZoneMonitor().clickButton(componentId, childId, slot, itemId, opcode)) {
            return;
        }
        Component component = player.getInterfaceState().getComponent(getRealId(componentId));
        if (component == null) {
            player.debug("Component ID different than interface ID: " + componentId);
            component = new Component(0);
        }
        // Plugins
        ComponentPlugin componentPlugin = component.getPlugin();
        if (componentPlugin != null && componentPlugin.handle(player, component, opcode, childId, -1, -1)) {
            if (player.isDebug()) {
                log.info("Handled plugin: [{}] with [{}].", childId, componentPlugin.getClass().getSimpleName());
            }
            return;
        }
        for (ComponentDefinition componentDefinition : ComponentDefinition.getDefinitions().values()) {
            if (componentDefinition == null) {
                continue;
            }
            if (componentDefinition.getPlugin() == null) {
                continue;
            }
            if (exclude(componentDefinition.getPlugin().getClass().getSimpleName())) {
                continue;
            }
            if (componentDefinition.getPlugin().handle(player, component, opcode, childId, -1, -1)) {
                if (player.isDebug()) {
                    log.info("Handled plugin: [{}].", componentDefinition.getPlugin().getClass().getSimpleName());
                }
                return;
            }
        }
        // TODO 317 move to plugin
        if (childId == 49002 && player.getSpellBookManager().getSpellBook()
            == SpellBookManager.SpellBook.LUNAR.getInterfaceId()) {
            player.getInterfaceState().openDefaultTabs();
        }
        // Dialogues
        if (player.getDialogueInterpreter().getDialogue() == null &&
            player.getDialogueInterpreter().getDialogueStage() == null) {
            player.getInterfaceState().closeChatbox();
            List<DialogueAction> dialogueActions = player.getDialogueInterpreter().getActions();
            if (dialogueActions.size() > 0) {
                DialogueAction dialogueAction = dialogueActions.get(0);
                OptionSelect optionSelect = OptionSelect.forId(childId);
                // set the child
                if (optionSelect == null) {
                    optionSelect = OptionSelect.OPTION_SELECT;
                    optionSelect.setInterfaceId(componentId);
                    optionSelect.setId(childId);
                }
                optionSelect.setChildId(childId);
                dialogueAction.handle(player, optionSelect);
                dialogueActions.remove(dialogueAction);
                dialogueActions.clear();
            }
            return;
        }

        DialoguePlugin dialoguePlugin = player.getDialogueInterpreter().getDialogue();
        if (dialoguePlugin != null) {
            if (dialoguePlugin.handle(componentId, childId)) {
                return;
            }
            OptionSelect optionSelect = OptionSelect.forId(childId);
            // set the child
            if (optionSelect == null) {
                optionSelect = OptionSelect.OPTION_SELECT;
                optionSelect.setInterfaceId(componentId);
                optionSelect.setId(childId);
            }
            optionSelect.setChildId(childId);
            if (player.getDialogueInterpreter().handle(componentId, optionSelect)) {
                return;
            }
        }
        if (player.getInterfaceState().hasChatbox()) {
            player.getInterfaceState().closeChatbox();
            if (player.getDialogueInterpreter().getDialogue() != null) {
                player.getDialogueInterpreter().getDialogue().end();
            }
            log.warn("Possible unhandled chatbox interface [{}] with child [{}].",
                componentId, childId);
        }
    }

    /**
     * Gets the arguments for the action button.
     *
     * @param player        The player.
     * @param packetBuilder The packet builder.
     * @return The arguments (component, button, slot, item).
     */
    private static int[] getArguments(Player player, PacketBuilder packetBuilder) {
        int componentId = -1;
        int childId = -1;
        int itemId = -1;
        int slot = -1;
        switch (packetBuilder.opcode()) {
            case ACTION_BUTTON_OPCODE:
                childId = packetBuilder.getShort() & 0xFFFF;
                componentId = getInterfaceId(player, childId);
                if (childId == 13901 || childId == 12034) {
                    componentId = 2808;
                }
                break;
            case SECONDARY_ACTION_BUTTON_OPCODE:
                childId = packetBuilder.getShortA() & 0xFFFF;
                componentId = getInterfaceId(player, childId);
                if (childId == 13901 || childId == 12034) {
                    componentId = 2808;
                }
                break;
            case THIRD_ACTION_BUTTON_OPCODE:
                childId = packetBuilder.getLEShort() & 0xFFFF;
                componentId = getInterfaceId(player, childId);
                if (childId == 13901 || childId == 12034) {
                    componentId = 2808;
                }
                break;
            case WIELD_ITEM_OPCODE:
                itemId = packetBuilder.getLEShort();
                slot = packetBuilder.getShortA();
                childId = packetBuilder.getShortA();
                componentId = getInterfaceId(player, childId);
                break;
            case THIRD_CLICK_ITEM_OPCODE:
                componentId = packetBuilder.getShortA();
                slot = packetBuilder.getShortA();
                itemId = packetBuilder.getShortA();
                childId = getInterfaceId(player, componentId);
                break;
        }
        player.debug(packetBuilder.opcode() + " Component ID: " + componentId + ", Child ID: " + childId + ", Slot: " + slot + ", Item ID: " + itemId);
        return new int[]{ componentId, childId, slot, itemId };
    }


    /**
     * Handles an item interaction.
     *
     * @param player The player.
     * @param opcode The opcode.
     * @param itemId The id of the item.
     */
    private static boolean handleItemInteraction(Player player, int opcode, int itemId, int slot, Container container) {
        if (slot < 0 || slot >= container.capacity()) {
            return false;
        }
        Item item = container.get(slot);
        if (item == null || item.getId() != itemId) {
            return false;
        }
        int index = 0;
        // TODO Check container ?
        // TODO If second option = 117 and container = bank
        switch (opcode) {
            case 117: // First option
                index = 0;
                break;
            case WIELD_ITEM_OPCODE:
                index = 1;
                break;
            case 16:
            case 145: // Third option / Alternate option 2
                index = 2;
                break;
            case 20:
            case 75: // Fourth option / Alternate option 4
                index = 3;
                break;
            case 87: // Fifth option (drop/destroy)
                index = 4;
                break;
        }
        final Option option = item.getInteraction().get(index);
        if (option == null || player.getLocks().isInteractionLocked()) {
            return false;
        }
        item.getInteraction().handleItemOption(player, option, container);
        return true;
    }

    /**
     * Gets the container for the component id.
     *
     * @param player      The player.
     * @param componentId The component id.
     * @return The container.
     */
    private Container getContainer(Player player, int componentId) {
        switch (componentId) {
            case 3213:
            case 3214:
            case 3086:
                return player.getInventory();
            case 5382:
                return player.getBank();
            case 6574:
                DuelSession duelSession = player.getExtension(DuelSession.class);
                if (duelSession == null) {
                    return null;
                }
                return duelSession.getContainer(); // TODO 317 May not need to be here
        }
        return null;
    }

}
