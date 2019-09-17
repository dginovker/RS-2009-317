package org.gielinor.net.packet.in;

import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.content.skill.free.magic.MagicSpell;
import org.gielinor.game.content.skill.free.smithing.SmithingConstants;
import org.gielinor.game.content.skill.member.summoning.familiar.FamiliarSpecial;
import org.gielinor.game.interaction.Interaction;
import org.gielinor.game.interaction.Option;
import org.gielinor.game.node.entity.combat.CombatSwingHandler;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.SpellBookManager;
import org.gielinor.game.node.item.GroundItem;
import org.gielinor.game.node.item.GroundItemManager;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.net.packet.IncomingPacket;
import org.gielinor.net.packet.PacketBuilder;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.PlayerContext;
import org.gielinor.net.packet.out.ClearMinimapFlag;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;
import org.gielinor.utilities.misc.RunScript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.activity.duelarena.DuelRule;
import plugin.activity.duelarena.DuelSession;

/**
 * Represents the {@link org.gielinor.net.packet.IncomingPacket} for interacting with an item.
 *
 * @author <a href="https://Gielinor.org">Gielinor Logan G.</a>
 */
public class ItemOptionPacketHandler implements IncomingPacket {

    private static final Logger log = LoggerFactory.getLogger(ItemOptionPacketHandler.class);

