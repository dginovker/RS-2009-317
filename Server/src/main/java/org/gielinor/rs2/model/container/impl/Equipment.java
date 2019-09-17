package org.gielinor.rs2.model.container.impl;

import org.gielinor.cache.def.impl.ItemDefinition;
import org.gielinor.game.node.entity.combat.equipment.WeaponInterface;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.world.update.flag.player.AppearanceFlag;
import org.gielinor.net.packet.PacketRepository;
import org.gielinor.net.packet.context.ContainerContext;
import org.gielinor.net.packet.out.ContainerPacket;
import org.gielinor.net.packet.out.WeightUpdate;
import org.gielinor.parser.item.ItemConfiguration;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.ContainerEvent;
import org.gielinor.rs2.model.container.ContainerListener;
import org.gielinor.rs2.plugin.Plugin;

import plugin.activity.duelarena.DuelRule;
import plugin.activity.duelarena.DuelSession;

/**
 * Represents the equipment container.
 *
 * @author Emperor
 */
public final class Equipment extends Container {

    /**
     * The equipment slots.
     */
    public static final int SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2, SLOT_WEAPON = 3,
        SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7, SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_AMMO = 13;

    /**
     * The bonus names.
     */
    private static final String[] BONUS_NAMES = { "Stab: ", "Slash: ", "Crush: ", "Magic: ", "Ranged: ", "Stab: ", "Slash: ", "Crush: ", "Magic: ", "Ranged: ", "Strength: ", "Prayer: " };

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new {@code Equipment} {@code Object}.
     *
     * @param player The player.
     */
    public Equipment(Player player) {
        super(14);
        this.player = player;
        register(new EquipmentListener(player));
    }

    @Override
    public boolean add(Item item, boolean fire) {
        return add(item, fire, true);
    }

    /**
     * Adds an item to the equipment container.
     *
     * @param item          The item to add.
     * @param fire          If we should refresh.
     * @param fromInventory If the item is being equipped from the inventory.
     * @return <code>True</code> if succesful, <code>False</code> if not.
     */
    public boolean add(Item item, boolean fire, boolean fromInventory) {
        return add(item, player.getInventory().getSlot(item), fire, fromInventory, -1);
    }

    /**
     * Adds an item to the equipment container.
     *
     * @param item          The item to add.
     * @param fire          If we should refresh.
     * @param fromInventory If the item is being equipped from the inventory.
     * @param ignoreRequirements Ignore the stat requirements
     * @return <code>True</code> if succesful, <code>False</code> if not.
     */
    public boolean add(Item item, int inventorySlot, boolean fire, boolean fromInventory, int equipId, boolean ignoreRequirements) {
        int slot = item.getDefinition().getConfiguration(ItemConfiguration.EQUIP_SLOT, -1);
        if (slot == -1 && item.getDefinition().getConfiguration(ItemConfiguration.WEAPON_INTERFACE, -1) != -1) {
            slot = 3;
        }
        if (slot < 0) {
            player.debug("No slot for item " + item.getId());
            return false; //Item can't be equipped.
        }
        DuelSession duelSession = DuelSession.getExtension(player);
        if (duelSession != null) {
            for (DuelRule duelRule : duelSession.getDuelRules()) {
                if (duelRule.getEquipmentSlot() == slot) {
                    if (duelRule.enforce(player, true)) {
                        return false;
                    }
                }
            }
        }
        if (!item.getDefinition().hasRequirement(player, true, true) && !ignoreRequirements) {
            return false;
        }
        Item current = super.get(slot);
        if (current != null && current.getId() == item.getId() && current.getDefinition().isStackable()) {
            int amount = getMaximumAdd(item);
            if (item.getCount() > amount) {
                amount += current.getCount();
            } else {
                amount = current.getCount() + item.getCount();
            }
            if (fromInventory) {
                player.getInventory().remove(new Item(item.getId(), amount - current.getCount()));
            }
            replace(new Item(item.getId(), amount), slot);
            return true;
        }
        if (fromInventory && current != null) {
            Plugin<Object> plugin = current.getDefinition().getConfiguration("equipment", null);
            if (plugin != null) {
                Object object = plugin.fireEvent("unequip", player, current, item);
                if (object != null && !((Boolean) object)) {
                    return true;
                }
            }
        }
        if (fromInventory && !player.getInventory().remove(item, inventorySlot, true)) {
            return false;
        }
        Item secondary = null;
        if (item.getDefinition().getConfiguration(ItemConfiguration.TWO_HANDED, false)) {
            secondary = get(SLOT_SHIELD);
        } else if (slot == SLOT_SHIELD) {
            secondary = get(SLOT_WEAPON);
            if (secondary != null && !secondary.getDefinition().getConfiguration(ItemConfiguration.TWO_HANDED, false)) {
                secondary = null;
            }
        }
        int currentSlot = -1;
        if (current != null) {
            currentSlot = inventorySlot;
            if (current.getDefinition().isStackable() && player.getInventory().contains(current.getId(), 1)) {
                currentSlot = -1;
            }
        }
        if (current != null && !player.getInventory().add(current, true, inventorySlot)) {
            player.getInventory().add(item);
            player.getActionSender().sendMessage("Not enough space in your inventory!");
            return false;
        }
        if (secondary != null && !player.getInventory().add(secondary)) {
            if (current != null && currentSlot != -1) {
                player.getInventory().remove(current, currentSlot, false);
            }
            player.getInventory().add(item);
            player.getActionSender().sendMessage("Not enough space in your inventory!");
            return false;
        }
        super.replace(item, slot, fire);
        if (secondary != null) {
            super.remove(secondary);
        }
        return true;
    }

