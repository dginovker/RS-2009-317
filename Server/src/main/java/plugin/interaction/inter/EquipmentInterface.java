package plugin.interaction.inter;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.component.CloseEvent;
import org.gielinor.game.component.Component;
import org.gielinor.game.component.ComponentDefinition;
import org.gielinor.game.component.ComponentPlugin;
import org.gielinor.game.interaction.OptionHandler;
import org.gielinor.game.node.entity.combat.DeathTask;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.lock.Lock;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.entity.player.link.Sidebar;
import org.gielinor.game.node.entity.player.link.audio.Audio;
import org.gielinor.game.node.entity.player.link.prayer.PrayerType;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.World;
import org.gielinor.net.packet.OperationCode;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.Equipment;
import org.gielinor.rs2.plugin.Plugin;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.utilities.string.TextUtils;

/**
 * Represents the equipment interface.
 *
 * @author Emperor
 */
public final class EquipmentInterface extends ComponentPlugin {

    public static int[] EQUIPMENT_INTERFACES = { 1644, 21172, 25609 };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int equipmentInterface : EQUIPMENT_INTERFACES) {
            ComponentDefinition.put(equipmentInterface, this);
        }
        return this;
    }

    @Override
    public boolean handle(final Player p, Component component, int opcode, int button, final int slot, final int itemId) {
        switch (opcode) {
            case 185:
                switch (button) {
                    case 21299:
                        p.getInterfaceState().close();
                        return true;
                    /**
                     * Equipment stats.
                     */
                    case 27653:
                        if (p.getInterfaceState().isOpened() && p.getInterfaceState().getOpened().getId() == 21172) {
                            return true;
                        }
                        p.setAttribute("equip_stats_open", true);
                        Equipment.update(p);
                        p.getInterfaceState().removeTabs(0, 1, 2, 5, 6, 7, 8, 9, 10, 11, 12, 13);
                        p.getInterfaceState().openTab(Sidebar.EQUIPMENT_TAB.ordinal(), new Component(1644));
                        p.getInventory().refresh();
                        ItemDefinition.statsUpdate(p);
                        p.getInterfaceState().open(new Component(21172)).setCloseEvent(new CloseEvent() {

                            @Override
                            public void close(Player player, Component component) {
                                player.removeAttribute("equip_stats_open");
                                player.getInterfaceState().openDefaultTabs();
                            }

                            @Override
                            public boolean canClose(Player player, Component component) {
                                return true;
                            }
                        });
                        return true;
                    case 27654:
                        if (p.getInterfaceState().isOpened() && p.getInterfaceState().getOpened().getId() == 25609) {
                            return true;
                        }
                        boolean skulled = p.getSkullManager().isSkulled();
                        boolean usingProtect = p.getPrayer().get(PrayerType.PROTECT_ITEMS) || p.getPrayer().get(PrayerType.CURSE_PROTECT_ITEMS);
                        p.getInterfaceState().openComponent(25609);
                        Container[] itemArray = DeathTask.getContainers(p);
                        int keptItems = skulled ? (usingProtect ? 1 : 0) : (usingProtect ? 4 : 3);
                        p.getActionSender().sendString(25621, itemArray[1].getNetworth(true, true) > Integer.MAX_VALUE ? "Lots!" :
                            (TextUtils.getFormattedNumber((int) itemArray[1].getNetworth(true, true)) + " gp"));
                        String keepMessage = usingProtect ?
                            (skulled ? "You're marked with a <col=FF3333>skull<col=FF981F>.\\nThis reduces the items you\\nkeep from three to zero!" :
                                "You have the <col=FF3333>Protect Items\\nprayer active, which saves\\nyou one extra item!")
                            : (skulled ? "You're marked with a <col=FF3333>skull<col=FF981F>.\\nThis reduces the items you\\nkeep from three to zero!" :
                            "You have no factors affecting\\nthe items you keep.");
                        if (skulled && usingProtect) {
                            keepMessage += "\\n\\nHowever, you also have the\\n<col=FF3333>Protect Items <col=FF981F>prayer active,\\nwhich saves you one extra\\nitem!";
                        }
                        p.getActionSender().sendString(25619, keepMessage);
                        p.getActionSender().sendString(25623, "~ " + keptItems + " ~");
                        PacketRepository.send(ContainerPacket.class, new ContainerContext(p, 25624, 0, 91, itemArray[0], false));
                        PacketRepository.send(ContainerPacket.class, new ContainerContext(p, 25625, 0, 91, itemArray[1], false));
                        return true;

                    case 27651:
                        p.getPriceGuideContainer().reset();
                        p.getPriceGuideContainer().open();
                        return true;
                }
                return false;
            case OperationCode.OPTION_OFFER_FIVE:
                if (component.getId() != 1644 && component.getId() != 21172) {
                    return false;
                }
                World.submit(new Pulse(1, p) {

                    @Override
                    public boolean pulse() {
                        operate(p, slot, itemId);
                        return true;
                    }
                });
                return true;
            case OperationCode.OPTION_OFFER_ONE:
                if (component.getId() == 25624 || component.getId() == 25625) {
                    p.getActionSender().sendMessage("You will " + (component.getId() == 25624 ? "keep" : "lose") + " this item should you die.");
                    return true;
                }
                if (component.getId() != 1644 && component.getId() != 21172) {
                    return false;
                }
                p.getPulseManager().clear();
                World.submit(new Pulse(1, p) {

                    @Override
                    public boolean pulse() {
                        unequip(p, slot, itemId);
                        return true;
                    }
                });
                return true;
            // TODO Items kept on death
//			default:
//				switch (button) {
//					case 50:
//						if (p.getInterfaceState().isOpened() && p.getInterfaceState().getOpened().getId() == 102) {
//							return true;
//						}
//						boolean skulled = p.getSkullManager().isSkulled();
//						boolean usingProtect = p.getPrayer().get(PrayerType.PROTECT_ITEMS);
//						p.getInterfaceState().openComponent(102);
//						p.getActionSender().sendAccessMask(211, 0, 2, 6684690, 4);
//						p.getActionSender().sendAccessMask(212, 0, 2, 6684693, 42);
//						Container[] itemArray = DeathTask.getContainers(p);
//						Container kept = itemArray[0];
//						int state = 0; //1=familiar carrying items
//						int keptItems = skulled ? (usingProtect ? 1 : 0) : (usingProtect ? 4 : 3);
//						int zoneType = p.getZoneMonitor().getType();
//						int pvpType = p.getSkullManager().isWilderness() ? 0 : 1;
//						Object[] params = new Object[]{11510, 12749, "", state, pvpType, kept.getId(3), kept.getId(2), kept.getId(1), kept.getId(0), keptItems, zoneType};
//						PacketRepository.send(ContainerPacket.class, new ContainerContext(p, 149, 0, 91, itemArray[1], false));
//						p.getActionSender().sendRunScript(118, "iiooooiisii", params);
//						break;
//				}
        }
        return false;
    }

    /**
     * Operates an item.
     *
     * @param player The player.
     * @param slot   The container slot.
     * @param itemId The item id.
     */
    public void operate(Player player, int slot, int itemId) {
        if (slot < 0 || slot > 13) {
            return;
        }
        Item item = player.getEquipment().get(slot);
        if (item == null) {
            return;
        }
        OptionHandler handler = item.getOperateHandler();
        if (handler != null && handler.handle(player, item, "operate")) {
            return;
        }
        player.getActionSender().sendMessage("There is no way to operate that item.");
    }

    /**
     * Unequips an item.
     *
     * @param player the player.
     * @param slot   the slot.
     * @param itemId the item id.
     */
    public void unequip(Player player, int slot, int itemId) {
        if (slot < 0 || slot > 13) {
            return;
        }
        Item item = player.getEquipment().get(slot);
        if (item == null) {
            return;
        }
        Lock lock = player.getLocks().getEquipmentLock();
        if (lock != null && lock.isLocked()) {
            if (lock.getMessage() != null) {
                player.getActionSender().sendMessage(lock.getMessage());
            }
            return;
        }
        if (slot == Equipment.SLOT_WEAPON) {
            player.getActionSender().sendString("", WeaponInterface.WeaponInterfaces.UNARMED.getTextId());
        }
        int maximumAdd = player.getInventory().getMaximumAdd(item);
        if (maximumAdd < item.getCount()) {
            player.getActionSender().sendMessage("Not enough free space in your inventory.");
            return;
        }
        Plugin<Object> plugin = item.getDefinition().getConfiguration("equipment", null);
        if (plugin != null) {
            if (!(boolean) plugin.fireEvent("unequip", player, item)) {
                return;
            }
        }
        if (player.getEquipment().remove(item)) {
            player.getActionSender().sendSound(new Audio(232, 10, 1));
            player.getDialogueInterpreter().close();
            player.getInventory().add(item);
            // Max cape
            if (item.getName().toLowerCase().contains("max cape")) {
                player.getConfigManager().set(491, 0, true);
            }
        }
    }
}
