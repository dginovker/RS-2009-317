package org.gielinor.game.content.bot;

import java.util.List;

import org.gielinor.game.node.entity.npc.NPC;
import org.gielinor.game.node.entity.player.Player;
import org.gielinor.game.node.item.Item;
import org.gielinor.game.node.object.GameObject;
import org.gielinor.game.world.World;
import org.gielinor.game.world.map.Location;
import org.gielinor.game.world.map.RegionManager;
import org.gielinor.rs2.model.container.Container;
import org.gielinor.rs2.model.container.impl.BankContainer;
import org.gielinor.rs2.pulse.Pulse;
import org.gielinor.rs2.pulse.impl.MovementPulse;

/**
 * Primary function and method class.
 *
 * @author Mike
 */
public class Method {

    private final Player player;
    private Container inventory;
    private Container bank;
    private Container equipment;
    private Location location;

    /**
     * Creates the method class.
     *
     * @param player The player.
     */
    public Method(Player player) {
        this.player = player;
        if (player != null) {
            this.inventory = player.getInventory();
            this.bank = player.getBank();
            this.equipment = player.getEquipment();
            this.location = player.getLocation();
        }
    }

    /**
     * Sends a game message.
     *
     * @param msg The message.
     */
    public void sendMessage(String msg) {
        player.getActionSender().sendMessage(msg);
    }

    /**
     * Sends the coordinates as a game message.
     */
    public void sendCoords() {
        int regionId = -1;
        if (RegionManager.getRegionPlane(player.getLocation()) != null && RegionManager.getRegionPlane(player.getLocation()).getRegion() != null) {
            regionId = RegionManager.getRegionPlane(player.getLocation()).getRegion().getId();
        }
        sendMessage(player.getLocation() + " [" + player.getLocation().getLocalX() + ", " + player.getLocation().getLocalY() + "] (Region ID: " + regionId + ")");
    }

    /**
     * The player's current location.
     */
    /**
     * Gets the full location.
     *
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the closest NPC to attack.
     *
     * @param name The name of the npc.
     * @return The NPC.
     */
    public NPC getClosestNpcToAttack(String name) {
        NPC current = null;
        name = name.trim().toLowerCase();
        for (NPC npc : RegionManager.getLocalNpcs(location)) {
            if (!npc.getName().equalsIgnoreCase(name)) {
                continue;
            }
            if (!npc.getDefinition().hasAttackOption()) {
                continue;
            }
            if (npc.inCombat()) {
                continue;
            }
            if (current == null) {
                current = npc;
                continue;
            }
            if (location.getDistance(npc.getLocation()) < location.getDistance(current.getLocation())) {
                current = npc;
            }
        }
        return current;
    }

    /**
     * Gets the closest NPC by name.
     *
     * @param name The name of the npc.
     * @return The NPC.
     */
    public NPC getClosestNpc(String name) {
        NPC current = null;
        name = name.trim().toLowerCase();
        for (NPC npc : RegionManager.getLocalNpcs(location)) {
            if (!npc.getName().equalsIgnoreCase(name)) {
                continue;
            }
            if (current == null) {
                current = npc;
                continue;
            }
            if (location.getDistance(npc.getLocation()) < location.getDistance(current.getLocation())) {
                current = npc;
            }
        }
        return current;
    }


    /**
     * Gets the closest NPC by id.
     *
     * @param npcId The id of the NPC.
     * @return The NPC.
     */
    public NPC getClosestNpc(int npcId) {
        NPC current = null;
        for (NPC npc : RegionManager.getLocalNpcs(location)) {
            if (npc.getId() != npcId) {
                continue;
            }
            if (current == null) {
                current = npc;
                continue;
            }
            if (location.getDistance(npc.getLocation()) < location.getDistance(current.getLocation())) {
                current = npc;
            }
        }
        return current;
    }

    /**
     * Attacks an NPC.
     *
     * @param npc The NPC to attack.
     */
    public void attackNPC(NPC npc) {
        if (player.getRunningBot() == null) {
            return;
        }
        if (npc == null) {
            return;
        }
        walkTo(npc.getLocation());
        World.submit(new Pulse(1, player, npc) {

            @Override
            public boolean pulse() {
                if (player.getRunningBot() == null) {
                    return true;
                }
                if (!npc.getDefinition().hasAttackOption()) {
                    return true;
                }
                if (!player.getLocation().withinDistance(npc.getLocation())) {
                    return false;
                }
                if (npc.inCombat()) {
                    return false;
                }
                if (npc.getInteraction().get(1) == null || npc.getInteraction().get(1).getHandler() == null) {
                    return false;
                }
                npc.getInteraction().get(1).getHandler().handle(player, npc, "attack");
                return false;
            }
        });
    }