    /**
     * Adds an item to the equipment container.
     *
     * @param item          The item to add.
     * @param inventorySlot The inventory slot of the item.
     * @param fire          If we should refresh.
     * @param fromInventory If the item is being equipped from the inventory.
     * @param equipId
     * @return <code>True</code> if successful, <code>false</code> if not.
     */
    public boolean add(Item item, int inventorySlot, boolean fire, boolean fromInventory, int equipId) {
        return add(item, inventorySlot, fire, fromInventory, equipId, false);
    }

    /**
     * Listens to the equipment container.
     *
     * @author Emperor
     */
    private static class EquipmentListener implements ContainerListener {

        /**
         * The player.
         */
        private final Player player;

        /**
         * Constructs a new {@code Equipment} {@code Object}.
         *
         * @param player The player.
         */
        public EquipmentListener(Player player) {
            this.player = player;
        }

        @Override
        public void update(Container c, ContainerEvent event) {
            int[] slots = event.getSlots();
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 1688, 28, 94, event.getItems(), false, slots));
            update(c);
            boolean updateDefenceAnimation = false;
            for (int slot : slots) {
                if (slot == Equipment.SLOT_WEAPON) {
                    player.getProperties().setAttackSpeed(c.getNew(slot).getDefinition().getConfiguration(ItemConfiguration.ATTACK_SPEED, 4));
                    WeaponInterface inter = player.getExtension(WeaponInterface.class);
                    if (inter == null) {
                        break;
                    }
                    inter.updateInterface();
                    updateDefenceAnimation = true;
                } else if (slot == Equipment.SLOT_SHIELD) {
                    updateDefenceAnimation = true;
                }
            }
            if (updateDefenceAnimation) {
                player.getProperties().updateDefenceAnimation();
            }
        }

        @Override
        public void refresh(Container c) {
            player.getProperties().
                setAttackSpeed(
                    c.getNew(3)
                        .getDefinition().
                        getConfiguration(ItemConfiguration.ATTACK_SPEED, 4)
                );
            WeaponInterface inter = player.getExtension(WeaponInterface.class);
            if (inter != null) {
                inter.updateInterface();
            }
            PacketRepository.send(ContainerPacket.class, new ContainerContext(player, 1688, 28, 94, c.toArray(), 14, false));
            update(c);
            player.getProperties().updateDefenceAnimation();
        }

        /**
         * Updates the bonuses, weight, animations, ...
         *
         * @param c The container.
         */
        public void update(Container c) {
            if (player.getAttribute("dfs_spec", false)) {
                player.removeAttribute("dfs_spec");
                player.getProperties().getCombatPulse().setHandler(null);
                if (!player.getSettings().isSpecialToggled()) {
                    player.getConfigManager().set(301, 0);
                }
            }
            player.getAppearance().setAnimations();
            player.getUpdateMasks().register(new AppearanceFlag(player));
            player.getSettings().updateWeight();
            updateBonuses(player);
        }
    }

    /**
     * Updates the bonuses.
     *
     * @param player The player.
     */
    public static void updateBonuses(Player player) {
        int[] bonuses = new int[16];
        for (Item item : player.getEquipment().toArray()) {
            if (item != null) {
                int[] bonus = item.getDefinition().getConfiguration(ItemConfiguration.BONUS, new int[16]);
                for (int i = 0; i < bonus.length; i++) {
                    if (i == 14 && bonuses[i] != 0) {
                        continue;
                    }
                    bonuses[i] += bonus[i];
                }
            }
        }
        Item shield = player.getEquipment().get(SLOT_SHIELD);
        if (shield != null && shield.getId() == 11283) {
            bonuses[5] += shield.getCharge() / 20;
            bonuses[6] += shield.getCharge() / 20;
            bonuses[7] += shield.getCharge() / 20;
            bonuses[9] += shield.getCharge() / 20;
        }
        player.getProperties().setBonuses(bonuses);
        update(player);
    }

    /**
     * Updates the equipment stats interface.
     *
     * @param player The player to update for.
     */
    public static void update(Player player) {
        if (!player.getInterfaceState().hasMainComponent(21172)) {
            return;
        }
        PacketRepository.send(WeightUpdate.class, player.getActionSender().getContext());
        int[] bonuses = player.getProperties().getBonuses();
        for (int index = 0; index < ItemDefinition.STRING_ID.length; index++) {
            int bonus = bonuses[index];
            String bonusValue = bonus > -1 ? ("+" + bonus) : Integer.toString(bonus);
            player.getActionSender().sendString(Integer.valueOf(ItemDefinition.STRING_ID[index][0]), ItemDefinition.STRING_ID[index][1] + ": " + bonusValue);
        }
    }

    /**
     * Checks if a slot is used.
     *
     * @param index The slot index.
     * @return <code>True</code> if so.
     */
    public boolean isSlotUsed(int index) {
        return get(index) != null;
    }

    public void toBank() {
        player.getBank().addAll(this);
        player.getEquipment().clear();
    }
}