    /**
     * Gets the correct interface ID for the player's opened component.
     *
     * @return The correct interface id.
     */
    public int getInterfaceId(int interfaceId, int childId) {
        switch (interfaceId) {
            case 25821:
                return 25810;

            case 25717:
                return 25728;
            /**
             * The smithing interface containers.
             */
            case 1119:
            case 1120:
            case 1121:
            case 1122:
            case 1123:
                return SmithingConstants.SMITHING_INTERFACE;

            case 24967:
            case 24971:
            case 24975:
            case 24979:
            case 24983:
            case 24987:
                return 24907;

            case 25530:
            case 25531:
                return 25521;

            case 25715:
            case 25716:
                return 25709;
        }
        // Duel remove staked items
        if (interfaceId >= 48110 && interfaceId <= 48137) {
            return 48110;
        }
        if (interfaceId >= 23335 && interfaceId < 23381) {
            return 23335;
        }
        if (interfaceId == 15445 || interfaceId == 15459 || interfaceId == 15473 || interfaceId == 15481) {
            return 13782;
        }
        if (interfaceId == 25041) {
            return 25030;
        }
        if (interfaceId == 25644) {
            return 25626;
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
        "SkillInterface",
        "ElnockExchangeInterfaceHandler",
        "GlassInterface",
        "GameInterface",
        "WarningInterface",
        "ShantayComponentPlugin",
        "WarningMessagePlugin",
        "ClanInterfacePlugin",
        "TutorialInterfacePlugin",
        "DropPartyInterface",
        "DropPartyOverlayInterface",
        "GravePurchaseInterface",
        "SummoningTabPlugin",
        "PuppyInterfacePlugin",
        "SummoningCreationPlugin",
        "FairyInterfaceHandler",
        "FurClothingInterface"
    };

    boolean exclude(String name) {
        for (String ex : exclude) {
            if (name.equalsIgnoreCase(ex)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void decode(final Player player, int opcode, PacketBuilder buffer) {
        int[] args = getArguments(player, buffer);
        if (args == null || player.getLocks().isComponentLocked()) {
            return;
        }
        int componentId = getInterfaceId(args[0], args[1]);
        if (componentId == 1688) {
            componentId = 1644;
        }
        int buttonId = args[1];
        int slot = args[2];
        int itemId = args[3];
        // TODO This is a cheapfix
        if (componentId == 5382 && player.getInterfaceState().getComponent(5382) != null && opcode == 129) {
            final Item item = player.getBank().get(slot);
            if (item != null) {
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        player.getBank().takeItem(slot, player.getBank().isNoteItems() ? player.getBank().getCount(item) : player.getInventory().getMaximumAdd(item));
                        return true;
                    }
                });
                return;
            }
        }
        if (componentId == 5382 && player.getInterfaceState().getComponent(5382) != null &&
            (opcode == 145 || opcode == 43 || opcode == 117 || opcode == 135 || opcode == 141 || opcode == 182)) {
            final Item item = player.getBank().get(slot);
            if (item != null) {
                World.submit(new Pulse(1, player) {

                    @Override
                    public boolean pulse() {
                        if (opcode == 135) {
                            player.setAttribute("runscript", new RunScript() {

                                @Override
                                public boolean handle() {
                                    int amount = (int) value;
                                    player.getBank().takeItem(slot, amount);
                                    player.getBank().getBankData().setLastX(amount);
                                    return false;
                                }
                            });
                            player.getDialogueInterpreter().sendInput(false, "Enter the amount.");
                            return true;
                        }
                        if (opcode == 182) {
                            player.getBank().takeAllButOne(slot);
                            return true;
                        }
                        if (opcode == 141) {
                            player.getBank().takeItem(slot, player.getBank().getBankData().getLastX());
                            return true;
                        }
                        player.getBank().takeItem(slot, opcode == 43 ? 10 : opcode == 145 ? 1 : 5);
                        return true;
                    }
                });
                return;
            }
        }
        player.debug("Component=" + componentId + ", button=" + buttonId + ", slot=" + slot + ", item=" + itemId + ", opcode=" + buffer.opcode());
        if (player.getLocks().isComponentLocked()) {
            return;
        }
        boolean grandExchange = (player.getInterfaceState().getOpened() != null &&
            ((player.getInterfaceState().getOpened().getId() == 25042 && opcode == 129) ||
                player.getInterfaceState().getOpened().getId() == 25521 &&
                    (opcode == 145 || opcode == 117 || opcode == 43 || opcode == 129)));
        if (itemId > -1 && slot > -1 && !grandExchange) {
            Container container = getContainer(player, componentId);
            if (container != null && handleItemInteraction(player, buffer.opcode(), itemId, slot, container)) {
                return;
            }
        }
        if (player.getZoneMonitor().clickButton(componentId, buttonId, slot, itemId, opcode)) {
            return;
        }
        Component component = player.getInterfaceState().getComponent(componentId);
        if (component == null) {
            player.debug("Component ID different than interface ID: " + componentId + (player.getInterfaceState().getOpened() == null ? "" : "(" + player.getInterfaceState().getOpened().getId() + ")"));
            component = new Component(componentId);
        }
        // Plugins
        ComponentPlugin componentPlugin = component.getPlugin();

        if (componentPlugin != null && componentPlugin.handle(player, component, buffer.opcode(), buttonId, slot, itemId)) {
            if (player.isDebug()) {
                log.info("Plugin handled: [{}].", componentPlugin.getClass().getSimpleName());
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
            // TODO 317 Re-enable after duel arena
            if (componentDefinition.getPlugin().handle(player, component, buffer.opcode(), buttonId, slot, itemId)) {
                log.info("ItemOption: [{}]. Interface: [{}].", component.getId(), componentDefinition.getPlugin().getClass().getSimpleName());
                break;
            }
        }
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
            case 3214:
            case 3086: // TODO Why is interface id different when "Drop/Destroy" option.
                //  case 5064: // Bank Inventory
                return player.getInventory();
            case 5382:
                return player.getBank();
            case 6574:
                DuelSession duelSession = player.getExtension(DuelSession.class);
                if (duelSession == null) {
                    return null;
                }
                return duelSession.getContainer(); // TODO 317 May not need to be here
//			case 3323:
//				TradeModule tradeModule = player.getExtension(TradeModule.class);
//				if (tradeModule == null) {
//					return null;
//				}
//				return tradeModule.getContainer();
        }
        return null;
    }

    /**
     * Gets the arguments for the action button.
     *
     * @param player The player.
     * @param packetBuilder The buffer.
     * @return The arguments [component, button, slot, item]
     * TODO send interface id from client!
     */
    private static int[] getArguments(Player player, PacketBuilder packetBuilder) {
        int data = -1;
        int componentId = -1;
        int buttonId = -1;
        int itemId = -1;
        int slot = -1;
        switch (packetBuilder.opcode()) {
            /**
             * Item option all (bank / inventory).
             */
            case 129:
                slot = packetBuilder.getShortA() & 0xFFFF;
                componentId = packetBuilder.getShort() & 0xFFFF;
                itemId = packetBuilder.getShortA() & 0xFFFF;
                break;
            /**
             * Item option x (bank / inventory).
             */
            case 135:
                slot = packetBuilder.getLEShort() & 0xFFFF;
                componentId = packetBuilder.getShortA() & 0xFFFF;
                itemId = packetBuilder.getLEShort() & 0xFFFF;
                break;
            /**
             * Item option 5/10 (sell 5 / bank 10).
             */
            case 43:
                componentId = packetBuilder.getLEShort() & 0xFFFF;
                itemId = packetBuilder.getShortA() & 0xFFFF;
                slot = packetBuilder.getShortA() & 0xFFFF;
                break;
            /**
             * First click.
             */
            case 122:
                componentId = packetBuilder.getLEShortA();
                slot = packetBuilder.getShortA();
                itemId = packetBuilder.getLEShort();
                break;
            /**
             * Wear / Wield
             */
            case 41:
                itemId = packetBuilder.getShort() & 0xFFFF;
                slot = packetBuilder.getShortA() & 0xFFFF;
                componentId = packetBuilder.getShortA() & 0xFFFF;
                break;
            /**
             * Alternate option 1.
             */
            case 75:
                componentId = packetBuilder.getLEShortA() & 0xFFFF;
                slot = packetBuilder.getLEShort();
                itemId = packetBuilder.getShortA();
                break;
            /**
             * Alternate option 2.
             */
            case 16:
                itemId = packetBuilder.getShortA() & 0xFFFF;
                slot = packetBuilder.getLEShortA() & 0xFFFF;
                componentId = packetBuilder.getLEShortA() & 0xFFFF;
                break;
            /**
             * Operate.
             */
            case 117:
                componentId = packetBuilder.getLEShortA() & 0xFFFF;
                itemId = packetBuilder.getLEShortA() & 0xFFFF;
                slot = packetBuilder.getLEShort() & 0xFFFF;
                break;
            /**
             * Withdraw last X.
             */
            case 141:
                componentId = packetBuilder.getLEShortA() & 0xFFFF;
                itemId = packetBuilder.getLEShortA() & 0xFFFF;
                slot = packetBuilder.getLEShort() & 0xFFFF;
                break;
            /**
             * Withdraw all but 1.
             */
            case 182:
                componentId = packetBuilder.getLEShortA() & 0xFFFF;
                itemId = packetBuilder.getLEShortA() & 0xFFFF;
                slot = packetBuilder.getLEShort() & 0xFFFF;
                break;
            /**
             * Third click.
             */
            case 145:
                componentId = packetBuilder.getShortA() & 0xFFFF;
                slot = packetBuilder.getShortA() & 0xFFFF;
                itemId = packetBuilder.getShortA() & 0xFFFF;
                break;
            /**
             * Drop / Destroy.
             */
            case 87:
                itemId = packetBuilder.getShortA() & 0xFFFF;
                componentId = packetBuilder.getShortA() & 0xFFFF;
                slot = packetBuilder.getShortA() & 0xFFFF;
                break;
            /**
             * Ground option 1.
             */
            case 236:
                int y = packetBuilder.getLEShort();
                itemId = packetBuilder.getShort();
                int x = packetBuilder.getLEShort();
                handleGroundItemInteraction(player, 2, itemId, Location.create(x, y, player.getLocation().getZ()));
                return null;

            /**
             * Ground option 2.
             */
            case 253:
                x = packetBuilder.getLEShort();
                y = packetBuilder.getLEShortA();
                itemId = packetBuilder.getShortA();
                handleGroundItemInteraction(player, 3, itemId, Location.create(x, y, player.getLocation().getZ()));
                return null;

            /**
             * Interface on item.
             */
            case 237:
                slot = packetBuilder.getShort();
                itemId = packetBuilder.getShortA();
                int interfaceId = packetBuilder.getShort();
                componentId = packetBuilder.getShortA();
                if (componentId >= 30000 && componentId <= 30350) {
                    interfaceId = 29999;
                }
                player.getActionSender().sendDebugPacket(packetBuilder.opcode(), "InterfaceOnItem", "Item ID: " + itemId,
                    "Slot: " + slot,
                    "Interface ID: " + interfaceId,
                    "Spell ID: " + componentId);
                if (slot < 0 || slot > 27) {
                    break;
                }
                Item item = player.getInventory().get(slot);
                if (item == null) {
                    break;
                }
                if (componentId == 25942) {
                    if (player.getFamiliarManager().hasFamiliar()) {
                        player.getFamiliarManager().getFamiliar().executeSpecialMove(new FamiliarSpecial(item, interfaceId, componentId, item));
                    } else {
                        player.getActionSender().sendMessage("You don't have a follower.");
                    }
                    return null;
                }
                if (player.getAttribute("magic:delay", -1) > World.getTicks()) {
                    break;
                }
                MagicSpell.castSpell(player, SpellBookManager.SpellBook.forInterface(player.getSpellBookManager().getSpellBook()), componentId, item);
                return null;

            /**
             * Interface on ground item.
             */
            case 181:
                y = packetBuilder.getLEShort();
                itemId = packetBuilder.getShort();
                x = packetBuilder.getLEShort();
                final int spellId = packetBuilder.getShortA();
                player.getActionSender().sendDebugPacket(packetBuilder.opcode(), "InterfaceOnGroundItem", "Item ID: " + itemId,
                    "X: " + x,
                    "Y:" + y,
                    "Spell ID: " + spellId);
                final Item groundItem = GroundItemManager.get(itemId, Location.create(x, y, player.getLocation().getZ()), player);
                if (groundItem == null || !player.getLocation().withinDistance(groundItem.getLocation())) {
                    break;
                }
                if (player.getAttribute("magic:delay", -1) > World.getTicks()) {
                    break;
                }
//				if (player.getZoneMonitor().clickButton(interfaceId, componentId, spell, itemId, opcode)) {
//					break;
//				}
                if (CombatSwingHandler.isProjectileClipped(player, groundItem, false)) {
                    MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, spellId, groundItem);
                } else {
                    if (DuelRule.NO_MOVEMENT.enforce(player, false)) {
                        PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
                        break;
                    }
                    player.getPulseManager().run(new MovementPulse(player, groundItem) {

                        @Override
                        public boolean update() {
                            if (CombatSwingHandler.isProjectileClipped(player, groundItem, false)) {
                                super.destination = player.getLocation();
                            }
                            boolean finished = super.update();
                            if (finished) {
                                player.getWalkingQueue().reset();
                            }
                            return finished;

                        }

                        @Override
                        public boolean pulse() {
                            MagicSpell.castSpell(player, SpellBookManager.SpellBook.MODERN, spellId, groundItem);
                            return true;
                        }
                    }, "movement");
                }
                return null;

            default:
                player.getActionSender().sendDebugMessage("[ItemOptionPacketHandler]: Invalid opcode " + packetBuilder.opcode() + ".");
                return null;
        }
        player.debug("ItemOptionPacketHandler: Component=" + componentId + ", slot=" + slot + ", item=" + itemId + ", opcode=" + packetBuilder.opcode());

        return new int[]{ componentId, buttonId, slot, itemId };
    }

    /**
     * Handles the ground item interaction.
     *
     * @param player   The player.
     * @param index    The index of the action.
     * @param itemId   The item id.
     * @param location The location of the item.
     */
    private static void handleGroundItemInteraction(final Player player, int index, int itemId, Location location) {
        final GroundItem item = GroundItemManager.get(itemId, location, player);
        if (item == null) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            return;
        }
        final Option option = item.getInteraction().get(index);
        if (option == null) {
            PacketRepository.send(ClearMinimapFlag.class, new PlayerContext(player));
            Interaction.handleInvalidInteraction(player, item, Option.NULL);
            return;
        }
        item.getInteraction().handle(player, option);
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
        // TODO switch container
        switch (opcode) {
            case 117: // First option
                index = 0;
                break;
            case 41: // Second option (wield/wear)
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
}