    /**
     * Banks all items except for any of the ids of items given.
     *
     * @param itemIds The item ids not to bank.
     */
    public void bankAllExcept(int... itemIds) {
        World.submit(new Pulse(4) {

            boolean checked = false;

            @Override
            public boolean pulse() {
                if (isBankOpen() && !checked) {
                    for (int i = 0; i < 28; i++) {
                        Item item = inventory.get(i);
                        boolean toContinue = true;
                        if (item == null) {
                            continue;
                        }
                        for (int itemId : itemIds) {
                            if (item.getId() == itemId) {
                                toContinue = false;
                            }
                        }
                        if (!toContinue) {
                            continue;
                        }
                        player.getBank().addItem(i, player.getInventory().getCount(item), null);
                    }
                    checked = true;
                    return false;
                }
                player.getInterfaceState().close();
                return true;
            }
        });
    }

    /**
     * Withdraws a single item from bank.
     *
     * @param itemId The id of the item to withdraw.
     */
    public void withdrawItem(int itemId) {
        if (isBankOpen()) {
            for (int i = 0; i < player.getBank().capacity(); i++) {
                Item item = bank.get(i);
                if (item == null) {
                    continue;
                }
                if (item.getId() == itemId) {
                    player.getBank().takeItem(i, 1);
                    break;
                }
            }
        }
        player.getInterfaceState().close();
    }

    /**
     * Checks if the player's equipment contains an item.
     *
     * @param itemId The id of the item to check.
     */
    public boolean equipmentContainsItem(int itemId) {
        return equipment.contains(itemId);
    }

    /**
     * Checks if the player's inventory contains an item.
     *
     * @param itemId The id of the item to check.
     */
    public boolean inventoryContainsItem(int itemId) {
        return inventory.contains(itemId);
    }

    /**
     * Gets the closest bank booth to the player.
     *
     * @return The bank booth.
     */
    public GameObject getClosestBankBooth() {
        GameObject CLOSEST_BANK_BOOTH = null;
        List<GameObject> objectList = RegionManager.getSurroundingObjects(player, "bank booth", "use-quickly", 15);
        if (objectList != null && objectList.size() > 0) {
            for (GameObject bankBooth : objectList) {
                if (bankBooth == null) {
                    continue;
                }
                if (CLOSEST_BANK_BOOTH == null) {
                    CLOSEST_BANK_BOOTH = objectList.get(0);
                }
                if (!CLOSEST_BANK_BOOTH.getDefinition().hasAction("use-quickly")) {
                    objectList.remove(CLOSEST_BANK_BOOTH);
                    continue;
                }
                if (location.getDistance(bankBooth.getLocation()) < location.getDistance(CLOSEST_BANK_BOOTH.getLocation())) {
                    CLOSEST_BANK_BOOTH = bankBooth;
                }
            }
        }
        return CLOSEST_BANK_BOOTH;
    }

    /**
     * Walks to the nearest bank booth and opens it.
     */
    public void openBankBooth() {
        if (player == null) {
            return;
        }
        final GameObject BANK_BOOTH = getClosestBankBooth();
        if (BANK_BOOTH == null) {
            player.getActionSender().sendMessage("Unable to find bank booth, move to a bank.");
            return;
        }
        player.getPulseManager().run(new MovementPulse(player, BANK_BOOTH) {

            boolean wait = true;

            @Override
            public boolean pulse() {
                if (player.getLocation().getDistance(BANK_BOOTH.getLocation()) > 1) {
                    return false;
                }
                if (!wait) {
                    if ((BANK_BOOTH.getInteraction().get(1) != null) && BANK_BOOTH.getInteraction().get(1).getName().equalsIgnoreCase("use-quickly")) {
                        BANK_BOOTH.getInteraction().get(1).getHandler().handle(player, BANK_BOOTH, "use-quickly");
                    }
                }
                if (player.getLocation().getDistance(BANK_BOOTH.getLocation()) == 1) {
                    wait = false;
                    return false;
                }
                return true;
            }
        }, "movement");
    }

    //	public static void clickInventoryItem(int interfaceId, int componentId, int itemId, int slot) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.ACTION_BUTTON1_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(0x2a70000);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(itemId);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(slot);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void withdrawAll(int itemId, int slot) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.aClass198_2020, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(0x2fa005f);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(itemId);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(slot);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void enableQuickPrayers() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.ACTION_BUTTON1_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(0x2ed0004);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(-1);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(-1);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void clickABOne(int interfaceId, int componentId) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.ACTION_BUTTON1_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(interfaceId << 16 | componentId);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(0);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void clickABFive(int interfaceId, int componentId) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.aClass198_1993, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(interfaceId << 16 | componentId);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(0);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void depositAllItems() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.ACTION_BUTTON1_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(0xb0012);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(0);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void depositAllEquipment() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.ACTION_BUTTON1_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntV2(0xb0014);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShort128(0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(0);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}

    /**
     * Walks to the given location.
     *
     * @param location The location to walk to.
     */
    public void walkTo(Location location) {
        player.getPulseManager().run(new MovementPulse(player, location) {

            @Override
            public boolean pulse() {
                return true;
            }
        }, "movement");
    }

    /**
     * Walks to the given location by the aeeay.
     *
     * @param locations The locations to walk to.
     */
    public void walkTo(Location[] locations) {
        int index = 0;
        for (Location location : locations) {

        }
        player.getPulseManager().run(new MovementPulse(player, location) {

            @Override
            public boolean pulse() {
                return true;
            }
        }, "movement");
    }

    /**
     * The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * The inventory container.
     */
    public Container getInventory() {
        return inventory;
    }

    /**
     * The bank container.
     */
    public Container getBank() {
        return bank;
    }

    /**
     * The equipment container.
     */
    public Container getEquipment() {
        return equipment;
    }

    /**
     * Whether or not the bank is open.
     */
    public boolean isBankOpen() {
        return player.getInterfaceState().hasMainComponent(BankContainer.BANK_INVENTORY_INTERFACE) || player.getInterfaceState().hasMainComponent(5292);
    }

    //	public void pickupItem(RSGroundItem item) {
    //		Class298_Sub36 class298_sub36_1 = Class18.method359(OutcommingPacket.ITEM_TAKE_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36_1.aClass298_Sub53_Sub2_7396.writeShort(item.getLocation().getY(), 0);
    //		class298_sub36_1.aClass298_Sub53_Sub2_7396.writeShortLE(item.getLocation().getX(), 0);
    //		class298_sub36_1.aClass298_Sub53_Sub2_7396.writeShort(item.getItemId(), 0);
    //		class298_sub36_1.aClass298_Sub53_Sub2_7396.write128Byte(1, (byte) 1);
    //		client.aClass25_8711.method390(class298_sub36_1, (byte) 0);
    //	}
    //
    //	public static void clickObjectOne(RSObject object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.aClass198_2019, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeByte128(0, 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntLE(object.getId(), (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE(object.getX(), 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(object.getY());
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void clickObjectTwo(RSObject object) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.aClass198_2052, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeByte128(0, 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeIntLE(object.getId(), (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE(object.getX(), 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeShortLE128(object.getY());
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public static void sendCommand(String command) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    //		Class298_Sub36 class298_sub36 = Class18.method359(OutcommingPacket.COMMANDS_PACKET, client.aClass25_8711.aClass449_330, (byte) 0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeByte(command.length() + 3);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeByte(0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeByte(0);
    //		class298_sub36.aClass298_Sub53_Sub2_7396.writeString(command, 0x7ec7b656);
    //		client.aClass25_8711.method390(class298_sub36, (byte) 0);
    //	}
    //
    //	public RSItem getInventoryItem(int id) {
    //		if (inventory == null) {
    //			return null;
    //		}
    //		for (int i = 0; i < inventory.length; i++) {
    //			if (inventory[i] != null && inventory[i].getId() == id) {
    //				return inventory[i];
    //			}
    //		}
    //
    //		return null;
    //	}
    //
    //	public RSItem getBankItem(int id) {
    //		if (bank == null) {
    //			return null;
    //		}
    //		for (int i = 0; i < bank.length; i++) {
    //			if (bank[i] != null && bank[i].getId() == id) {
    //				return bank[i];
    //			}
    //		}
    //
    //		return null;
    //	}
    //
    //	public transient boolean inventoryContains(int ids[]) {
    //		for (int i = 0; i < ids.length; i++) {
    //			if (getInventoryItem(ids[i]) != null) {
    //				return true;
    //			}
    //		}
    //
    //		return false;
    //	}
    //
    //	public boolean withdrawAll(int itemId) {
    //		RSItem item;
    //		sendCommand("b");
    //		wait(500);
    //		depositAllItems();
    //		wait(500);
    //		item = getBankItem(itemId);
    //		if (item == null) {
    //			return false;
    //		}
    //		try {
    //			withdrawAll(item.getId(), item.getSlot());
    //			return true;
    //		} catch (IllegalArgumentException ex) {
    //			Logger.getLogger(Methods.getName()).log(Level.SEVERE, null, ex);
    //		} catch (IllegalAccessException ex) {
    //			Logger.getLogger(Methods.getName()).log(Level.SEVERE, null, ex);
    //		} catch (InvocationTargetException ex) {
    //			Logger.getLogger(Methods.getName()).log(Level.SEVERE, null, ex);
    //		}
    //		return false;
    //	}
    //
    //	public void drinkPrayerPotion() {
    //		if (getInventoryItem(143) != null) {
    //			RSItem item = getInventoryItem(143);
    //			clickInventoryItem(336, 0, item.getId(), item.getSlot());
    //			return;
    //		}
    //		if (getInventoryItem(141) != null) {
    //			RSItem item = getInventoryItem(141);
    //			clickInventoryItem(336, 0, item.getId(), item.getSlot());
    //			return;
    //		}
    //		if (getInventoryItem(139) != null) {
    //			RSItem item = getInventoryItem(139);
    //			clickInventoryItem(336, 0, item.getId(), item.getSlot());
    //			return;
    //		}
    //		try {
    //			if (getInventoryItem(2434) != null) {
    //				RSItem item = getInventoryItem(2434);
    //				clickInventoryItem(336, 0, item.getId(), item.getSlot());
    //				return;
    //			}
    //		} catch (Exception ex) {
    //			Logger.getLogger(Methods.getName()).log(Level.SEVERE, null, ex);
    //		}
    //		return;
    //	}


}
